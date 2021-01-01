package de.lhankedev.modelpool.model;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class AttributeDefinition {

    public static final String OBJECT_REFERENCE_PREFIX = "#";

    String attributeName;
    List<String> attributeValues;

}