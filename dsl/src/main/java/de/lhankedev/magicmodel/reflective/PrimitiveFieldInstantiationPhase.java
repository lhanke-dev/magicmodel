package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.AttributeDefinition;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
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
        List<AttributeDefinition> primitiveAttributes = createdMagicModelObject.getMmObjectDefinition().getAttributes().stream()
                .filter(this::isPrimitiveAttribute)
                .collect(Collectors.toList());

        Object objectInstance = createdMagicModelObject.getCreatedObject();
        primitiveAttributes.forEach(attributeDefinition ->
                this.fillAttributeValues(attributeDefinition.getAttributeName(), attributeDefinition.getAttributeValues(), objectInstance));
    }

    private void fillAttributeValues(final String attributeName, final List<String> attributeValues, Object objectInstance) {
        try {
            final Field field = objectInstance.getClass().getDeclaredField(attributeName);
            final BiFunction<Class<?>, Optional<String>, Object> primitiveTypeConverterCallback = (elementClass, stringValueOpt) -> convertToTargetPrimitiveType(elementClass, stringValueOpt);
            reflections.injectValueIntoField(objectInstance, field, attributeValues, primitiveTypeConverterCallback);
        } catch (NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s with value %s into object of type %s",
                            attributeName, attributeValues, objectInstance.getClass()), e);
        }
    }

    private Object convertToTargetPrimitiveType(Class<?> attributeType, Optional<String> valueOpt) {
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


    private boolean isPrimitiveAttribute(final AttributeDefinition attributeDefinition) {
        return attributeDefinition.getAttributeValues()
                .stream()
                .noneMatch(value -> value.startsWith(OBJECT_REFERENCE_PREFIX));
    }
}
