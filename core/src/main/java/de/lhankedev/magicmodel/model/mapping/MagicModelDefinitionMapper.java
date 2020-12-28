package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(uses = {
        ObjectDefinitionMapper.class
}, imports = {
        ObjectDefinitionMapper.class,
        MagicmodelParser.NamespaceContext.class,
        MagicmodelParser.QualifiedNameContext.class,
        Collectors.class,
        Optional.class,
        String.class
})
public interface MagicModelDefinitionMapper {

    MagicModelDefinitionMapper INSTANCE = Mappers.getMapper(MagicModelDefinitionMapper.class);

    @Mapping(expression = "java(modelDefinition.modelDeclaration().modelName().getText().trim())", target = "name")
    @Mapping(expression = "java(Optional.ofNullable(modelDefinition.namespace())" +
            ".map(NamespaceContext::qualifiedName)" +
            ".map(QualifiedNameContext::getText)" +
            ".map(String::trim))",
            target = "namespace")
    @Mapping(expression = "java(modelDefinition.objectDefinition()" +
            ".stream()" +
            ".map(ObjectDefinitionMapper.INSTANCE::antlrObjectDefinitionToObjectDefinition)" +
            ".collect(Collectors.toList()))",
            target = "objects"
    )
    MagicModelDefinition antlrMagicModelToMagicModel(MagicmodelParser.ModeldefinitionContext modelDefinition);

}
