package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.assertion.CarAssertion;
import de.lhankedev.magicmodel.model.Car;
import de.lhankedev.magicmodel.model.Engine;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

class MagicModelReflectionsTest {

    @FieldDefaults(level = AccessLevel.PRIVATE)
    private static class WithUntypedListField {
        List untypedList;
    }

    private final MagicModelReflections cut = new MagicModelReflections();

    @Test
    void testGetResources() {
        Assertions.assertThat(cut.getResources(Pattern.compile("testClassPathResourceProvider\\.txt")))
                .hasSize(1);
    }

    @Test
    void testClazzForName() {
        Optional<Class<?>> reflectionsClazz = cut.clazzForName(MagicModelReflections.class.getCanonicalName());
        Assertions.assertThat(reflectionsClazz)
                .isPresent()
                .containsSame(MagicModelReflections.class);
    }

    @Test
    void testCreateObject() {
        Car car = cut.createObject(Car.class);
        Assertions.assertThat(car)
                .isNotNull();
    }

    @Test
    void testInjectValueIntoField() throws NoSuchFieldException {
        Car car = new Car();
        Field field = Car.class.getDeclaredField("model");
        String value = "testVal";
        Car injectedCar = cut.injectValueIntoField(field, car, value);
        CarAssertion.assertThat(injectedCar)
                .hasModel(value);
    }

    @Test
    void testAlignOrUnwrapCollectionType() throws NoSuchFieldException {
        Field singleValueField = Car.class.getDeclaredField("model");
        String testModelVal = "testModel";
        Object alignedSingleValue = cut.alignOrUnwrapCollectionType(singleValueField, Collections.singletonList(testModelVal));
        Assertions.assertThat(alignedSingleValue)
                .isInstanceOf(String.class)
                .isEqualTo(testModelVal);

        Field listCollectionField = Car.class.getDeclaredField("inspectionYears");
        List<Integer> inspectionYearsVal = Arrays.asList(1990, 1993);
        Object alignedListValue = cut.alignOrUnwrapCollectionType(listCollectionField, inspectionYearsVal);
        Assertions.assertThat(alignedListValue)
                .isInstanceOf(List.class);
        Assertions.assertThat((List) inspectionYearsVal)
                .containsExactlyInAnyOrder(1990, 1993);

        Field setCollectionField = Car.class.getDeclaredField("onlinePrices");
        List<Double> onlinePricedValue = Arrays.asList(2391.34, 3264.95);
        Object alignedSetValue = cut.alignOrUnwrapCollectionType(setCollectionField, onlinePricedValue);
        Assertions.assertThat(alignedSetValue)
                .isInstanceOf(Set.class); // should've been converted to target collection type
        Assertions.assertThat((Set) alignedSetValue)
                .containsExactlyInAnyOrder(2391.34, 3264.95);
    }

    @Test
    void openResource() throws IOException {
        try (InputStream in = cut.openResource("de/lhankedev/magicmodel/resources/testClassPathResourceProvider.txt")) {
            String fileContent = IOUtils.toString(in, StandardCharsets.UTF_8);
            Assertions.assertThat(fileContent)
                    .isEqualTo("Found it!");
        }
    }

    @Test
    void testFindAttributeNameWithType() {
        Optional<String> attributeNameWithType = cut.findAttributeNameWithType(Car.class, Engine.class);
        Assertions.assertThat(attributeNameWithType)
                .isPresent()
                .contains("engine");
    }

    @Test
    void testGetCollectionElementType() throws NoSuchFieldException {
        Optional<Class<?>> collectionElementType = cut.getCollectionElementType(Car.class.getDeclaredField("inspectionYears").getGenericType());
        Assertions.assertThat(collectionElementType)
                .isPresent()
                .containsSame(Integer.class);

        Optional<Class<?>> untypedCollectionElementType = cut.getCollectionElementType(WithUntypedListField.class.getDeclaredField("untypedList").getGenericType());
        Assertions.assertThat(untypedCollectionElementType)
                .isEmpty();
    }

}
