package de.lhankedev.magicmodel.model;

import java.util.List;
import java.util.Optional;

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
public class MagicModelDefinition {

    String name;
    Optional<String> namespace;
    List<ObjectDefinition> objects;

}
