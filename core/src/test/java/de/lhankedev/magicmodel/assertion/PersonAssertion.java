package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.Person;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class PersonAssertion extends AbstractAssert<PersonAssertion, Person> {

    private PersonAssertion(final Person actual) {
        super(actual, PersonAssertion.class);
    }

    public static PersonAssertion assertThat(final Person actual) {
        return new PersonAssertion(actual);
    }

    public PersonAssertion hasForeName(final String expected) {
        Assertions.assertThat(actual.getForeName())
                .isEqualTo(expected);
        return this;
    }

    public PersonAssertion hasLastName(final String expected) {
        Assertions.assertThat(actual.getLastName())
                .isEqualTo(expected);
        return this;
    }

    public PersonAssertion hasAge(final int expected) {
        Assertions.assertThat(actual.getAge())
                .isEqualTo(expected);
        return this;
    }

}
