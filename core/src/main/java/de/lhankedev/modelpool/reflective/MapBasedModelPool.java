package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.ModelPool;
import de.lhankedev.modelpool.model.ObjectDefinition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PACKAGE;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
class MapBasedModelPool implements ModelPool {

    Map<String, CreatedModelPoolObject> modelObjectsById;
    Map<ObjectDefinition, CreatedModelPoolObject> modelObjectsByDefinition;
    @Getter(PACKAGE)
    List<CreatedModelPoolObject> createdObjects;

    MapBasedModelPool() {
        this.modelObjectsById = new LinkedHashMap<>();
        this.modelObjectsByDefinition = new LinkedHashMap<>();
        this.createdObjects = new ArrayList<>();
    }

    void addObjectInstance(final ObjectDefinition objectDefinition, final Object object) {
        final CreatedModelPoolObject createdModelPoolObject = new CreatedModelPoolObject(objectDefinition, object);
        this.createdObjects.add(createdModelPoolObject);
        modelObjectsByDefinition.put(objectDefinition, createdModelPoolObject);
        objectDefinition.getId().ifPresent(id -> modelObjectsById.put(id, createdModelPoolObject));
    }

    Optional<CreatedModelPoolObject> getObjectByDefinition(final ObjectDefinition definition) {
        return Optional.ofNullable(modelObjectsByDefinition.get(definition));
    }

    Optional<CreatedModelPoolObject> getCreatedModelPoolObjectById(final String id) {
        return Optional.ofNullable(modelObjectsById.get(id));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getObjectById(final String id, final Class<T> expectedClass) {
        return Optional.ofNullable(modelObjectsById.get(id)).map(modelObject -> (T) modelObject.getCreatedObject());
    }
}
