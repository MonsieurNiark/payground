package fr.purse.payground.exception;

/**
 * Forbidden exception
 */
public class ForbiddenException extends RuntimeException {

    /**
     * Constructor
     *
     * @param message error message
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
