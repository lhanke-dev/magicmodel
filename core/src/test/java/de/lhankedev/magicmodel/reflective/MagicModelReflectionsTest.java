package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.assertion.CarAssertion;
import de.lhankedev.magicmodel.model.Car;
import de.lhankedev.magicmodel.model.Engine;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MagicModelReflectionsTest {

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class WithUntypedListField {
        //CHECKSTYLE:OFF
        @SuppressWarnings({"unused", "rawtypes"})
        List untypedList;
        //CHECKSTYLE:ON
    }

    private final MagicModelReflections cut = new MagicModelReflections();

    @Test
    void testGetResources() {
        Assertions.assertThat(cut.getResources(Pattern.compile("testClassPathResourceProvider\\.txt")))
                .hasSize(1);
    }

    @Test
    void testClazzForName() {
        final Optional<Class<?>> reflectionsClazz = cut.clazzForName(MagicModelReflections.class.getCanonicalName());
        Assertions.assertThat(reflectionsClazz)
                .isPresent()
                .containsSame(MagicModelReflections.class);
    }

    @Test
    void testCreateObject() {
        final Car car = cut.createObject(Car.class);
        Assertions.assertThat(car)
                .isNotNull();
    }

    @Test
    void testInjectValueIntoField() throws NoSuchFieldException {
        final Car car = new Car();
        final Field field = Car.class.getDeclaredField("model");
        final String value = "testVal";
        final Car injectedCar = cut.injectValueIntoField(field, car, value);
        CarAssertion.assertThat(injectedCar)
                .hasModel(value);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testAlignOrUnwrapCollectionType() throws NoSuchFieldException {
        final Field singleValueField = Car.class.getDeclaredField("model");
        final String testModelVal = "testModel";
        final Object alignedSingleValue = cut.alignOrUnwrapCollectionType(singleValueField,
                Collections.singletonList(testModelVal));
        Assertions.assertThat(alignedSingleValue)
                .isInstanceOf(String.class)
                .isEqualTo(testModelVal);

        final Field listCollectionField = Car.class.getDeclaredField("inspectionYears");
        final List<Integer> inspectionYearsVal = Arrays.asList(1990, 1993);
        final Object alignedListValue = cut.alignOrUnwrapCollectionType(listCollectionField, inspectionYearsVal);
        Assertions.assertThat(alignedListValue)
                .isInstanceOf(List.class);
        Assertions.assertThat(inspectionYearsVal)
                .containsExactlyInAnyOrder(1990, 1993);

        final Field setCollectionField = Car.class.getDeclaredField("onlinePrices");
        final List<Double> onlinePricedValue = Arrays.asList(2391.34, 3264.95);
        final Object alignedSetValue = cut.alignOrUnwrapCollectionType(setCollectionField, onlinePricedValue);
        Assertions.assertThat(alignedSetValue)
                .isInstanceOf(Set.class); // should've been converted to target collection type
        Assertions.assertThat((Set<Double>) alignedSetValue)
                .containsExactlyInAnyOrder(2391.34, 3264.95);
    }

    @Test
    void openResource() throws IOException {
        try (final InputStream in = cut.openResource(
                "de/lhankedev/magicmodel/resources/testClassPathResourceProvider.txt")) {
            final String fileContent = IOUtils.toString(in, StandardCharsets.UTF_8);
            Assertions.assertThat(fileContent)
                    .isEqualTo("Found it!");
        }
    }

    @Test
    void testFindAttributeNameWithType() {
        final Optional<String> attributeNameWithType = cut.findAttributeNameWithType(Car.class, Engine.class);
        Assertions.assertThat(attributeNameWithType)
                .isPresent()
                .contains("engine");
    }

    @Test
    void testGetCollectionElementType() throws NoSuchFieldException {
        final Optional<Class<?>> collectionElementType = cut.getCollectionElementType(
                Car.class.getDeclaredField("inspectionYears").getGenericType());
        Assertions.assertThat(collectionElementType)
                .isPresent()
                .containsSame(Integer.class);

        final Optional<Class<?>> untypedCollectionElementType = cut.getCollectionElementType(
                WithUntypedListField.class.getDeclaredField("untypedList").getGenericType());
        Assertions.assertThat(untypedCollectionElementType)
                .isEmpty();
    }

}
