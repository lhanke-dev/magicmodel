package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.Car;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class CarAssertion extends AbstractAssert<CarAssertion, Car> {

    private CarAssertion(final Car actual) {
        super(actual, CarAssertion.class);
    }

    public static CarAssertion assertThat(final Car actual) {
        return new CarAssertion(actual);
    }

    public CarAssertion hasManufacturer(final String expected) {
        Assertions.assertThat(actual.getManufacturer())
                .isEqualTo(expected);
        return this;
    }

    public CarAssertion hasModel(final String expected) {
        Assertions.assertThat(actual.getModel())
                .isEqualTo(expected);
        return this;
    }

    public EngineAssertion getEngine() {
        return EngineAssertion.assertThat(actual.getEngine());
    }

    public PersonAssertion getOwner() {
        return PersonAssertion.assertThat(actual.getOwner());
    }

}
