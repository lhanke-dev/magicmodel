package de.lhankedev.modelpool.reflective;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class ModelPoolReflections {

    private static final Logger LOG = LoggerFactory.getLogger(ModelPoolReflections.class);

    public static final ClassLoader RESOURCE_CLASS_LOADER = ModelPoolReflections.class.getClassLoader();

    private final Reflections reflections;

    public ModelPoolReflections() {
        this.reflections = setupReflections();
    }

    protected Reflections setupReflections() {
        final ResourcesScanner scanner = new ResourcesScanner();

        // this seems to be necessary due to https://github.com/ronmamo/reflections/issues/273 which prevents adding the classloader directly
        final List<URL> packageUrls = Arrays.stream(RESOURCE_CLASS_LOADER.getDefinedPackages())
                .map(Package::getName)
                .flatMap(packageName -> ClasspathHelper.forPackage(packageName).stream())
                .collect(Collectors.toList());

        final ConfigurationBuilder configBuilder = new ConfigurationBuilder()
                .addUrls(packageUrls)
                .addClassLoader(ClasspathHelper.contextClassLoader())
                .addScanners(scanner);

        return new Reflections(configBuilder);
    }

    public Set<String> getResources(final Pattern pattern) {
        return reflections.getResources(pattern);
    }

    public Optional<Class<?>> clazzForName(final String fullyQualifiedName) {
        return Optional.ofNullable(ReflectionUtils.forName(fullyQualifiedName, ClasspathHelper.contextClassLoader()));
    }

    @SuppressWarnings("unchecked")
    public <T> T createObject(final Class<T> clazz) {
        final Constructor<T> noArgsConstructor = ReflectionUtils.getConstructors(clazz,
                constructor -> constructor.getParameterCount() == 0)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new StreamSupportingModelCreationException(
                                format("Failed to instantiate defined object of type %s since " +
                                                "no default (no args) constructor could be found.",
                                        clazz.getCanonicalName())));
        try {
            return noArgsConstructor.newInstance();
        } catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new StreamSupportingModelCreationException(
                    format("Failed to instantiate defined object of type %s since " +
                                    "the default constructor could not be invoked successfully.",
                            clazz.getCanonicalName()), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T injectValueIntoField(final Field field, final T targetInstance, final Object value) {
        final Optional<Method> setterMethod = ReflectionUtils.getMethods(targetInstance.getClass(),
                method -> this.isSetterOfField(method, field)).stream()
                .findFirst();
        setterMethod.ifPresentOrElse(
                setterInstance -> this.injectValuesViaSetter(setterInstance, targetInstance, value),
                () -> this.injectValuesDirectly(field, targetInstance, value));
        return targetInstance;
    }

    public Object alignOrUnwrapCollectionType(final Field targetField, final List<?> targetValues) {
        if (!Collection.class.isAssignableFrom(targetField.getType()) && targetValues.size() > 1) {
            throw new StreamSupportingModelCreationException(format(
                    "Could not set attribute collection value %s to field with name %s in class %s",
                    targetValues, targetField.getName(), targetField.getDeclaringClass().getCanonicalName()));
        } else if (Collection.class.isAssignableFrom(targetField.getType())) {
            return convertToTargetCollectionType(targetField, targetValues);
        } else {
            return targetValues.stream().findFirst().orElse(null);
        }
    }

    private Object convertToTargetCollectionType(final Field field, final List<?> attributeValues) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            if (Set.class.isAssignableFrom(field.getType())) {
                return new LinkedHashSet<>(attributeValues);
            }
            return new ArrayList<>(attributeValues);
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported terminal collection type %s found to be set to %s",
                field.getType(), attributeValues));
    }

    public Optional<Class<?>> getCollectionElementType(final Type type) {
        final Type[] typeArguments = type instanceof ParameterizedType ?
                ((ParameterizedType) type).getActualTypeArguments() :
                new Type[0];
        if (typeArguments.length < 1) {
            return Optional.empty();
        } else if (typeArguments.length == 1) {
            return clazzForName(typeArguments[0].getTypeName());
        }
        throw new StreamSupportingModelCreationException(format(
                "Unsupported count of type parameters for collection type %s",
                type));
    }

    private void injectValuesDirectly(final Field field, final Object objectInstance, final Object attributeValues) {
        try {
            field.set(objectInstance, attributeValues); //NOSONAR
        } catch (final IllegalAccessException e) {
            LOG.debug(
                    "Failed to set value {} for field {} directly for object of type {}. " +
                            "Trying to make the field accessible.",
                    attributeValues,
                    field.getName(),
                    objectInstance.getClass().getCanonicalName());
            if (field.trySetAccessible()) {
                try {
                    field.set(objectInstance, attributeValues); //NOSONAR
                } catch (final IllegalAccessException illegalAccessException) {
                    throw new StreamSupportingModelCreationException(
                            format("Failed to set value %s for field %s directly for object of type %s since " +
                                            "the field could not be accessed. " +
                                            "Please disable security manager or provide a setter for the field.",
                                    attributeValues, field.getName(), objectInstance.getClass().getCanonicalName()),
                            e);
                }
            }
        }
    }

    private void injectValuesViaSetter(final Method setterInstance, final Object objectInstance,
                                       final Object attributeValues) {
        try {
            setterInstance.invoke(objectInstance, attributeValues);
        } catch (final IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new StreamSupportingModelCreationException(format(
                    "Could not set value %s via setter %s for instance of type %s.",
                    attributeValues,
                    setterInstance,
                    objectInstance.getClass().getCanonicalName()), e);
        }
    }

    private boolean isSetterOfField(final Method method, final Field field) {
        return (method.getReturnType() == Void.class || method.getReturnType() == void.class) &&
                Modifier.isPublic(method.getModifiers()) &&
                method.getName().equals("set" + capitalize(field.getName())) &&
                method.getParameterCount() == 1 &&
                method.getParameterTypes()[0] == field.getType();
    }

    public InputStream openResource(final String classPathLocation) {
        return RESOURCE_CLASS_LOADER.getResourceAsStream(classPathLocation);
    }

    @SuppressWarnings("unchecked")
    public Optional<String> findAttributeNameWithType(final Class<?> searchIn, final Class<?> forFieldWithThisType) {
        final Set<Field> matchingFields = ReflectionUtils.getFields(searchIn, field ->
                field.getType() == forFieldWithThisType ||
                        getCollectionElementType(field.getGenericType()).filter(
                                elementType -> elementType == forFieldWithThisType).isPresent());
        if (matchingFields.size() > 1) {
            throw new StreamSupportingModelCreationException(
                    format("There is more than one field with type %s in class %s. " +
                                    "Cannot resolve reference implicitly. " +
                                    "Please use explicit identifiers.",
                            forFieldWithThisType, searchIn));
        }
        return matchingFields.stream().map(Field::getName).findFirst();
    }

}
