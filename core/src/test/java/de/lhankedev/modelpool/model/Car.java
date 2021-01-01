package de.lhankedev.modelpool.model;

import java.util.List;
import java.util.Set;

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
public class Car {

    String manufacturer;
    String model;

    List<Integer> inspectionYears;
    Set<Double> onlinePrices;

    Engine engine;
    Person owner;

    List<Person> previousOwners;

}
