package de.lhankedev.modelpool.reflective;

import de.lhankedev.modelpool.ModelPool;
import de.lhankedev.modelpool.ModelPoolCreationContext;
import de.lhankedev.modelpool.ModelPoolFactory;
import de.lhankedev.modelpool.antlr.ModelPoolLexer;
import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.antlr.ModelPoolParser.ModeldefinitionContext;
import de.lhankedev.modelpool.exception.ModelPoolCreationException;
import de.lhankedev.modelpool.model.ModelPoolDefinition;
import de.lhankedev.modelpool.model.mapping.ModelPoolDefinitionMapper;
import de.lhankedev.modelpool.resources.ClassPathPoolResourceProvider;
import de.lhankedev.modelpool.resources.ModelPoolResourceProvider;
import de.lhankedev.modelpool.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class ReflectiveModelPoolFactory implements ModelPoolFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectiveModelPoolFactory.class);

    public static final String DEFAULT_MODELPOOL_DEFINITION_FILE_SUFFIX = ".mp";

    private final ModelPoolResourceProvider resourceProvider;
    private final ModelPoolDefinitionMapper mapper;

    private final List<ModelCreationPhase> phases;

    public ReflectiveModelPoolFactory() {
        this.resourceProvider = new ClassPathPoolResourceProvider(".*\\" + DEFAULT_MODELPOOL_DEFINITION_FILE_SUFFIX);
        this.mapper = ModelPoolDefinitionMapper.INSTANCE;
        this.phases = setupPhases();
    }

    protected List<ModelCreationPhase> setupPhases() {
        return Arrays.asList(
                // order is relevant
                new ObjectCreationPhase(),
                new TerminalFieldCollectionPhase(),
                new PlaceholderFieldCollectionPhase(),
                new ReferenceFieldCollectionPhase(),
                new FieldInjectionPhase()
        );
    }

    @Override
    public ModelPool createModel(final ModelPoolCreationContext context) throws ModelPoolCreationException {
        final Collection<ModelPoolDefinition> modelDefinitionPoolDefinitions = getModelPoolDefinitions();
        final List<ModelPoolDefinition> modelDefinitions = modelDefinitionPoolDefinitions.stream()
                .filter(modelPool -> modelPool.getName().equals(context.getModelname()))
                .collect(Collectors.toList());

        if (modelDefinitions.isEmpty()) {
            throw new ModelPoolCreationException(
                    format("Could not find model definition with name %s", context.getModelname()));
        } else if (modelDefinitions.size() > 1) {
            throw new ModelPoolCreationException(format("Found ambiguous model definitions with name %s on the " +
                    "classpath. Please make model names unique.", context.getModelname()));
        }

        final ModelPoolDefinition modelPoolDefinition = modelDefinitions.get(0);
        final ReflectiveCreationContext internalCreationContext = new ReflectiveCreationContext(modelPoolDefinition,
                context);

        try {
            phases.forEach(phase -> phase.perform(internalCreationContext));
        } catch (final StreamSupportingModelCreationException e) {
            throw new ModelPoolCreationException(format("Failed to create model with name %s", context.getModelname()),
                    e);
        }

        return internalCreationContext.getModelPool();
    }

    private Collection<ModelPoolDefinition> getModelPoolDefinitions() {
        return this.resourceProvider.findResources()
                .stream()
                .map(this::parseModelPool)
                .map(this.mapper::antlrModelPoolToModelPool)
                .collect(Collectors.toList());
    }

    private ModeldefinitionContext parseModelPool(final Resource resource) {
        LOG.debug("Parsing resource: {}", resource);
        try (final InputStream inStream = resource.open()) {
            final ModelPoolLexer lexer = new ModelPoolLexer(CharStreams.fromStream(inStream, StandardCharsets.UTF_8));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final ModelPoolParser parser = new ModelPoolParser(tokenStream);
            return parser.modeldefinition();
        } catch (final IOException e) {
            LOG.error("Could not load modelpool from resource " + resource, e);
        }
        return null;
    }

}
