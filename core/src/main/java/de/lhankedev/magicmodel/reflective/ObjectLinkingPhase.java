package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.AttributeDefinition;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import lombok.Value;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

public class ObjectLinkingPhase implements ModelCreationPhase {

    @Value
    private class ObjectReference {

        List<String> targetIds;
        String attributeName;
        Object owner;

    }

    private final MagicModelReflections reflections;

    ObjectLinkingPhase() {
        this.reflections = new MagicModelReflections();
    }

    @Override
    public ModelCreationContext perform(ModelCreationContext context) {
        List<ObjectReference> objectReferences = context.getParsedModel().getObjects().stream()
                .flatMap(parsedObject -> this.collectObjectReferences(parsedObject, context.getMapBasedMagicModel().getObjectByDefinition(parsedObject)))
                .collect(Collectors.toList());
        objectReferences.forEach(objectReference -> injectObjectReference(context, objectReference));
        return context;
    }

    private void injectObjectReference(ModelCreationContext context, ObjectReference objectReference) {
        try {
            final Field field = objectReference.getOwner().getClass().getDeclaredField(objectReference.getAttributeName());
            final BiFunction<Class<?>, Optional<String>, Object> convertTerminalValueToTargetType = (elementType, targetIdOpt) ->
                    targetIdOpt.flatMap(targetId -> context.getMapBasedMagicModel().getObjectById(targetId, Object.class)).orElseThrow(
                            () -> new StreamSupportingModelCreationException(
                                    format("Could not resolve referenced object with id %s in magic model with name %s.",
                                            context.getParsedModel().getName())));
            reflections.injectValueIntoField(objectReference.getOwner(), field, objectReference.getTargetIds(), convertTerminalValueToTargetType);
        } catch (NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s with value %s into object of type %s",
                            objectReference.getAttributeName(), objectReference.getTargetIds(), objectReference.getOwner().getClass()), e);
        }
    }

    private Stream<ObjectReference> collectObjectReferences(ObjectDefinition objectDefinition, Optional<CreatedMagicModelObject> objectByDefinition) {
        Object modelObject = objectByDefinition.orElseThrow(() -> new StreamSupportingModelCreationException(
                format("Did not find created  object for definition %s.",
                        objectDefinition))).getCreatedObject();

        return objectDefinition.getAttributes().stream()
                .filter(this::containsObjectReferences)
                .map(attributeDefinition ->
                        new ObjectReference(attributeDefinition.getAttributeValues().stream().map(this::stripObjectReferencePrefix).collect(Collectors.toList()),
                                attributeDefinition.getAttributeName(), modelObject));
    }

    private String stripObjectReferencePrefix(String targetIdValueWithPrefix) {
        return targetIdValueWithPrefix.substring(AttributeDefinition.OBJECT_REFERENCE_PREFIX.length());
    }

    private boolean containsObjectReferences(AttributeDefinition attributeDefinition) {
        return attributeDefinition.getAttributeValues().stream()
                .anyMatch(value -> value.startsWith(AttributeDefinition.OBJECT_REFERENCE_PREFIX));
    }
}
