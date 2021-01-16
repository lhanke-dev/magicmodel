package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.model.ObjectDefinition;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.lhankedev.modelpool.model.ValueType.REFERENCE;
import static java.lang.String.format;

public class ReferenceFieldCollectionPhase extends AbstractFieldCollectionPhase {

    ModelPoolReflections reflections = new ModelPoolReflections();

    ReferenceFieldCollectionPhase() {
        super(REFERENCE);
    }

    @Override
    protected void collectFieldsForObject(final ReflectiveCreationContext context,
                                          final CreatedModelPoolObject object) {
        super.collectFieldsForObject(context, object);
        final ObjectDefinition definition = object.getModelPoolObjectDefinition();
        object.getModelPoolObjectDefinition().getParentObjectId().ifPresent(
                parentId -> addResolvedParentReference(context, object, parentId, definition.getParentAttributeName()));
    }

    @Override
    protected List<Object> collectValuesForField(final ReflectiveCreationContext context, final Field field,
                                                 final List<String> values) {
        return values.stream()
                .map(id -> context.getMapBasedModelPool().getObjectById(id, Object.class)
                        .orElseThrow(() -> new StreamSupportingModelCreationException(
                                format("Did not find referenced object with id %s", id))))
                .collect(Collectors.toList());
    }

    private void addResolvedParentReference(final ReflectiveCreationContext context,
                                            final CreatedModelPoolObject object,
                                            final String parentId,
                                            final Optional<String> parentAttributeName) {
        final Class<?> targetFieldType = object.getCreatedObject().getClass();
        final CreatedModelPoolObject parentObject = context.getMapBasedModelPool()
                .getCreatedModelPoolObjectById(parentId)
                .orElseThrow(()
                        -> new StreamSupportingModelCreationException(
                        format("Did not find parent object with id %s", parentId))
                );
        final Field targetField = this.resolveTargetField(parentObject.getCreatedObject(), targetFieldType,
                parentAttributeName);
        parentObject.addResolvedFieldDefinition(targetField, object.getCreatedObject());
    }

    private Field resolveTargetField(final Object parentObject, final Class<?> targetType,
                                     final Optional<String> parentAttributeNameOpt) {
        return parentAttributeNameOpt
                .or(() -> reflections.findAttributeNameWithType(parentObject.getClass(), targetType))
                .map(parentAttributeName -> reflections.fieldByName(parentObject.getClass(), parentAttributeName))
                .orElseThrow(() -> new StreamSupportingModelCreationException(
                        format("Could not resolve parent reference for type %s with parent %s.", targetType,
                                parentObject.getClass())));
    }


}
