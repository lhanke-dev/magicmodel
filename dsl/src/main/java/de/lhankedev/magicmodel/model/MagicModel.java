package de.lhankedev.magicmodel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MagicModel {

    String name;
    String namespace;
    List<ObjectDefinition> objects;

}
