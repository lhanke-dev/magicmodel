package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.ObjectDefinition;

import java.util.Optional;

import static java.lang.String.format;

public class ObjectCreationPhase implements ModelCreationPhase {

    @Override
    public ModelCreationContext perform(ModelCreationContext context) {
        context.getParsedModel()
                .getObjects().stream()
                .forEach(objectDefinition -> this.createObject(context, objectDefinition));
        return context;
    }

    private void createObject(ModelCreationContext context, ObjectDefinition objectDefinition) {
        final Optional<String> namespaceOpt = context.getParsedModel().getNamespace();

        final String qualifiedType = namespaceOpt
                .map(namespace -> format("%s.%s", namespace, objectDefinition.getType()))
                .orElse(objectDefinition.getType());

        final Optional<Class<?>> objectClazz = context.clazzForName(qualifiedType);
        if (objectClazz.isEmpty()) {
            throw new StreamSupportingModelCreationException(
                    format("Could not find type %s used to define object with id %s from model with name %s",
                            qualifiedType, objectDefinition.getId(), context.getParsedModel().getName()));
        }
        final Object instance = context.createObject(objectClazz.get());

        context.getMapBasedMagicModel().addObjectInstance(objectDefinition, instance);
    }
}
