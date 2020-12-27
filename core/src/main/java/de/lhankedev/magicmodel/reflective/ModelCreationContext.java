package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Getter(PACKAGE)
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
@ToString
public class ModelCreationContext {

    MapBasedMagicModel mapBasedMagicModel = new MapBasedMagicModel();
    MagicModelDefinition parsedModel;

    public MagicModel getMagicModel() {
        return this.mapBasedMagicModel;
    }

}
