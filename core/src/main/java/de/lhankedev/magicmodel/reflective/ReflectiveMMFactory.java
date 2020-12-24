package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.MagicModelFactory;
import de.lhankedev.magicmodel.antlr.MagicmodelLexer;
import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.antlr.MagicmodelParser.ModeldefinitionContext;
import de.lhankedev.magicmodel.exception.MagicModelCreationException;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import de.lhankedev.magicmodel.model.mapping.MagicModelDefinitionMapper;
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class ReflectiveMMFactory implements MagicModelFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ReflectiveMMFactory.class);

    private final MagicModelResourceProvider resourceProvider;
    private final MagicModelDefinitionMapper mapper;

    private final List<ModelCreationPhase> phases;

    public ReflectiveMMFactory() {
        this.resourceProvider = new ClassPathResourceProvider(".*\\.mm");
        this.mapper = MagicModelDefinitionMapper.INSTANCE;
        this.phases = setupPhases();
    }

    protected List<ModelCreationPhase> setupPhases() {
        return Arrays.asList(
                new ObjectCreationPhase(),
                new PrimitiveFieldInstantiationPhase(),
                new ObjectLinkingPhase()
        );
    }

    @Override
    public MagicModel createModel(String modelName) throws MagicModelCreationException {

        Collection<MagicModelDefinition> magicModelDefinitionDefinitions = getMagicModelDefinitions();
        List<MagicModelDefinition> modelDefinitions = magicModelDefinitionDefinitions.stream()
                .filter(magicModel -> magicModel.getName().equals(modelName))
                .collect(Collectors.toList());

        if (modelDefinitions.isEmpty()) {
            throw new MagicModelCreationException(format("Could not find model definition with name %s", modelName));
        } else if (modelDefinitions.size() > 1) {
            throw new MagicModelCreationException(format("Found ambiguous model definitions with name %s on the classpath. Please make model names unique.", modelName));
        }

        final MagicModelDefinition magicModelDefinition = modelDefinitions.get(0);
        final ModelCreationContext context = new ModelCreationContext(magicModelDefinition);

        try {
            phases.stream()
                    .forEach(phase -> phase.perform(context));
        } catch (StreamSupportingModelCreationException e) {
            throw new MagicModelCreationException(format("Failed to create model with name %s", modelName), e);
        }

        return context.getMagicModel();
    }

    private Collection<MagicModelDefinition> getMagicModelDefinitions() {
        return this.resourceProvider.findResources()
                .stream()
                .map(this::parseMagicModel)
                .map(this.mapper::antlrMagicModelToMagicModel)
                .collect(Collectors.toList());
    }

    private ModeldefinitionContext parseMagicModel(Resource resource) {
        LOG.debug("Parsing resource: {}", resource);
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
