package seedu.FlashCLI.exception;

/**
 * Represents application-specific exceptions thrown by FlashCLI.
 */
public class FlashException extends Exception {

    public FlashException(ErrorType errorType) {
        super(errorType.getMessage());
    }
}