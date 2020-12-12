package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModelFactory;
import de.lhankedev.magicmodel.antlr.MagicmodelLexer;
import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.antlr.MagicmodelParser.ModeldefinitionContext;
import de.lhankedev.magicmodel.resources.ClassPathResourceProvider;
import de.lhankedev.magicmodel.resources.MagicModelResourceProvider;
import de.lhankedev.magicmodel.resources.Resource;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.stream.Collectors;

public class ReflectiveMMFactory implements MagicModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectiveMMFactory.class);

    private final MagicModelResourceProvider resourceProvider;

    public ReflectiveMMFactory() {
        this.resourceProvider = new ClassPathResourceProvider(".*\\.mm");
    }

    @Override
    public <T> T createModel(String modelName, Class<T> modelClazz) {

        return null;
    }

    private Collection<ModeldefinitionContext> getMagicModelDefinitions() {
        return this.resourceProvider.findResources()
                .stream()
                .map(this::parseMagicModel)
                .collect(Collectors.toList());
    }

    private ModeldefinitionContext parseMagicModel(Resource resource) {
        try (InputStream inStream = resource.open()) {
            final MagicmodelLexer lexer = new MagicmodelLexer(CharStreams.fromStream(inStream, StandardCharsets.UTF_8));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final MagicmodelParser parser = new MagicmodelParser(tokenStream);

            return parser.modeldefinition();

        } catch (IOException e) {
            LOG.error("Could not load magicmodel from resource " + resource, e);
        }
        return null;
    }

}
