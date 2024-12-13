package fr.purse.payground.exception;

/**
 * Not found exception
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructor
     *
     * @param message error message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
