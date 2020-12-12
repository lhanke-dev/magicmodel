package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.model.ObjectDefinition;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(uses = {
        AttributeDefinitionMapper.class
}, imports = {
        AttributeDefinitionMapper.class,
        Collectors.class,
        MagicmodelParser.ObjectParentContext.class,
        MagicmodelParser.ObjectIdContext.class,
        TerminalNode.class
})
public interface ObjectDefinitionMapper {

    ObjectDefinitionMapper INSTANCE = Mappers.getMapper(ObjectDefinitionMapper.class);

    @Mappings({
            @Mapping(expression = "java(java.util.Optional.ofNullable(objectDefinition.objectId())" +
                    ".map(ObjectIdContext::getText))",
                    target = "id"),
            @Mapping(expression = "java(java.util.Optional.ofNullable(objectDefinition.objectParent())" +
                    ".map(ObjectParentContext::getText))",
                    target = "parent"),
            @Mapping(expression = "java(objectDefinition.qualifiedName().getText())", target = "type"),
            @Mapping(expression = "java(objectDefinition.attributeDefinition()" +
                    ".stream()" +
                    ".map(AttributeDefinitionMapper.INSTANCE::antlrAttributeDefinitionToAttributeDefinition)" +
                    ".collect(Collectors.toList()))",
                    target = "attributes"
            )
    })
    ObjectDefinition antlrObjectDefinitionToObjectDefinition(MagicmodelParser.ObjectDefinitionContext objectDefinition);

}
