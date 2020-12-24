package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.model.ObjectDefinition;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@ToString
public class CreatedMagicModelObject {

    ObjectDefinition mmObjectDefinition;
    Object createdObject;

}
