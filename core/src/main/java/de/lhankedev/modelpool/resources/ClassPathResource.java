package de.lhankedev.modelpool.resources;

import de.lhankedev.modelpool.reflective.ModelPoolReflections;

import java.io.InputStream;

import lombok.Value;

@Value
public class ClassPathResource implements Resource {

    String classPathLocation;
    ModelPoolReflections reflections;

    @Override
    public InputStream open() {
        return reflections.openResource(classPathLocation);
    }

}
