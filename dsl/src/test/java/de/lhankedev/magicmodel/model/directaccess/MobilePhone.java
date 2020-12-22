package de.lhankedev.magicmodel.model.directaccess;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE)
@ToString
public class MobilePhone {

    String manufacturer;
    int weight;
    double screenSize;

}
