package de.lhankedev.magicmodel.resources;

import de.lhankedev.magicmodel.reflective.MagicModelReflections;

import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassPathResourceProvider implements MagicModelResourceProvider {

    private final MagicModelReflections reflections;
    private final Pattern filePattern;

    public ClassPathResourceProvider(final String filePattern) {
        this.reflections = new MagicModelReflections();
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
