package de.lhankedev.modelpool.reflective;

import java.lang.reflect.Field;
import java.util.ArrayList;

class FieldInjectionPhase implements ModelCreationPhase {

    ModelPoolReflections reflections = new ModelPoolReflections();

    @Override
    public ReflectiveCreationContext perform(final ReflectiveCreationContext context) {
        context.getMapBasedModelPool().getCreatedObjects().forEach(this::injectFieldValuesForObject);
        return context;
    }

    private void injectFieldValuesForObject(final CreatedModelPoolObject createdObject) {
        createdObject.getResolvedFields()
                .forEach((key, value) -> injectField(createdObject.getCreatedObject(), key, value));
    }

    private void injectField(final Object objectInstance, final Field field, final ArrayList<Object> targetValues) {
        final Object valueToSet = reflections.alignOrUnwrapCollectionType(field, targetValues);
        reflections.injectValueIntoField(field, objectInstance, valueToSet);
    }
}
