package de.lhankedev.magicmodel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Car {

    String manufacturer;
    String model;

    Engine engine;

    Person owner;

    List<Person> previousOwners;

}
