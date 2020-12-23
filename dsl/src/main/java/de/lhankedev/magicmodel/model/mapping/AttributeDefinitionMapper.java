package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.model.AttributeDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.stream.Collectors;

@Mapper(
        imports = {
                Collectors.class,
                MagicmodelParser.SingleValueContext.class,
                Collections.class,
                String.class
        }
)
public interface AttributeDefinitionMapper {

    AttributeDefinitionMapper INSTANCE = Mappers.getMapper(AttributeDefinitionMapper.class);

    @Mappings({
            @Mapping(expression = "java(attributeDefinition.attributeName().getText().trim())", target = "attributeName"),
            @Mapping(expression = "java(attributeDefinition.attributeValue().singleValue() == null ? " +
                    "attributeDefinition.attributeValue().listValue().singleValue().stream()" +
                    ".map(SingleValueContext::getText)" +
                    ".map(String::trim)" +
                    ".collect(Collectors.toList()) : " +
                    "Collections.singletonList(attributeDefinition.attributeValue().singleValue().getText().trim()))",
                    target = "attributeValues")}
    )
    AttributeDefinition antlrAttributeDefinitionToAttributeDefinition(MagicmodelParser.AttributeDefinitionContext attributeDefinition);

}
