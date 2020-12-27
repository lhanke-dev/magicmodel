package de.lhankedev.magicmodel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class Car {

    String manufacturer;
    String model;

    List<Integer> inspectionYears;
    Set<Double> onlinePrices;

    Engine engine;
    Person owner;

    List<Person> previousOwners;

}
