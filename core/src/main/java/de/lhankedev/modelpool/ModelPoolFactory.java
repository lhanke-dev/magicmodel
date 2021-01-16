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
     * @param context {@link ModelPoolCreationContext} instance that holds runtime metadata that is relevant for model
     *                assembly
     * @return the {@link ModelPool} instance that belongs to the definition with name of the modelName param
     * @throws ModelPoolCreationException in case the model cannot be found or the name is ambiguous or anything
     *                                    else goes wrong during model initialization
     */
    ModelPool createModel(ModelPoolCreationContext context) throws ModelPoolCreationException;

    /**
     * Get a {@link ModelPoolFactory} instance. Defaults to a reflection based {@link ModelPoolFactory} implementation.
     *
     * @return a {@link ModelPoolFactory} instance
     */
    static ModelPoolFactory getInstance() {
        return new ReflectiveModelPoolFactory();
    }

}
