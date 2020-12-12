package de.lhankedev.magicmodel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class ObjectDefinition {

    String type;
    Optional<String> id;
    Optional<String> parent;
    List<AttributeDefinition> attributes;

}
