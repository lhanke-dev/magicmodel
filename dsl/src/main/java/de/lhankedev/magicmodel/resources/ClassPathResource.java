package de.lhankedev.magicmodel.resources;

import lombok.Value;

import java.io.InputStream;

@Value
public class ClassPathResource implements Resource {

    String classPathLocation;
    ClassLoader classLoader;

    @Override
    public InputStream open() {
        return classLoader.getResourceAsStream(classPathLocation);
    }

}
