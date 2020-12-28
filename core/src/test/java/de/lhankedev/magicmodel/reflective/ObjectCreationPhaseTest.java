package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.Car;
import de.lhankedev.magicmodel.model.Engine;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class ObjectCreationPhaseTest {

    private final ObjectCreationPhase cut = new ObjectCreationPhase();

    @Test
    void testObjectCreation() {
        final String testId = "testId";
        ObjectDefinition objectWithId = new ObjectDefinition();
        objectWithId.setId(Optional.of(testId));
        objectWithId.setType(Car.class.getSimpleName());
        ObjectDefinition objectWithoutId = new ObjectDefinition();
        objectWithoutId.setType(Engine.class.getSimpleName());
        MagicModelDefinition modelDefinition = new MagicModelDefinition();
        modelDefinition.setObjects(Arrays.asList(
                objectWithId, objectWithoutId
        ));
        modelDefinition.setNamespace(Optional.of(Car.class.getPackageName()));
        ModelCreationContext modelCreationContext = new ModelCreationContext(modelDefinition);

        ModelCreationContext contextWithCreatedObjects = cut.perform(modelCreationContext);

        List<CreatedMagicModelObject> createdObjects = contextWithCreatedObjects.getMapBasedMagicModel().getCreatedObjects();
        Assertions.assertThat(createdObjects)
                .hasSize(2);
        List<Class<?>> createdInstanceTypes = createdObjects.stream()
                .map(CreatedMagicModelObject::getCreatedObject)
                .map(Object::getClass)
                .collect(Collectors.toList());
        Assertions.assertThat(createdInstanceTypes)
                .containsExactlyInAnyOrder(Car.class, Engine.class);

        Optional<Car> carInstance = contextWithCreatedObjects.getMapBasedMagicModel().getObjectById(testId, Car.class);
        Assertions.assertThat(carInstance)
                .isPresent();
        Assertions.assertThat(carInstance.get())
                .isNotNull();
    }

}
