package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.MagicModelFactory;
import de.lhankedev.magicmodel.assertion.EngineAssertion;
import de.lhankedev.magicmodel.assertion.MobilePhoneAssertion;
import de.lhankedev.magicmodel.assertion.PersonAssertion;
import de.lhankedev.magicmodel.assertion.PersonsAssertion;
import de.lhankedev.magicmodel.assertion.TerminalTypeTestAssertion;
import de.lhankedev.magicmodel.exception.MagicModelCreationException;
import de.lhankedev.magicmodel.model.Car;
import de.lhankedev.magicmodel.model.TerminalTestType;
import de.lhankedev.magicmodel.model.directaccess.MobilePhone;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectiveMagicModelFactoryTest {

    private final MagicModelFactory factory = new ReflectiveMagicModelFactory();

    @Test
    void testTerminalSingleValueFields() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("TerminalFieldSingleValues");
        final Optional<TerminalTestType> testTypes = magicModel.getObjectById("testPrimitives", TerminalTestType.class);

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
    void testDirectFieldAccess() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("DirectFieldAccess");
        final Optional<MobilePhone> phone = magicModel.getObjectById("testPhone", MobilePhone.class);

        Assertions.assertThat(phone)
                .isPresent();

        MobilePhoneAssertion.assertThat(phone.get())
                .hasScreenSize(5.6)
                .hasManufacturer("TestManufacturer")
                .hasWeight(461);
    }

    @Test
    void testTerminalCollectionValueFields() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("TerminalFieldCollectionValues");
        final Optional<TerminalTestType> testTypes = magicModel.getObjectById("testPrimitives", TerminalTestType.class);

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
    void testTerminalListValueFields() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("TerminalFieldListValues");
        final Optional<TerminalTestType> testTypes = magicModel.getObjectById("testPrimitives", TerminalTestType.class);

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
    void testTerminalSetValueFields() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("TerminalFieldSetValues");
        final Optional<TerminalTestType> testTypes = magicModel.getObjectById("testPrimitives", TerminalTestType.class);

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
    void testReferenceInSingleAttribute() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("SingleValueReference");
        final Optional<Car> car = magicModel.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        EngineAssertion.assertThat(car.get().getEngine())
                .isNotNull()
                .hasHorsePower(205)
                .hasDisplacement(4009);
    }

    @Test
    void testReferenceInCollectionAttribute() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("CollectionValueReference");
        final Optional<Car> car = magicModel.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonsAssertion.assertThat(car.get().getPreviousOwners())
                .hasSize(2)
                .contains("Ele", "Phant", 28)
                .contains("Dino", "Saur", 21);
    }

    @Test
    void testQualifiedParentReference() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("QualifiedParentReference");
        final Optional<Car> car = magicModel.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonAssertion.assertThat(car.get().getOwner())
                .isNotNull()
                .hasForeName("Dino")
                .hasLastName("Saur")
                .hasAge(21);
    }

    @Test
    void testImplicitParentReference() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("ImplicitParentReference");
        final Optional<Car> car = magicModel.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        EngineAssertion.assertThat(car.get().getEngine())
                .isNotNull()
                .hasHorsePower(205)
                .hasDisplacement(4009);
    }

    @Test
    void testParentCollectionReference() throws MagicModelCreationException {
        final MagicModel magicModel = factory.createModel("ParentCollectionReference");
        final Optional<Car> car = magicModel.getObjectById("testCar", Car.class);

        Assertions.assertThat(car)
                .isPresent();

        PersonsAssertion.assertThat(car.get().getPreviousOwners())
                .contains("Ele", "Phant", 28)
                .contains("Dino", "Saur", 21);
    }


}
