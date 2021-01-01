package de.lhankedev.modelpool.resources;

import java.util.Collection;

/**
 * A provider for *.mm resource files.
 */
public interface ModelPoolResourceProvider {

    /**
     * Gathers *.mm resource file locations as {@link Resource} instances.
     *
     * @return the gathered {@link Resource} instances
     */
    Collection<Resource> findResources();

}
