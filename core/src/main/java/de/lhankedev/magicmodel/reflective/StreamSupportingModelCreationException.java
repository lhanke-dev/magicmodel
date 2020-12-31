package de.lhankedev.magicmodel.reflective;

public class StreamSupportingModelCreationException extends RuntimeException {

    public StreamSupportingModelCreationException(final String message) {
        super(message);
    }

    public StreamSupportingModelCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
