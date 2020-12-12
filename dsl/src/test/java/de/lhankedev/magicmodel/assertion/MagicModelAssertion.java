package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.MagicModel;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class MagicModelAssertion extends AbstractAssert<MagicModelAssertion, MagicModel> {

    private MagicModelAssertion(final MagicModel actual) {
        super(actual, MagicModelAssertion.class);
    }

    public static MagicModelAssertion assertThat(final MagicModel actual) {
        return new MagicModelAssertion(actual);
    }

    public MagicModelAssertion hasName(final String name) {
        Assertions.assertThat(actual.getName())
                .isEqualTo(name);
        return this;
    }

    public MagicModelAssertion hasNamespace(final String namespace) {
        Assertions.assertThat(actual.getNamespace())
                .isEqualTo(namespace);
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
        Optional<ObjectDefinition> targetObject = actual.getObjects().stream()
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
