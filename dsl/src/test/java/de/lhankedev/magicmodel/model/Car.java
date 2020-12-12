package de.lhankedev.magicmodel.model;

import lombok.Value;

import java.util.List;

@Value
public class Car {

    String manufacturer;
    String model;

    Engine engine;

    Person owner;

    List<Person> previousOwners;

}
