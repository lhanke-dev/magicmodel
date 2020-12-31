package de.lhankedev.magicmodel.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClassPathResourceProviderTest {

    public static final String EXPECTED_CLASSPATH_RESOURCE_CONTENT = "Found it!";
    private final ClassPathResourceProvider cut = new ClassPathResourceProvider("testClassPath.*\\.txt");

    @Test
    void testClassPathResourceScanning() throws IOException {
        final Collection<Resource> resources = cut.findResources();
        assertThat(resources)
                .hasSize(1);
        final Optional<Resource> foundResourceOpt = resources.stream().findFirst();
        assertThat(foundResourceOpt)
                .isPresent();
        final Resource foundResource = foundResourceOpt.get();
        try (final InputStream resourceStream = foundResource.open()) {
            final String content = IOUtils.toString(resourceStream, StandardCharsets.UTF_8);
            assertThat(content)
                    .isEqualTo(EXPECTED_CLASSPATH_RESOURCE_CONTENT);
        }
    }

}
