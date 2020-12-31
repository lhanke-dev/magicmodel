package de.lhankedev.magicmodel.resources;

import de.lhankedev.magicmodel.reflective.MagicModelReflections;

import java.io.InputStream;

import lombok.Value;

@Value
public class ClassPathResource implements Resource {

    String classPathLocation;
    MagicModelReflections reflections;

    @Override
    public InputStream open() {
        return reflections.openResource(classPathLocation);
    }

}
