package de.lhankedev.modelpool.resources;

import java.io.InputStream;

/**
 * A modelpool definition resource which is capable of providing a stream
 * with the *.mp definition as UTF-8 character bytestream.
 */
public interface Resource {

    /**
     * Opens a fresh stream providing *.mp definition in UTF-8 format.
     * <br />
     * The stream shall be closed by the caller.
     * Can be called multiple times and provides each time a fresh stream for the *.mp definition content.
     *
     * @return The bytestream of UTF-8 encoded characters building a *.mp definition file
     */
    InputStream open();

}
