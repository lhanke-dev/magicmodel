package de.lhankedev.modelpool.model.mapping;

import de.lhankedev.modelpool.antlr.ModelPoolLexer;
import de.lhankedev.modelpool.antlr.ModelPoolParser;
import de.lhankedev.modelpool.assertion.ModelPoolAssertion;
import de.lhankedev.modelpool.assertion.ObjectDefinitionAssertion;
import de.lhankedev.modelpool.model.ModelPoolDefinition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

class ModelPoolDefinitionMapperTest {

    public static final String FULL_EXAMPLE_MODEL_CLASSPATH =
            "/de/lhankedev/modelpool/model/mapping/FullExampleModel.mp";

    private final ModelPoolDefinitionMapper cut = ModelPoolDefinitionMapper.INSTANCE;

    @Test
    void testModelPoolMapper() throws IOException {

        try (final InputStream fullExampleModelStream = ModelPoolDefinitionMapperTest.class
                .getResourceAsStream(FULL_EXAMPLE_MODEL_CLASSPATH)) {
            final String fullExampleModelFileContent = IOUtils.toString(fullExampleModelStream, StandardCharsets.UTF_8);
            final ModelPoolLexer lexer = new ModelPoolLexer(CharStreams.fromString(fullExampleModelFileContent));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final ModelPoolParser parser = new ModelPoolParser(tokenStream);

            final ModelPoolParser.ModeldefinitionContext modeldefinition = parser.modeldefinition();
            final ModelPoolDefinition modelPoolDefinition = cut.antlrModelPoolToModelPool(modeldefinition);

            final ModelPoolAssertion exampleModelAssertion = ModelPoolAssertion.assertThat(modelPoolDefinition)
                    .hasName("FullExampleModel")
                    .hasNamespace("de.lhankedev.modelpool.model")
                    .hasObjectCount(6);

            exampleModelAssertion.getObjectById("emptyCar")
                    .isNotNull();

            final ObjectDefinitionAssertion carAssertion = exampleModelAssertion.getObjectById("testCar")
                    .isNotNull()
                    .hasId("testCar")
                    .hasType("Car");

            carAssertion.getAttributeByName("manufacturer")
                    .isNotNull()
                    .hasName("manufacturer")
                    .hasTerminalValues("TestManufacturer");
            carAssertion.getAttributeByName("model")
                    .isNotNull()
                    .hasName("model")
                    .hasPlaceholderValues("testModel");
            carAssertion.getAttributeByName("engine")
                    .isNotNull()
                    .hasName("engine")
                    .hasReferenceValues("testEngine");
            carAssertion.getAttributeByName("previousOwners")
                    .isNotNull()
                    .hasName("previousOwners")
                    .hasReferenceValues("dinoSaur", "elePhant");

            final ObjectDefinitionAssertion engineAssertion = exampleModelAssertion.getObjectById("testEngine")
                    .isNotNull()
                    .hasId("testEngine")
                    .hasType("Engine");

            engineAssertion.getAttributeByName("horsePower")
                    .isNotNull()
                    .hasName("horsePower")
                    .hasTerminalValues("205");
            engineAssertion.getAttributeByName("displacement")
                    .isNotNull()
                    .hasName("displacement")
                    .hasTerminalValues("4009");

            final ObjectDefinitionAssertion ownerAssertion = exampleModelAssertion.getObject(3)
                    .isNotNull()
                    .hasType("Person")
                    .hasParent("testCar.owner");

            ownerAssertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasTerminalValues("Lion");
            ownerAssertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasTerminalValues("King");
            ownerAssertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasTerminalValues("99");

            final ObjectDefinitionAssertion previousOwner1Assertion = exampleModelAssertion.getObjectById("elePhant")
                    .isNotNull()
                    .hasType("Person");

            previousOwner1Assertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasTerminalValues("Ele");
            previousOwner1Assertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasTerminalValues("Phant");
            previousOwner1Assertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasTerminalValues("28");

            final ObjectDefinitionAssertion previousOwner2Assertion = exampleModelAssertion.getObjectById("dinoSaur")
                    .isNotNull()
                    .hasType("Person");

            previousOwner2Assertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasTerminalValues("Dino");
            previousOwner2Assertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasTerminalValues("Saur");
            previousOwner2Assertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasTerminalValues("21");
        }

    }


}
