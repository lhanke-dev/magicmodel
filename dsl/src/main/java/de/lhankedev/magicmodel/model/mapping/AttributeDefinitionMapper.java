package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.model.AttributeDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AttributeDefinitionMapper {

    AttributeDefinitionMapper INSTANCE = Mappers.getMapper(AttributeDefinitionMapper.class);

    @Mappings({
            @Mapping(expression = "java(attributeDefinition.attributeName().getText())", target = "attributeName"),
            @Mapping(expression = "java(attributeDefinition.attributeValue().getText())", target = "attributeValue")}
    )
    AttributeDefinition antlrAttributeDefinitionToAttributeDefinition(MagicmodelParser.AttributeDefinitionContext attributeDefinition);

}
