package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.model.ModelPoolDefinition;

import java.util.Optional;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {
        ObjectDefinitionMapper.class
    }, imports = {
        ObjectDefinitionMapper.class,
        ModelPoolParser.NamespaceContext.class,
        ModelPoolParser.QualifiedNameContext.class,
        Collectors.class,
        Optional.class,
        String.class
})
public interface ModelPoolDefinitionMapper {

    ModelPoolDefinitionMapper INSTANCE = Mappers.getMapper(ModelPoolDefinitionMapper.class);

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
    ModelPoolDefinition antlrModelPoolToModelPool(ModelPoolParser.ModeldefinitionContext modelDefinition);

}
