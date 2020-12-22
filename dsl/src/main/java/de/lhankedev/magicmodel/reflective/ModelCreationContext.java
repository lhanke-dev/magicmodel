package de.lhankedev.magicmodel.reflective;

import de.lhankedev.magicmodel.MagicModel;
import de.lhankedev.magicmodel.model.MagicModelDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Getter(PACKAGE)
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
@ToString
public class ModelCreationContext<T> {

    MapBasedMagicModel mapBasedMagicModel = new MapBasedMagicModel();

    MagicModelDefinition parsedModel;
    Reflections reflections;

    public MagicModel getMagicModel() {
        return this.mapBasedMagicModel;
    }

    Optional<Class<?>> clazzForName(final String fullyQualifiedName) {
        return Optional.ofNullable(ReflectionUtils.forName(fullyQualifiedName, ClasspathHelper.contextClassLoader()));
    }

    <T> T createObject(final Class<T> clazz) {
        Set<Constructor> constructors = ReflectionUtils.getConstructors(clazz, constructor -> constructor.getParameterCount() == 0);
        if (constructors.isEmpty()) {
            throw new StreamSupportingModelCreationException(format("Failed to instantiate defined object of type %s since no default (no args) constructor could be found.", clazz.getCanonicalName()));
        }
        Constructor constructor = constructors.stream().findFirst().get();
        try {
            return (T) constructor.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new StreamSupportingModelCreationException(format("Failed to instantiate defined object of type %s since the default constructor could not be invoked successfully.", clazz.getCanonicalName()), e);
        }
    }
}
