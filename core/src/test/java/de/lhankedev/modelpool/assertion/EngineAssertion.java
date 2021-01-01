package de.lhankedev.modelpool.assertion;

import de.lhankedev.modelpool.model.Engine;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class EngineAssertion extends AbstractAssert<EngineAssertion, Engine> {

    private EngineAssertion(final Engine actual) {
        super(actual, EngineAssertion.class);
    }

    public static EngineAssertion assertThat(final Engine actual) {
        return new EngineAssertion(actual);
    }

    public EngineAssertion hasHorsePower(final int expected) {
        Assertions.assertThat(actual.getHorsePower())
                .isEqualTo(expected);
        return this;
    }

    public EngineAssertion hasDisplacement(final int expected) {
        Assertions.assertThat(actual.getDisplacement())
                .isEqualTo(expected);
        return this;
    }

}
