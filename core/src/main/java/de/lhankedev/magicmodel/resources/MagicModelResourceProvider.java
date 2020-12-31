package de.lhankedev.magicmodel.resources;

import java.util.Collection;

/**
 * A provider for *.mm resource files.
 */
public interface MagicModelResourceProvider {

    /**
     * Gathers *.mm resource file locations as {@link Resource} instances.
     *
     * @return the gathered {@link Resource} instances
     */
    Collection<Resource> findResources();

}
