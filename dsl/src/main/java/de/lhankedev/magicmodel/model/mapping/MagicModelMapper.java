package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.model.MagicModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(uses = {
        ObjectDefinitionMapper.class
}, imports = {
        ObjectDefinitionMapper.class,
        Collectors.class
})
public interface MagicModelMapper {

    MagicModelMapper INSTANCE = Mappers.getMapper(MagicModelMapper.class);

    @Mappings({
            @Mapping(expression = "java(modelDefinition.modelDeclaration().modelName().getText())", target = "name"),
            @Mapping(expression = "java(modelDefinition.namespace().qualifiedName().getText())", target = "namespace"),
            @Mapping(expression = "java(modelDefinition.objectDefinition()" +
                    ".stream()" +
                    ".map(ObjectDefinitionMapper.INSTANCE::antlrObjectDefinitionToObjectDefinition)" +
                    ".collect(Collectors.toList()))",
                    target = "objects"
            )}
    )
    MagicModel antlrMagicModelToMagicModel(MagicmodelParser.ModeldefinitionContext modelDefinition);

}
