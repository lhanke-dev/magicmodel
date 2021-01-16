package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.ModelPoolCreationContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.lhankedev.modelpool.model.ValueType.PLACEHOLDER;
import static java.lang.String.format;

class PlaceholderFieldCollectionPhase extends AbstractFieldCollectionPhase {

    PlaceholderFieldCollectionPhase() {
        super(PLACEHOLDER);
    }

    @Override
    protected List<Object> collectValuesForField(final ReflectiveCreationContext context, final Field field,
                                                 final List<String> values) {
        final Map<String, Object> placeholderValues = context.getClientContext().getPlaceholderValues();
        return values.stream()
                .map(placeholder ->
                        Optional.ofNullable(placeholderValues.get(placeholder))
                                .orElseThrow(
                                        () -> new StreamSupportingModelCreationException(
                                                format("Could not resolve placeholder %s from ModelPool definition " +
                                                                "with name %s. " +
                                                                "Please ensure to pass a defined value via the %s.",
                                                        placeholder, context.getParsedModel().getName(),
                                                        ModelPoolCreationContext.class.getName()))))
                .collect(Collectors.toList());
    }

}
