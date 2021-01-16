package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.model.ObjectDefinition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class CreatedModelPoolObject {

    ObjectDefinition modelPoolObjectDefinition;
    Object createdObject;

    Map<Field, ArrayList<Object>> resolvedFields = new LinkedHashMap<>();

    public void addResolvedFieldDefinition(final Field field, final Object value) {
        resolvedFields.computeIfAbsent(field, key -> new ArrayList<>())
                .add(value);
    }
}
