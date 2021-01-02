package de.lhankedev.modelpool;

import java.util.Optional;

/**
 * Holds object instances that were defined in a *.mp model definition file and instantiated via
 * {@link ModelPoolFactory#createModel(String)}.
 * <br>
 * Allows to access the created instances in convenient ways.
 */
public interface ModelPool {

    /**
     * Retrieve an object instance from a modelpool with the unique id that has been specified
     * in the modelpools *.mp definition file.
     *
     * @param id            The id of the object definition as specified in the modelpool *.mp definition file
     * @param expectedClass The class of the object instance that shall be retrieved
     * @param <T>           The type of the object instance to retrieve
     * @return An {@link Optional} holding the actual instance or {@link Optional#empty()} in case the id could
     *         not be found in the modelpools *.mp definition file.
     */
    <T> Optional<T> getObjectById(String id, Class<T> expectedClass);

}
