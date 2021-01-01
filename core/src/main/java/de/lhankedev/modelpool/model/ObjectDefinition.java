package de.lhankedev.modelpool.model;

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
public class ObjectDefinition {

    String type;
    Optional<String> id = Optional.empty();
    Optional<String> parent = Optional.empty();
    List<AttributeDefinition> attributes;

    public Optional<String> getParentObjectId() {
        return getParent()
                .map(parentString -> parentString.contains(".") ?
                        parentString.split("\\.")[0] :
                        parentString);
    }

    public Optional<String> getParentAttributeName() {
        return getParent()
                .filter(parentString -> parentString.contains("."))
                .map(parentString -> parentString.split("\\.")[1]);
    }

}
