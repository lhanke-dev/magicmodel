package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.Person;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class PersonsAssertion extends AbstractAssert<PersonsAssertion, Collection<Person>> {

    private PersonsAssertion(final Collection<Person> actual) {
        super(actual, PersonsAssertion.class);
    }

    public static PersonsAssertion assertThat(final Collection<Person> actual) {
        return new PersonsAssertion(actual);
    }

    public PersonsAssertion hasSize(final int expectedSize) {
        Assertions.assertThat(actual)
                .hasSize(expectedSize);
        return this;
    }

    public PersonsAssertion contains(final String foreName, final String lastName, final int age) {

        Assertions.assertThat(actual)
                .isNotNull();

        Optional<Person> matchingPerson = actual.stream()
                .filter(person -> Objects.equals(foreName, person.getForeName())
                        && Objects.equals(lastName, person.getLastName())
                        && Objects.equals(age, person.getAge()))
                .findAny();

        if (matchingPerson.isEmpty()) {
            failWithMessage("Did not find person with foreName %s, lastName %s and age %s in collection %s",
                    foreName, lastName, age, actual);
        }

        return this;
    }

}
