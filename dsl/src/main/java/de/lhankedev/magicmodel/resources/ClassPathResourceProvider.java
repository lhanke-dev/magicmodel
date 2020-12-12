package de.lhankedev.magicmodel.resources;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassPathResourceProvider implements MagicModelResourceProvider {

    private static final ClassLoader RESOURCE_CLASS_LOADER = ClassPathResourceProvider.class.getClassLoader();

    private final Reflections reflections;
    private final Pattern filePattern;

    public ClassPathResourceProvider(final String filePattern) {
        final ResourcesScanner scanner = new ResourcesScanner();

        // this seems to be necessary due to https://github.com/ronmamo/reflections/issues/273 which prevents adding the classloader directly
        List<URL> packageURLs = Arrays.stream(RESOURCE_CLASS_LOADER.getDefinedPackages())
                .map(Package::getName)
                .flatMap(packageName -> ClasspathHelper.forPackage(packageName).stream())
                .collect(Collectors.toList());

        final ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                .addUrls(packageURLs)
                .addScanners(scanner);
        this.reflections = new Reflections(configBuilder);
        this.filePattern = Pattern.compile(filePattern);
    }

    @Override
    public Collection<Resource> findResources() {
        return reflections.getResources(filePattern)
                .stream()
                .map(resourceLocation -> new ClassPathResource(resourceLocation, RESOURCE_CLASS_LOADER))
                .collect(Collectors.toList());
    }
}
