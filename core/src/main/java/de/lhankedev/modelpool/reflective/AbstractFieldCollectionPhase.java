package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.model.AttributeDefinition;
import de.lhankedev.modelpool.model.AttributeValueDefinition;
import de.lhankedev.modelpool.model.ObjectDefinition;
import de.lhankedev.modelpool.model.ValueType;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static java.lang.String.format;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
abstract class AbstractFieldCollectionPhase implements ModelCreationPhase {

    ValueType type;

    @Override
    public ReflectiveCreationContext perform(final ReflectiveCreationContext context) {
        context.getMapBasedModelPool().getCreatedObjects()
                .forEach(object -> this.collectFieldsForObject(context, object));
        return context;
    }

    protected void collectFieldsForObject(final ReflectiveCreationContext context,
                                          final CreatedModelPoolObject object) {
        final ObjectDefinition definition = object.getModelPoolObjectDefinition();
        definition.getAttributes().stream()
                .filter(attribute -> this.isAttributeOfType(attribute, type))
                .forEach(terminalAttribute -> collectAttribute(context, object, terminalAttribute));
    }


    private boolean isAttributeOfType(final AttributeDefinition attributeDefinition, final ValueType type) {
        return attributeDefinition.getAttributeValues()
                .stream()
                .anyMatch(value -> value.getType() == type);
    }

    protected void collectAttribute(final ReflectiveCreationContext context, final CreatedModelPoolObject object,
                                    final AttributeDefinition attribute) {
        try {
            final Field field = object.getCreatedObject().getClass().getDeclaredField(
                    attribute.getAttributeName());
            final List<String> valueDefinitions = attribute.getAttributeValues().stream().map(
                    AttributeValueDefinition::getValue).collect(Collectors.toList());
            this.collectValuesForField(context, field, valueDefinitions)
                    .forEach(value -> object.addResolvedFieldDefinition(field, value));
        } catch (final NoSuchFieldException e) {
            throw new StreamSupportingModelCreationException(
                    format("Could not inject field value for field %s into object of type %s",
                            attribute.getAttributeName(), object.getCreatedObject().getClass()), e);
        }
    }

    protected abstract List<Object> collectValuesForField(ReflectiveCreationContext context, Field field,
                                                          List<String> values);

}
