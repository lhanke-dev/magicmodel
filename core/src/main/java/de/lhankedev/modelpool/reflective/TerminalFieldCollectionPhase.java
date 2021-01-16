package de.lhankedev.modelpool.reflective;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import static de.lhankedev.modelpool.model.ValueType.TERMINAL;
import static java.lang.String.format;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class TerminalFieldCollectionPhase extends AbstractFieldCollectionPhase {

    ModelPoolReflections reflections = new ModelPoolReflections();

    TerminalFieldCollectionPhase() {
        super(TERMINAL);
    }

    @Override
    protected List<Object> collectValuesForField(final ReflectiveCreationContext context, final Field targetField,
                                                 final List<String> values) {
        final Class<?> targetValueType = Collection.class.isAssignableFrom(targetField.getType()) ?
                reflections.getCollectionElementType(targetField.getGenericType()).orElse(Object.class) :
                targetField.getType();

        return values.stream()
                .map(stringValue -> convertToTargetPrimitiveType(targetValueType, stringValue))
                .collect(Collectors.toList());
    }

    private Object convertToTargetPrimitiveType(final Class<?> attributeType, final String value) {
        if (attributeType == int.class || attributeType == Integer.class) {
            return Integer.parseInt(value);
        } else if (attributeType == long.class || attributeType == Long.class) {
            return Long.parseLong(value);
        } else if (attributeType == double.class || attributeType == Double.class) {
            return Double.parseDouble(value);
        } else if (attributeType == float.class || attributeType == Float.class) {
            return Float.parseFloat(value);
        } else if (attributeType == boolean.class || attributeType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (attributeType == String.class) {
            return value;
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported terminal type %s found to be set to %s",
                attributeType, value));
    }
}
