package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.model.AttributeDefinition;

import java.util.Collections;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        imports = {
                Collectors.class,
                ModelPoolParser.SingleValueContext.class,
                Collections.class,
                String.class
        }
)
public interface AttributeDefinitionMapper {

    AttributeDefinitionMapper INSTANCE = Mappers.getMapper(AttributeDefinitionMapper.class);


    @Mapping(expression = "java(attributeDefinition.attributeName().getText().trim())", target = "attributeName")
    @Mapping(expression = "java(attributeDefinition.attributeValue().singleValue() == null ? " +
            "attributeDefinition.attributeValue().listValue().singleValue().stream()" +
            ".map(SingleValueContext::getText)" +
            ".map(String::trim)" +
            ".collect(Collectors.toList()) : " +
            "Collections.singletonList(attributeDefinition.attributeValue().singleValue().getText().trim()))",
            target = "attributeValues")
    AttributeDefinition antlrAttributeDefinitionToAttributeDefinition(
            ModelPoolParser.AttributeDefinitionContext attributeDefinition);

}
