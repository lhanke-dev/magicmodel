package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.*;

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

    Optional<CreatedMagicModelObject> getObjectByDefinition(ObjectDefinition definition) {
        return Optional.ofNullable(modelObjectsByDefinition.get(definition));
    }

    @Override
    public <T> Optional<T> getObjectById(String id, Class<T> expectedClass) {
        return Optional.ofNullable(modelObjectsById.get(id)).map(modelObject -> (T) modelObject.getCreatedObject());
    }
}
