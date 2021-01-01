package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.model.AttributeDefinition;
import de.lhankedev.modelpool.model.ObjectDefinition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import lombok.Value;

import static java.lang.String.format;

public class ObjectLinkingPhase implements ModelCreationPhase {

    @Value
    private class ObjectReference {

        List<Object> targetObjects = new ArrayList<>();
        String attributeName;
        Object owner;

        ObjectReference addTargetObjects(final Stream<Object> targetObjects) {
            targetObjects.forEach(this.targetObjects::add);
            return this;
        }

    }

    private final ModelPoolReflections reflections;

    ObjectLinkingPhase() {
        this.reflections = new ModelPoolReflections();
    }

    @Override
    public ModelCreationContext perform(final ModelCreationContext context) {
        final List<ObjectReference> objectReferences = new ArrayList<>();
        context.getParsedModel().getObjects()
                .forEach(parsedObject -> this.addObjectReferences(parsedObject, objectReferences,
                        context.getMapBasedModelPool()));
        objectReferences.forEach(this::injectObjectReference);
        return context;
    }

    private void injectObjectReference(final ObjectReference objectReference) {
        try {
            final Field field = objectReference.getOwner().getClass().getDeclaredField(
                    objectReference.getAttributeName());
            final Object valueToSet = reflections.alignOrUnwrapCollectionType(field,
                    objectReference.getTargetObjects());
            reflections.injectValueIntoField(field, objectReference.getOwner(), valueToSet);
        } catch (final NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s with value %s into object of type %s",
                            objectReference.getAttributeName(), objectReference.getTargetObjects(),
                            objectReference.getOwner().getClass()), e);
        }
    }

    private void addObjectReferences(final ObjectDefinition objectDefinition,
                                     final List<ObjectReference> objectReferences,
                                     final MapBasedModelPool modelPool) {
        final Object modelObject = modelPool.getObjectByDefinition(objectDefinition)
                .orElseThrow(() -> new StreamSupportingModelCreationException(
                        format("Did not find created object for definition %s.",
                                objectDefinition))).getCreatedObject();

        objectDefinition.getAttributes().stream()
                .filter(this::containsObjectReferences)
                .map(attributeDefinition -> createForwardObjectReference(attributeDefinition, modelPool, modelObject))
                .forEach(objectReferences::add);

        objectDefinition.getParentObjectId()
                .map(parentObjectId -> modelPool.getObjectById(parentObjectId, Object.class)
                        .orElseThrow(()
                                -> new StreamSupportingModelCreationException(
                                format("Did not find parent object with id %s", parentObjectId))
                        ))
                .map(parentObject ->
                        objectDefinition.getParentAttributeName()
                                .map(parentAttributeName -> this.createReferenceByParentId(parentObject,
                                        parentAttributeName, modelObject))
                                .orElseGet(() -> this.createReferenceByParentId(parentObject, modelObject)))
                .ifPresent(parentReference -> mergeParentReferenceIntoExistingReferences(objectReferences,
                        parentReference));
    }

    private ObjectReference createForwardObjectReference(final AttributeDefinition attributeDefinition,
                                                         final MapBasedModelPool modelPool,
                                                         final Object modelObject) {
        return new ObjectReference(attributeDefinition.getAttributeName(), modelObject)
                .addTargetObjects(
                        attributeDefinition.getAttributeValues().stream().map(this::stripObjectReferencePrefix)
                                .map(targetId -> modelPool.getObjectById(targetId, Object.class))
                                .filter(Optional::isPresent)
                                .map(Optional::get));
    }

    private void mergeParentReferenceIntoExistingReferences(final List<ObjectReference> objectReferences,
                                                            final ObjectReference parentReference) {
        objectReferences.stream()
                .filter(objectReference -> parentReference.getOwner() == objectReference.getOwner() &&
                        parentReference.getAttributeName().equals(objectReference.getAttributeName()))
                .findFirst()
                .ifPresentOrElse(
                        objectReference -> objectReference.getTargetObjects().addAll(
                                parentReference.getTargetObjects()),
                        () -> objectReferences.add(parentReference));
    }

    private ObjectReference createReferenceByParentId(final Object owner, final String parentAttributeName,
                                                      final Object modelObject) {
        return new ObjectReference(parentAttributeName, owner)
                .addTargetObjects(Stream.of(modelObject));
    }

    private ObjectReference createReferenceByParentId(final Object owner, final Object modelObject) {
        return new ObjectReference(reflections.findAttributeNameWithType(owner.getClass(), modelObject.getClass())
                .orElseThrow(() ->
                        new StreamSupportingModelCreationException(
                                format("Did not find attribute with type %s in referenced parent class %s" +
                                                " please use explicit references via id.",
                                        modelObject.getClass(), owner.getClass()))), owner)
                .addTargetObjects(Stream.of(modelObject));
    }


    private String stripObjectReferencePrefix(final String targetIdValueWithPrefix) {
        return targetIdValueWithPrefix.substring(AttributeDefinition.OBJECT_REFERENCE_PREFIX.length());
    }

    private boolean containsObjectReferences(final AttributeDefinition attributeDefinition) {
        return attributeDefinition.getAttributeValues().stream()
                .anyMatch(value -> value.startsWith(AttributeDefinition.OBJECT_REFERENCE_PREFIX));
    }
}
