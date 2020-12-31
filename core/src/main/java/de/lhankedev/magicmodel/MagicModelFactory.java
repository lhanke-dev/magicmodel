package de.lhankedev.magicmodel;

import de.lhankedev.magicmodel.exception.MagicModelCreationException;

/**
 * Manages magic model definitions.
 * <br />
 * A new model instance can be created via it's unique model name as specified in a *.mm definition file.
 */
public interface MagicModelFactory {

    /**
     * Create a new instance of the model with the name of the modelName param.
     * The factory uses all known *.mm definition file and looks up the model definition by it's unique name.
     * <br />
     * On each call of this method a new model instance is created and returned.
     *
     * @param modelName the name of the model that a new instance shall be created for.
     *                  Needs to be specified in any of the *.mm files known to the factory.
     * @return the {@link MagicModel} instance that belongs to the definition with name of the modelName param
     * @throws MagicModelCreationException in case the model cannot be found or the name is ambiguous or anything
     *                                     else goes wrong during model initialization
     */
    MagicModel createModel(String modelName) throws MagicModelCreationException;

}
