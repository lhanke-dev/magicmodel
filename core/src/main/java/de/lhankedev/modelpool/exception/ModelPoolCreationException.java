package de.lhankedev.modelpool.exception;

public class ModelPoolCreationException extends Exception {

    public ModelPoolCreationException(final String message) {
        super(message);
    }

    public ModelPoolCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
