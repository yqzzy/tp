package seedu.flashcli.exception;


/**
 * Represents application-specific exceptions thrown by FlashCLI.
 */
public class FlashException extends Exception {
    private final ErrorType errorType;

    public FlashException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public FlashException(FlashException flashException, CommandFormat commandFormat) {
        super(flashException.getMessage() + "\n" + commandFormat.getFormat());
        this.errorType = flashException.getErrorType();
    }

    public ErrorType getErrorType() {
        return errorType;
    }
}
