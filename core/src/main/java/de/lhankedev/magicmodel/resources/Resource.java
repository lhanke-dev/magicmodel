package de.lhankedev.magicmodel.resources;

import java.io.InputStream;

/**
 * A magic model resource definition which is capable of providing a stream
 * with the *.mm definition as UTF-8 character bytestream.
 */
public interface Resource {

    /**
     * Opens a fresh stream providing *.mm definition in UTF-8 format.
     * <br />
     * The stream shall be closed by the caller.
     * Can be called multiple times and provides each time a fresh stream for the *.mm definition content.
     *
     * @return The bytestream of UTF-8 encoded characters building a *.mm definition file
     */
    InputStream open();

}
