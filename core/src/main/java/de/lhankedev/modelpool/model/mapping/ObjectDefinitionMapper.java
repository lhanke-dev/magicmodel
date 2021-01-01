package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.model.ObjectDefinition;

import java.util.Optional;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        AttributeDefinitionMapper.class
    }, imports = {
        AttributeDefinitionMapper.class,
        Collectors.class,
        ModelPoolParser.ObjectParentContext.class,
        ModelPoolParser.ObjectIdContext.class,
        TerminalNode.class,
        Optional.class,
        String.class
})
public interface ObjectDefinitionMapper {

    ObjectDefinitionMapper INSTANCE = Mappers.getMapper(ObjectDefinitionMapper.class);

    @Mapping(expression = "java(Optional.ofNullable(objectDefinition.objectId())" +
            ".map(ObjectIdContext::getText)" +
            ".map(String::trim))",
            target = "id")
    @Mapping(expression = "java(Optional.ofNullable(objectDefinition.objectParent())" +
            ".map(ObjectParentContext::getText)" +
            ".map(String::trim))",
            target = "parent")
    @Mapping(expression = "java(objectDefinition.qualifiedName().getText())", target = "type")
    @Mapping(expression = "java(objectDefinition.attributeDefinition()" +
            ".stream()" +
            ".map(AttributeDefinitionMapper.INSTANCE::antlrAttributeDefinitionToAttributeDefinition)" +
            ".collect(Collectors.toList()))",
            target = "attributes"
    )
    ObjectDefinition antlrObjectDefinitionToObjectDefinition(ModelPoolParser.ObjectDefinitionContext objectDefinition);

}
