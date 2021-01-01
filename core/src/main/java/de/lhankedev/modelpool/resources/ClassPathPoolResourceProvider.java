package de.lhankedev.modelpool.resources;

import de.lhankedev.modelpool.reflective.ModelPoolReflections;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassPathPoolResourceProvider implements ModelPoolResourceProvider {

    private final ModelPoolReflections reflections;
    private final Pattern filePattern;

    public ClassPathPoolResourceProvider(final String filePattern) {
        this.reflections = new ModelPoolReflections();
        this.filePattern = Pattern.compile(filePattern);
    }

    @Override
    public Collection<Resource> findResources() {
        return reflections.getResources(filePattern)
                .stream()
                .map(resourceLocation -> new ClassPathResource(resourceLocation, reflections))
                .collect(Collectors.toList());
    }
}
