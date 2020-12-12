package de.lhankedev.magicmodel.model.mapping;

import de.lhankedev.magicmodel.antlr.MagicmodelLexer;
import de.lhankedev.magicmodel.antlr.MagicmodelParser;
import de.lhankedev.magicmodel.assertion.MagicModelAssertion;
import de.lhankedev.magicmodel.assertion.ObjectDefinitionAssertion;
import de.lhankedev.magicmodel.model.MagicModel;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class MagicModelMapperTest {

    public static final String FULL_EXAMPLE_MODEL_CLASSPATH = "/de/lhankedev/magicmodel/model/mapping/FullExampleModel.mm";

    private final MagicModelMapper cut = MagicModelMapper.INSTANCE;

    @Test
    void testMagicModelMapper() throws IOException {

        try (InputStream fullExampleModelStream = MagicModelMapperTest.class.getResourceAsStream(FULL_EXAMPLE_MODEL_CLASSPATH)) {
            final String fullExampleModelFileContent = IOUtils.toString(fullExampleModelStream, StandardCharsets.UTF_8);
            final MagicmodelLexer lexer = new MagicmodelLexer(CharStreams.fromString(fullExampleModelFileContent));
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            final MagicmodelParser parser = new MagicmodelParser(tokenStream);

            MagicmodelParser.ModeldefinitionContext modeldefinition = parser.modeldefinition();
            MagicModel magicModel = cut.antlrMagicModelToMagicModel(modeldefinition);

            MagicModelAssertion exampleModelAssertion = MagicModelAssertion.assertThat(magicModel)
                    .hasName("FullExampleModel")
                    .hasNamespace("de.lhankedev.magicmodel.model")
                    .hasObjectCount(3);

            ObjectDefinitionAssertion carAssertion = exampleModelAssertion.getObjectById("testCar")
                    .isNotNull()
                    .hasId("testCar")
                    .hasType("Car");

            carAssertion.getAttributeByName("manufacturer")
                    .isNotNull()
                    .hasName("manufacturer")
                    .hasValue("TestManufacturer");
            carAssertion.getAttributeByName("model")
                    .isNotNull()
                    .hasName("model")
                    .hasValue("TestModel");
            carAssertion.getAttributeByName("engine")
                    .isNotNull()
                    .hasName("engine")
                    .hasValue("#testEngine");

            ObjectDefinitionAssertion engineAssertion = exampleModelAssertion.getObjectById("testEngine")
                    .isNotNull()
                    .hasId("testEngine")
                    .hasType("Engine");

            engineAssertion.getAttributeByName("horsePower")
                    .isNotNull()
                    .hasName("horsePower")
                    .hasValue("205");
            engineAssertion.getAttributeByName("displacement")
                    .isNotNull()
                    .hasName("displacement")
                    .hasValue("4009");

            ObjectDefinitionAssertion ownerAssertion = exampleModelAssertion.getObject(2)
                    .isNotNull()
                    .hasType("Person")
                    .hasParent("testCar.owner");

            ownerAssertion.getAttributeByName("foreName")
                    .isNotNull()
                    .hasName("foreName")
                    .hasValue("Dino");
            ownerAssertion.getAttributeByName("lastName")
                    .isNotNull()
                    .hasName("lastName")
                    .hasValue("Saur");
            ownerAssertion.getAttributeByName("age")
                    .isNotNull()
                    .hasName("age")
                    .hasValue("21");
        }

    }


}
