package de.lhankedev.modelpool.assertion;

import de.lhankedev.modelpool.model.ModelPoolDefinition;
import de.lhankedev.modelpool.model.ObjectDefinition;

import java.util.Optional;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ModelPoolAssertion extends AbstractAssert<ModelPoolAssertion, ModelPoolDefinition> {

    private ModelPoolAssertion(final ModelPoolDefinition actual) {
        super(actual, ModelPoolAssertion.class);
    }

    public static ModelPoolAssertion assertThat(final ModelPoolDefinition actual) {
        return new ModelPoolAssertion(actual);
    }

    public ModelPoolAssertion hasName(final String name) {
        Assertions.assertThat(actual.getName())
                .isEqualTo(name);
        return this;
    }

    public ModelPoolAssertion hasNamespace(final String namespace) {
        final Optional<String> actualNamespace = actual.getNamespace();
        Assertions.assertThat(actualNamespace)
                .isPresent()
                .contains(namespace);
        return this;
    }

    public ModelPoolAssertion hasObjectCount(final int objectCount) {
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
