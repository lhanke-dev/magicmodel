package de.lhankedev.magicmodel;

import java.util.Optional;

/**
 * Holds object instances that were defined in a *.mm model defintion file and instantiated via
 * {@link MagicModelFactory#createModel(String)}.
 * <br />
 * Allows to access the created instances in convenient ways.
 */
public interface MagicModel {

    /**
     * Retrieve an object instance from a magic model with the unique id that has been specified
     * in the magic models *.mm definition file.
     *
     * @param id            The id of the object definition as specified in the magic model *.mm definition file
     * @param expectedClass The class of the object instance that shall be retrieved
     * @param <T>           The type of the object instance to retrieve
     * @return An {@link Optional} holding the actual instance or {@link Optional#empty()} in case the id could
     *         not be found in the magic models *.mm definition file.
     */
    <T> Optional<T> getObjectById(String id, Class<T> expectedClass);

}
