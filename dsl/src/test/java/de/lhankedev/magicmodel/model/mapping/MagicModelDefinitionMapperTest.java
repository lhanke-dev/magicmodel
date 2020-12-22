package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelLexer;
import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.assertion.MagicModelAssertion;
import de.lhankedev.magicmodel.assertion.ObjectDefinitionAssertion;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MagicModelDefinitionMapperTest {

    public static final String FULL_EXAMPLE_MODEL_CLASSPATH = "/de/lhankedev/magicmodel/model/mapping/FullExampleModel.mm";

    private final MagicModelDefinitionMapper cut = MagicModelDefinitionMapper.INSTANCE;

    @Test
    void testMagicModelMapper() throws IOException {

        try (InputStream fullExampleModelStream = MagicModelDefinitionMapperTest.class.getResourceAsStream(FULL_EXAMPLE_MODEL_CLASSPATH)) {
            final String fullExampleModelFileContent = IOUtils.toString(fullExampleModelStream, StandardCharsets.UTF_8);
            final MagicmodelLexer lexer = new MagicmodelLexer(CharStreams.fromString(fullExampleModelFileContent));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final MagicmodelParser parser = new MagicmodelParser(tokenStream);

            final MagicmodelParser.ModeldefinitionContext modeldefinition = parser.modeldefinition();
            final MagicModelDefinition magicModelDefinition = cut.antlrMagicModelToMagicModel(modeldefinition);

            final MagicModelAssertion exampleModelAssertion = MagicModelAssertion.assertThat(magicModelDefinition)
                    .hasName("FullExampleModel")
                    .hasNamespace("de.lhankedev.magicmodel.model")
                    .hasObjectCount(5);

            final ObjectDefinitionAssertion carAssertion = exampleModelAssertion.getObjectById("testCar")
                    .isNotNull()
                    .hasId("testCar")
                    .hasType("Car");

            carAssertion.getAttributeByName("manufacturer")
                    .isNotNull()
                    .hasName("manufacturer")
                    .hasValues("TestManufacturer");
            carAssertion.getAttributeByName("model")
                    .isNotNull()
                    .hasName("model")
                    .hasValues("TestModel");
            carAssertion.getAttributeByName("engine")
                    .isNotNull()
                    .hasName("engine")
                    .hasValues("#testEngine");
            carAssertion.getAttributeByName("previousOwners")
                    .isNotNull()
                    .hasName("previousOwners")
                    .hasValues("#dinoSaur", "#elePhant");

            final ObjectDefinitionAssertion engineAssertion = exampleModelAssertion.getObjectById("testEngine")
                    .isNotNull()
                    .hasId("testEngine")
                    .hasType("Engine");

            engineAssertion.getAttributeByName("horsePower")
                    .isNotNull()
                    .hasName("horsePower")
                    .hasValues("205");
            engineAssertion.getAttributeByName("displacement")
                    .isNotNull()
                    .hasName("displacement")
                    .hasValues("4009");

            final ObjectDefinitionAssertion ownerAssertion = exampleModelAssertion.getObject(2)
                    .isNotNull()
                    .hasType("Person")
                    .hasParent("testCar.owner");

            ownerAssertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasValues("Lion");
            ownerAssertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasValues("King");
            ownerAssertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasValues("99");

            final ObjectDefinitionAssertion previousOwner1Assertion = exampleModelAssertion.getObjectById("elePhant")
                    .isNotNull()
                    .hasType("Person");

            previousOwner1Assertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasValues("Ele");
            previousOwner1Assertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasValues("Phant");
            previousOwner1Assertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasValues("28");

            final ObjectDefinitionAssertion previousOwner2Assertion = exampleModelAssertion.getObjectById("dinoSaur")
                    .isNotNull()
                    .hasType("Person");

            previousOwner2Assertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasValues("Dino");
            previousOwner2Assertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasValues("Saur");
            previousOwner2Assertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasValues("21");
        }

    }


}
