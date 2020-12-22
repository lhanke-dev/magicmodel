package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.AttributeDefinition;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

import static de.lhankedev.magicmodel.model.AttributeDefinition.OBJECT_REFERENCE_PREFIX;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class PrimitiveFieldInstantiationPhase implements ModelCreationPhase {

    private static final Logger LOG = LoggerFactory.getLogger(PrimitiveFieldInstantiationPhase.class);

    @Override
    public ModelCreationContext perform(final ModelCreationContext context) {
        context.getMapBasedMagicModel().getCreatedObjects()
                .forEach(createdObject -> this.instantiatePrimitiveFields(createdObject, context));
        return context;
    }

    private void instantiatePrimitiveFields(final CreatedMagicModelObject createdMagicModelObject, final ModelCreationContext context) {
        List<AttributeDefinition> primitiveAttributes = createdMagicModelObject.getMmObjectDefinition().getAttributes().stream()
                .filter(this::isPrimitiveAttribute)
                .collect(Collectors.toList());

        Object objectInstance = createdMagicModelObject.getCreatedObject();
        primitiveAttributes.forEach(attributeDefinition ->
                this.fillAttributeValues(attributeDefinition.getAttributeName(), attributeDefinition.getAttributeValues(), objectInstance, context));
    }

    private void fillAttributeValues(final String attributeName, final List<String> attributeValues, Object objectInstance, final ModelCreationContext context) {
        try {
            final Field field = objectInstance.getClass().getDeclaredField(attributeName);
            final Optional<Method> setterMethod = ReflectionUtils.getMethods(objectInstance.getClass(), method -> this.isSetterOfField(method, field)).stream()
                    .findFirst();

            final Object valueToSet = convertValueToTargetTypeIfNecessary(objectInstance.getClass(),
                    attributeName,
                    field,
                    attributeValues,
                    context);
            setterMethod
                    .ifPresentOrElse(
                            setterInstance -> this.injectValuesViaSetter(setterInstance, objectInstance, valueToSet),
                            () -> this.injectValuesDirectly(field, objectInstance, valueToSet)
                    );
        } catch (NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s with value %s into object of type %s",
                            attributeName, attributeValues, objectInstance.getClass()), e);
        }
    }

    private Object convertValueToTargetTypeIfNecessary(Class<?> targetClass, String attributeName, Field targetField, List<String> attributeValues, ModelCreationContext context) {
        if (!Collection.class.isAssignableFrom(targetField.getType()) && attributeValues.size() > 1) {
            throw new StreamSupportingModelCreationException(format(
                    "Could not set attribute collection value %s to field with name %s type %s in class %s",
                    attributeValues, attributeName, targetField.getType().getCanonicalName(), targetClass.getCanonicalName()));
        } else if (Collection.class.isAssignableFrom(targetField.getType())) {
            return convertToTargetCollectionType(targetField, attributeValues, context);
        } else {
            return convertToTargetPrimitiveType(targetField.getType(), attributeValues.stream().findFirst(), context);
        }
    }

    private Object convertToTargetPrimitiveType(Class<?> attributeType, Optional<String> valueOpt, ModelCreationContext context) {
        if (attributeType == int.class || attributeType == Integer.class) {
            return valueOpt.map(Integer::parseInt).orElse(null);
        } else if (attributeType == long.class || attributeType == Long.class) {
            return valueOpt.map(Long::parseLong).orElse(null);
        } else if (attributeType == double.class || attributeType == Double.class) {
            return valueOpt.map(Double::parseDouble).orElse(null);
        } else if (attributeType == float.class || attributeType == Float.class) {
            return valueOpt.map(Float::parseFloat).orElse(null);
        } else if (attributeType == boolean.class || attributeType == Boolean.class) {
            return valueOpt.map(Boolean::parseBoolean).orElse(null);
        } else if (attributeType == String.class) {
            return valueOpt.orElse("");
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported terminal type %s found to be set to %s",
                attributeType, valueOpt));
    }

    private Object convertToTargetCollectionType(Field field, List<String> attributeValues, ModelCreationContext context) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            List<Object> convertedElementValues = getCollectionElementType(field.getGenericType(), context)
                    .map(elementType -> attributeValues.stream()
                            .map(value -> convertToTargetPrimitiveType(elementType, Optional.of(value), context))
                            .collect(Collectors.toList()))
                    .orElse(attributeValues.stream()
                            .map(stringElem -> (Object) stringElem)
                            .collect(Collectors.toList()));
            if (Set.class.isAssignableFrom(field.getType())) {
                return new LinkedHashSet<>(convertedElementValues);
            }
            return convertedElementValues;
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported terminal collection type %s found to be set to %s",
                field.getType(), attributeValues));
    }

    private Optional<Class<?>> getCollectionElementType(final Type type, final ModelCreationContext context) {
        final Type[] typeArguments = type instanceof ParameterizedType ?
                ((ParameterizedType) type).getActualTypeArguments() :
                new Type[0];
        if (typeArguments.length < 1) {
            return Optional.empty();
        } else if (typeArguments.length == 1) {
            return context.clazzForName(typeArguments[0].getTypeName());
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported count of type parameters for collection type %s",
                type));
    }

    private void injectValuesDirectly(final Field field, final Object objectInstance, final Object attributeValues) {
        try {
            field.set(objectInstance, attributeValues);
        } catch (IllegalAccessException e) {
            LOG.debug("Failed to set value {} for field {} directly for object of type {}. Trying to make the field accessible.",
                    attributeValues, field.getName(), objectInstance.getClass().getCanonicalName());
            if (field.trySetAccessible()) {
                try {
                    field.set(objectInstance, attributeValues);
                } catch (IllegalAccessException illegalAccessException) {
                    throw new StreamSupportingModelCreationException(format("Failed to set value %s for field %s directly for object of type %s since the field could not be accessed. Please disable security manager or provide a setter for the field."
                            , attributeValues, field.getName(), objectInstance.getClass().getCanonicalName()), e);
                }
            }
        }
    }

    private void injectValuesViaSetter(final Method setterInstance, final Object objectInstance, final Object attributeValues) {
        try {
            setterInstance.invoke(objectInstance, attributeValues);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new StreamSupportingModelCreationException(format("Could not set value %s via setter %s for instance of type %s.",
                    attributeValues, setterInstance, objectInstance.getClass().getCanonicalName()), e);
        }
    }

    private boolean isSetterOfField(final Method method, final Field field) {
        return (method.getReturnType() == Void.class || method.getReturnType() == void.class) &&
                Modifier.isPublic(method.getModifiers()) &&
                method.getName().equals("set" + capitalize(field.getName())) &&
                method.getParameterCount() == 1 &&
                method.getParameterTypes()[0] == field.getType();
    }

    private boolean isPrimitiveAttribute(final AttributeDefinition attributeDefinition) {
        return attributeDefinition.getAttributeValues()
                .stream()
                .noneMatch(value -> value.startsWith(OBJECT_REFERENCE_PREFIX));
    }
}
