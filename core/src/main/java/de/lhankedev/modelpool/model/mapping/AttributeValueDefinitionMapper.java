package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.model.AttributeValueDefinition;
import de.lhankedev.modelpool.model.ValueType;
import de.lhankedev.modelpool.reflective.StreamSupportingModelCreationException;
import de.lhankedev.modelpool.util.FirstMatchExecutor;

import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import static java.lang.String.format;

@Mapper(
        imports = {
                Optional.class,
                ModelPoolParser.class,
                ModelPoolParser.SingleValueContext.class,
                ValueType.class,
                FirstMatchExecutor.class
        }
)
public interface AttributeValueDefinitionMapper {

    AttributeValueDefinitionMapper INSTANCE = Mappers.getMapper(AttributeValueDefinitionMapper.class);
    MappingHelper MAPPING_HELPER = new MappingHelper();

    String REFERENCE_PREFIX = "#";
    String PLACEHOLDER_PREFIX = "${";
    String PLACEHOLDER_SUFFIX = "}";

    class MappingHelper {

        ValueType getValueType(final ModelPoolParser.SingleValueContext valueDefinition) {
            return new FirstMatchExecutor<ModelPoolParser.SingleValueContext, ValueType>(valueDefinition)
                    .addAlternative(context -> context.terminalValue() != null, context -> ValueType.TERMINAL)
                    .addAlternative(context -> context.placeholder() != null, context -> ValueType.PLACEHOLDER)
                    .addAlternative(context -> context.reference() != null, context -> ValueType.REFERENCE)
                    .perform().orElseThrow(() -> new StreamSupportingModelCreationException(
                            format("Could not determine valueDefinition type. Was none of: [%s, %s, %s]",
                                    ValueType.TERMINAL, ValueType.PLACEHOLDER, ValueType.REFERENCE)));
        }

        String getValue(final ModelPoolParser.SingleValueContext valueDefinition) {
            return new FirstMatchExecutor<ModelPoolParser.SingleValueContext, String>(valueDefinition)
                    .addAlternative(context -> context.terminalValue() != null,
                            context -> context.terminalValue().getText())
                    .addAlternative(context -> context.placeholder() != null,
                            context -> this.stripPlaceholderTokens(context.placeholder().getText().trim()))
                    .addAlternative(context -> context.reference() != null,
                            context -> this.stripReferenceTokens(context.reference().getText().trim()))
                    .perform().orElseThrow(() -> new StreamSupportingModelCreationException(
                            format("Could not determine valueDefinition type. Was none of: [%s, %s, %s]",
                                    ValueType.TERMINAL, ValueType.PLACEHOLDER, ValueType.REFERENCE))).trim();
        }

        private String stripReferenceTokens(final String valueWithTokens) {
            return valueWithTokens.substring(REFERENCE_PREFIX.length());
        }

        private String stripPlaceholderTokens(final String valueWithTokens) {
            return valueWithTokens.substring(PLACEHOLDER_PREFIX.length(),
                    valueWithTokens.length() - PLACEHOLDER_SUFFIX.length());
        }

    }

    @Mapping(expression = "java(MAPPING_HELPER.getValueType(valueDefinition))", target = "type")
    @Mapping(expression = "java(MAPPING_HELPER.getValue(valueDefinition))", target = "value")
    AttributeValueDefinition antlrAttributeValueDefinitionToAttributeValueDefinition(
            ModelPoolParser.SingleValueContext valueDefinition);

}
