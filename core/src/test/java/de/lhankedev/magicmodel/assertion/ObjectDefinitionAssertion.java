package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.AttributeDefinition;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

import java.util.Optional;

public class ObjectDefinitionAssertion extends AbstractAssert<ObjectDefinitionAssertion, ObjectDefinition> {

    private ObjectDefinitionAssertion(final ObjectDefinition actual) {
        super(actual, ObjectDefinitionAssertion.class);
    }

    public static ObjectDefinitionAssertion assertThat(final ObjectDefinition actual) {
        return new ObjectDefinitionAssertion(actual);
    }

    public ObjectDefinitionAssertion hasId(final String id) {
        Optional<String> actualId = actual.getId();
        Assertions.assertThat(actualId)
                .isPresent();
        Assertions.assertThat(actualId.get())
                .isEqualTo(id);
        return this;
    }

    public ObjectDefinitionAssertion hasParent(final String parent) {
        Optional<String> actualParent = actual.getParent();
        Assertions.assertThat(actualParent)
                .isPresent();
        Assertions.assertThat(actualParent.get())
                .isEqualTo(parent);
        return this;
    }

    public ObjectDefinitionAssertion hasType(final String type) {
        Assertions.assertThat(actual.getType())
                .isEqualTo(type);
        return this;
    }

    public ObjectDefinitionAssertion hasAttributeCount(final int attributeCount) {
        Assertions.assertThat(actual.getAttributes())
                .isNotNull()
                .hasSize(attributeCount);
        return this;
    }

    public AttributeDefinitionAssertion getAttribute(final int index) {
        Assertions.assertThat(actual.getAttributes())
                .isNotNull();
        Assertions.assertThat(actual.getAttributes().size())
                .isGreaterThan(index);
        return AttributeDefinitionAssertion
                .assertThat(actual.getAttributes().get(index))
                .isNotNull();
    }

    public AttributeDefinitionAssertion getAttributeByName(final String name) {
        Optional<AttributeDefinition> targetAttribute = actual.getAttributes().stream()
                .filter(attribute -> name.equals(attribute.getAttributeName()))
                .findFirst();
        Assertions.assertThat(targetAttribute)
                .isPresent();
        return AttributeDefinitionAssertion
                .assertThat(targetAttribute.get());
    }
}
