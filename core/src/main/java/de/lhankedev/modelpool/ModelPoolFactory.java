package de.lhankedev.modelpool;

import de.lhankedev.modelpool.exception.ModelPoolCreationException;
import de.lhankedev.modelpool.reflective.ReflectiveModelPoolFactory;

/**
 * Manages modelpool definitions.
 * <br>
 * A new model instance can be created via it's unique model name as specified in a *.mp definition file.
 */
public interface ModelPoolFactory {

    /**
     * Create a new instance of the model with the name of the modelName param.
     * The factory uses all known *.mp definition file and looks up the model definition by it's unique name.
     * <br>
     * On each call of this method a new model instance is created and returned.
     *
     * @param modelName the name of the model that a new instance shall be created for.
     *                  Needs to be specified in any of the *.mp files known to the factory.
     * @return the {@link ModelPool} instance that belongs to the definition with name of the modelName param
     * @throws ModelPoolCreationException in case the model cannot be found or the name is ambiguous or anything
     *                                    else goes wrong during model initialization
     */
    ModelPool createModel(String modelName) throws ModelPoolCreationException;

    /**
     * Get a {@link ModelPoolFactory} instance. Defaults to a reflection based {@link ModelPoolFactory} implementation.
     *
     * @return a {@link ModelPoolFactory} instance
     */
    static ModelPoolFactory getInstance() {
        return new ReflectiveModelPoolFactory();
    }

}
