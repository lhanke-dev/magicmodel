package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.model.AttributeDefinition;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
        imports = {
                Collectors.class,
                ModelPoolParser.AttributeDefinitionContext.class,
                AttributeValueDefinitionMapper.class,
                Stream.class
        }
)
public interface AttributeDefinitionMapper {

    AttributeDefinitionMapper INSTANCE = Mappers.getMapper(AttributeDefinitionMapper.class);

    @Mapping(expression = "java(attributeDefinition.attributeName().getText().trim())", target = "attributeName")
    @Mapping(expression = "java(" +
            "(" +
            "   attributeDefinition.attributeValue().singleValue() == null ? " +
            "   attributeDefinition.attributeValue().listValue().singleValue().stream() :" +
            "   Stream.of(attributeDefinition.attributeValue().singleValue())" +
            ")" +
            ".map(AttributeValueDefinitionMapper.INSTANCE::antlrAttributeValueDefinitionToAttributeValueDefinition)" +
            ".collect(Collectors.toList()" +
            "))",
            target = "attributeValues")
    AttributeDefinition antlrAttributeDefinitionToAttributeDefinition(
            ModelPoolParser.AttributeDefinitionContext attributeDefinition);

}
