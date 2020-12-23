package de.lhankedev.magicmodel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

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
