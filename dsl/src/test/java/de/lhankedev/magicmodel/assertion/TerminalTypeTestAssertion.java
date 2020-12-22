package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.TerminalTypeTest;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class TerminalTypeTestAssertion extends AbstractAssert<TerminalTypeTestAssertion, TerminalTypeTest> {

    private TerminalTypeTestAssertion(final TerminalTypeTest actual) {
        super(actual, TerminalTypeTestAssertion.class);
    }

    public static TerminalTypeTestAssertion assertThat(final TerminalTypeTest actual) {
        return new TerminalTypeTestAssertion(actual);
    }

    public TerminalTypeTestAssertion hasIntValue(final int expected) {
        Assertions.assertThat(actual.getIntValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasLongValue(final long expected) {
        Assertions.assertThat(actual.getLongValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasDoubleValue(final double expected) {
        Assertions.assertThat(actual.getDoubleValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasFloatValue(final float expected) {
        Assertions.assertThat(actual.getFloatValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasBoolValue(final boolean expected) {
        Assertions.assertThat(actual.isBoolValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasStringValue(final String expected) {
        Assertions.assertThat(actual.getStringValue())
                .isEqualTo(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasIntCollection(final Integer... expected) {
        Assertions.assertThat(actual.getIntCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasLongCollection(final Long... expected) {
        Assertions.assertThat(actual.getLongCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasDoubleCollection(final Double... expected) {
        Assertions.assertThat(actual.getDoubleCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasFloatCollection(final Float... expected) {
        Assertions.assertThat(actual.getFloatCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasBoolCollection(final Boolean... expected) {
        Assertions.assertThat(actual.getBooleanCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasStringCollection(final String... expected) {
        Assertions.assertThat(actual.getStringCollection())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasIntList(final Integer... expected) {
        Assertions.assertThat(actual.getIntList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasLongList(final Long... expected) {
        Assertions.assertThat(actual.getLongList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasDoubleList(final Double... expected) {
        Assertions.assertThat(actual.getDoubleList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasFloatList(final Float... expected) {
        Assertions.assertThat(actual.getFloatList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasBoolList(final Boolean... expected) {
        Assertions.assertThat(actual.getBooleanList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasStringList(final String... expected) {
        Assertions.assertThat(actual.getStringList())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasIntSet(final Integer... expected) {
        Assertions.assertThat(actual.getIntSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasLongSet(final Long... expected) {
        Assertions.assertThat(actual.getLongSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasDoubleSet(final Double... expected) {
        Assertions.assertThat(actual.getDoubleSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasFloatSet(final Float... expected) {
        Assertions.assertThat(actual.getFloatSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasBoolSet(final Boolean... expected) {
        Assertions.assertThat(actual.getBooleanSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

    public TerminalTypeTestAssertion hasStringSet(final String... expected) {
        Assertions.assertThat(actual.getStringSet())
                .containsExactlyInAnyOrder(expected);
        return this;
    }

}
