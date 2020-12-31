package de.lhankedev.magicmodel.exception;

public class MagicModelCreationException extends Exception {

    public MagicModelCreationException(final String message) {
        super(message);
    }

    public MagicModelCreationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
