package de.lhankedev.magicmodel.model.directaccess;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class MobilePhone {

    String manufacturer;
    int weight;
    double screenSize;

}
