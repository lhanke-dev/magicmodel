package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.model.ObjectDefinition;

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
class MapBasedMagicModel implements MagicModel {

    Map<String, CreatedMagicModelObject> modelObjectsById;
    Map<ObjectDefinition, CreatedMagicModelObject> modelObjectsByDefinition;
    @Getter(PACKAGE)
    List<CreatedMagicModelObject> createdObjects;

    MapBasedMagicModel() {
        this.modelObjectsById = new LinkedHashMap<>();
        this.modelObjectsByDefinition = new LinkedHashMap<>();
        this.createdObjects = new ArrayList<>();
    }

    void addObjectInstance(final ObjectDefinition objectDefinition, final Object object) {
        final CreatedMagicModelObject createdMagicModelObject = new CreatedMagicModelObject(objectDefinition, object);
        this.createdObjects.add(createdMagicModelObject);
        modelObjectsByDefinition.put(objectDefinition, createdMagicModelObject);
        objectDefinition.getId().ifPresent(id -> modelObjectsById.put(id, createdMagicModelObject));
    }

    Optional<CreatedMagicModelObject> getObjectByDefinition(final ObjectDefinition definition) {
        return Optional.ofNullable(modelObjectsByDefinition.get(definition));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getObjectById(final String id, final Class<T> expectedClass) {
        return Optional.ofNullable(modelObjectsById.get(id)).map(modelObject -> (T) modelObject.getCreatedObject());
    }
}
