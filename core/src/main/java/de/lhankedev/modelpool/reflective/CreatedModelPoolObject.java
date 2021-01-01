package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.model.ObjectDefinition;

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

    ObjectDefinition mmObjectDefinition;
    Object createdObject;

}
