package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.MagicModelDefinition;
import de.lhankedev.magicmodel.model.ObjectDefinition;

import java.util.Optional;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class MagicModelAssertion extends AbstractAssert<MagicModelAssertion, MagicModelDefinition> {

    private MagicModelAssertion(final MagicModelDefinition actual) {
        super(actual, MagicModelAssertion.class);
    }

    public static MagicModelAssertion assertThat(final MagicModelDefinition actual) {
        return new MagicModelAssertion(actual);
    }

    public MagicModelAssertion hasName(final String name) {
        Assertions.assertThat(actual.getName())
                .isEqualTo(name);
        return this;
    }

    public MagicModelAssertion hasNamespace(final String namespace) {
        final Optional<String> actualNamespace = actual.getNamespace();
        Assertions.assertThat(actualNamespace)
                .isPresent()
                .contains(namespace);
        return this;
    }

    public MagicModelAssertion hasObjectCount(final int objectCount) {
        Assertions.assertThat(actual.getObjects())
                .isNotNull()
                .hasSize(objectCount);
        return this;
    }

    public ObjectDefinitionAssertion getObject(final int index) {
        Assertions.assertThat(actual.getObjects())
                .isNotNull();
        Assertions.assertThat(actual.getObjects().size())
                .isGreaterThan(index);
        return ObjectDefinitionAssertion
                .assertThat(actual.getObjects().get(index))
                .isNotNull();
    }

    public ObjectDefinitionAssertion getObjectById(final String id) {
        final Optional<ObjectDefinition> targetObject = actual.getObjects().stream()
                .filter(object -> object.getId().isPresent())
                .filter(object -> id.equals(object.getId().get()))
                .findFirst();
        if (targetObject.isEmpty()) {
            failWithMessage("Could not find object definition with id %s in %s.", id, actual.getObjects());
        }
        Assertions.assertThat(targetObject)
                .isPresent();
        return ObjectDefinitionAssertion
                .assertThat(targetObject.get());
    }
}
