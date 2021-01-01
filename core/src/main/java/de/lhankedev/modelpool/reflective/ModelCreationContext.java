package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.ModelPool;
import de.lhankedev.modelpool.model.ModelPoolDefinition;

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

    MapBasedModelPool mapBasedModelPool = new MapBasedModelPool();
    ModelPoolDefinition parsedModel;

    public ModelPool getModelPool() {
        return this.mapBasedModelPool;
    }

}
