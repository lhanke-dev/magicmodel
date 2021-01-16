package de.lhankedev.modelpool.assertion;

import de.lhankedev.modelpool.model.AttributeDefinition;
import de.lhankedev.modelpool.model.AttributeValueDefinition;
import de.lhankedev.modelpool.model.ValueType;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class AttributeDefinitionAssertion extends AbstractAssert<AttributeDefinitionAssertion, AttributeDefinition> {

    private AttributeDefinitionAssertion(final AttributeDefinition actual) {
        super(actual, AttributeDefinitionAssertion.class);
    }

    public static AttributeDefinitionAssertion assertThat(final AttributeDefinition actual) {
        return new AttributeDefinitionAssertion(actual);
    }

    public AttributeDefinitionAssertion hasName(final String name) {
        Assertions.assertThat(actual.getAttributeName())
                .isEqualTo(name);
        return this;
    }

    private AttributeDefinitionAssertion hasValues(final ValueType type, final String... values) {
        final List<AttributeValueDefinition> attributeValues = actual.getAttributeValues();
        Assertions.assertThat(attributeValues)
                .isNotNull();
        final List<String> terminalValues = attributeValues.stream()
                .filter(value -> value.getType() == type)
                .map(AttributeValueDefinition::getValue)
                .collect(Collectors.toList());
        Assertions.assertThat(terminalValues)
                .isNotNull()
                .containsExactlyInAnyOrder(values);
        return this;
    }

    public AttributeDefinitionAssertion hasTerminalValues(final String... values) {
        return hasValues(ValueType.TERMINAL, values);
    }

    public AttributeDefinitionAssertion hasReferenceValues(final String... values) {
        return hasValues(ValueType.REFERENCE, values);
    }

    public AttributeDefinitionAssertion hasPlaceholderValues(final String... values) {
        return hasValues(ValueType.PLACEHOLDER, values);
    }

}
