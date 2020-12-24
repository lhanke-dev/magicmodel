package de.lhankedev.magicmodel.assertion;

import de.lhankedev.magicmodel.model.AttributeDefinition;
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

    public AttributeDefinitionAssertion hasValues(final String... values) {
        Assertions.assertThat(actual.getAttributeValues())
                .containsExactlyInAnyOrder(values);
        return this;
    }

}
