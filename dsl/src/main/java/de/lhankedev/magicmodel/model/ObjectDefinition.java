package de.lhankedev.magicmodel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ObjectDefinition {

    String type;
    Optional<String> id;
    Optional<String> parent;
    List<AttributeDefinition> attributes;

}
