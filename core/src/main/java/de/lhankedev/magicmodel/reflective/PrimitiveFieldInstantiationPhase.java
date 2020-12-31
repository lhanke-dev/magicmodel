package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.AttributeDefinition;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static de.lhankedev.magicmodel.model.AttributeDefinition.OBJECT_REFERENCE_PREFIX;
import static java.lang.String.format;

public class PrimitiveFieldInstantiationPhase implements ModelCreationPhase {

    private final MagicModelReflections reflections;

    PrimitiveFieldInstantiationPhase() {
        this.reflections = new MagicModelReflections();
    }

    @Override
    public ModelCreationContext perform(final ModelCreationContext context) {
        context.getMapBasedMagicModel().getCreatedObjects()
                .forEach(this::instantiatePrimitiveFields);
        return context;
    }

    private void instantiatePrimitiveFields(final CreatedMagicModelObject createdMagicModelObject) {
        final List<AttributeDefinition> primitiveAttributes = createdMagicModelObject.getMmObjectDefinition()
                .getAttributes().stream()
                .filter(this::isPrimitiveAttribute)
                .collect(Collectors.toList());

        final Object objectInstance = createdMagicModelObject.getCreatedObject();
        primitiveAttributes.forEach(attributeDefinition ->
                this.fillAttributeValues(attributeDefinition.getAttributeName(),
                        attributeDefinition.getAttributeValues(), objectInstance));
    }

    private void fillAttributeValues(final String attributeName, final List<String> attributeValues,
                                     final Object objectInstance) {
        try {
            final Field field = objectInstance.getClass().getDeclaredField(attributeName);
            final Class<?> targetValueType = Collection.class.isAssignableFrom(field.getType()) ?
                    reflections.getCollectionElementType(field.getGenericType()).orElse(Object.class) :
                    field.getType();

            final List<Object> targetValues = attributeValues.stream()
                    .map(stringValue -> convertToTargetPrimitiveType(targetValueType, stringValue))
                    .collect(Collectors.toList());
            final Object valueToSet = reflections.alignOrUnwrapCollectionType(field, targetValues);

            reflections.injectValueIntoField(field, objectInstance, valueToSet);
        } catch (final NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s with value %s into object of type %s",
                            attributeName, attributeValues, objectInstance.getClass()), e);
        }
    }

    private Object convertToTargetPrimitiveType(final Class<?> attributeType, final String value) {
        if (attributeType == int.class || attributeType == Integer.class) {
            return Integer.parseInt(value);
        } else if (attributeType == long.class || attributeType == Long.class) {
            return Long.parseLong(value);
        } else if (attributeType == double.class || attributeType == Double.class) {
            return Double.parseDouble(value);
        } else if (attributeType == float.class || attributeType == Float.class) {
            return Float.parseFloat(value);
        } else if (attributeType == boolean.class || attributeType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (attributeType == String.class) {
            return value;
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported terminal type %s found to be set to %s",
                attributeType, value));
    }


    private boolean isPrimitiveAttribute(final AttributeDefinition attributeDefinition) {
        return attributeDefinition.getAttributeValues()
                .stream()
                .noneMatch(value -> value.startsWith(OBJECT_REFERENCE_PREFIX));
    }
}
