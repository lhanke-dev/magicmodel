package de.lhankedev.modelpool;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(level = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
@ToString
public class ModelPoolCreationContext {

    String modelname;

    @Singular
    Map<String, Object> placeholderValues;

}
