package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.ModelPool;
import de.lhankedev.modelpool.ModelPoolCreationContext;
import de.lhankedev.modelpool.ModelPoolFactory;
import de.lhankedev.modelpool.assertion.EngineAssertion;
import de.lhankedev.modelpool.assertion.MobilePhoneAssertion;
import de.lhankedev.modelpool.assertion.PersonAssertion;
import de.lhankedev.modelpool.assertion.PersonsAssertion;
import de.lhankedev.modelpool.assertion.TerminalTypeTestAssertion;
import de.lhankedev.modelpool.exception.ModelPoolCreationException;
import de.lhankedev.modelpool.model.Car;
import de.lhankedev.modelpool.model.TerminalTestType;
import de.lhankedev.modelpool.model.directaccess.MobilePhone;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectiveModelPoolFactoryTest {

    private final ModelPoolFactory factory = new ReflectiveModelPoolFactory();

    @Test
    void testTerminalSingleValueFields() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("TerminalFieldSingleValues")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<TerminalTestType> testTypes = modelPool.getObjectById("testPrimitives", TerminalTestType.class);

        Assertions.assertThat(testTypes)
                .isPresent();

        TerminalTypeTestAssertion.assertThat(testTypes.get())
                .hasIntValue(12)
                .hasLongValue(3L)
                .hasBoolValue(true)
                .hasDoubleValue(22.1)
                .hasFloatValue(4.7f)
                .hasStringValue("testString");
    }

    @Test
    void testDirectFieldAccess() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("DirectFieldAccess")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<MobilePhone> phone = modelPool.getObjectById("testPhone", MobilePhone.class);

        Assertions.assertThat(phone)
                .isPresent();

        MobilePhoneAssertion.assertThat(phone.get())
                .hasScreenSize(5.6)
                .hasManufacturer("TestManufacturer")
                .hasWeight(461);
    }

    @Test
    void testTerminalCollectionValueFields() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("TerminalFieldCollectionValues")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<TerminalTestType> testTypes = modelPool.getObjectById("testPrimitives", TerminalTestType.class);

        Assertions.assertThat(testTypes)
                .isPresent();

        TerminalTypeTestAssertion.assertThat(testTypes.get())
                .hasIntCollection(1, 2)
                .hasLongCollection(3L, 4L)
                .hasBoolCollection(false, true)
                .hasDoubleCollection(1.1, 1.2)
                .hasFloatCollection(1.3f, 1.4f)
                .hasStringCollection("testString1", "testString2");
    }

    @Test
    void testTerminalListValueFields() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("TerminalFieldListValues")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<TerminalTestType> testTypes = modelPool.getObjectById("testPrimitives", TerminalTestType.class);

        Assertions.assertThat(testTypes)
                .isPresent();

        TerminalTypeTestAssertion.assertThat(testTypes.get())
                .hasIntList(1, 2)
                .hasLongList(3L, 4L)
                .hasBoolList(false, true)
                .hasDoubleList(1.1, 1.2)
                .hasFloatList(1.3f, 1.4f)
                .hasStringList("testString1", "testString2");
    }

    @Test
    void testTerminalSetValueFields() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("TerminalFieldSetValues")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<TerminalTestType> testTypes = modelPool.getObjectById("testPrimitives", TerminalTestType.class);

        Assertions.assertThat(testTypes)
                .isPresent();

        TerminalTypeTestAssertion.assertThat(testTypes.get())
                .hasIntSet(1, 2)
                .hasLongSet(3L, 4L)
                .hasBoolSet(false, true)
                .hasDoubleSet(1.1, 1.2)
                .hasFloatSet(1.3f, 1.4f)
                .hasStringSet("testString1", "testString2");
    }

    @Test
    void testReferenceInSingleAttribute() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("SingleValueReference")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<Car> car = modelPool.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        EngineAssertion.assertThat(car.get().getEngine())
                .isNotNull()
                .hasHorsePower(205)
                .hasDisplacement(4009);
    }

    @Test
    void testReferenceInCollectionAttribute() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("CollectionValueReference")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<Car> car = modelPool.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonsAssertion.assertThat(car.get().getPreviousOwners())
                .hasSize(2)
                .contains("Ele", "Phant", 28)
                .contains("Dino", "Saur", 21);
    }

    @Test
    void testQualifiedParentReference() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("QualifiedParentReference")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<Car> car = modelPool.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonAssertion.assertThat(car.get().getOwner())
                .isNotNull()
                .hasForeName("Dino")
                .hasLastName("Saur")
                .hasAge(21);
    }

    @Test
    void testImplicitParentReference() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("ImplicitParentReference")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<Car> car = modelPool.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        EngineAssertion.assertThat(car.get().getEngine())
                .isNotNull()
                .hasHorsePower(205)
                .hasDisplacement(4009);
    }

    @Test
    void testParentCollectionReference() throws ModelPoolCreationException {
        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("ParentCollectionReference")
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<Car> car = modelPool.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonsAssertion.assertThat(car.get().getPreviousOwners())
                .contains("Ele", "Phant", 28)
                .contains("Dino", "Saur", 21);
    }

    @Test
    void testPlaceholderSubstitution() throws ModelPoolCreationException {
        final String manufacturer = "testManufacturerPlaceholder";
        final int weight = 654;
        final double screenSize = 3.1;

        final ModelPoolCreationContext context = ModelPoolCreationContext.builder()
                .modelname("PlaceholderSubstitution")
                .placeholderValue("manufacturer", manufacturer)
                .placeholderValue("weight", weight)
                .placeholderValue("screenSize", screenSize)
                .build();

        final ModelPool modelPool = factory.createModel(context);
        final Optional<MobilePhone> phone = modelPool.getObjectById("testPhone", MobilePhone.class);

        Assertions.assertThat(phone)
                .isPresent();

        MobilePhoneAssertion.assertThat(phone.get())
                .hasManufacturer(manufacturer)
                .hasWeight(weight)
                .hasScreenSize(screenSize);
    }


}
