package de.lhankedev.magicmodel.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class TerminalTestType {

    int intValue;
    long longValue;
    boolean boolValue;
    double doubleValue;
    float floatValue;
    String stringValue;

    Collection<Integer> intCollection;
    Collection<Long> longCollection;
    Collection<Boolean> booleanCollection;
    Collection<Double> doubleCollection;
    Collection<Float> floatCollection;
    Collection<String> stringCollection;

    List<Integer> intList;
    List<Long> longList;
    List<Boolean> booleanList;
    List<Double> doubleList;
    List<Float> floatList;
    List<String> stringList;

    Set<Integer> intSet;
    Set<Long> longSet;
    Set<Boolean> booleanSet;
    Set<Double> doubleSet;
    Set<Float> floatSet;
    Set<String> stringSet;

}
