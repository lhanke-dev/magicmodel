package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.Car;
import de.lhankedev.magicmodel.model.Engine;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import de.lhankedev.magicmodel.model.ObjectDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ObjectCreationPhaseTest {

    private final ObjectCreationPhase cut = new ObjectCreationPhase();

    @Test
    void testObjectCreation() {
        final String testId = "testId";
        final ObjectDefinition objectWithId = new ObjectDefinition();
        objectWithId.setId(Optional.of(testId));
        objectWithId.setType(Car.class.getSimpleName());
        final ObjectDefinition objectWithoutId = new ObjectDefinition();
        objectWithoutId.setType(Engine.class.getSimpleName());
        final MagicModelDefinition modelDefinition = new MagicModelDefinition();
        modelDefinition.setObjects(Arrays.asList(
                objectWithId, objectWithoutId
        ));
        modelDefinition.setNamespace(Optional.of(Car.class.getPackageName()));
        final ModelCreationContext modelCreationContext = new ModelCreationContext(modelDefinition);

        final ModelCreationContext contextWithCreatedObjects = cut.perform(modelCreationContext);

        final List<CreatedMagicModelObject> createdObjects = contextWithCreatedObjects.getMapBasedMagicModel()
                .getCreatedObjects();
        Assertions.assertThat(createdObjects)
                .hasSize(2);
        final List<Class<?>> createdInstanceTypes = createdObjects.stream()
                .map(CreatedMagicModelObject::getCreatedObject)
                .map(Object::getClass)
                .collect(Collectors.toList());
        Assertions.assertThat(createdInstanceTypes)
                .containsExactlyInAnyOrder(Car.class, Engine.class);

        final Optional<Car> carInstance = contextWithCreatedObjects.getMapBasedMagicModel().getObjectById(testId,
                Car.class);
        Assertions.assertThat(carInstance)
                .isPresent();
        Assertions.assertThat(carInstance.get())
                .isNotNull();
    }

}
