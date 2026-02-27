package seedu.FlashCLI.parser;

/**
 * Represents application-specific exceptions thrown by FlashCLI.
 */
public class FlashException extends Exception {

    public FlashException(String errorType) {
        super(getErrorMessage(errorType));
    }

    /**
     * Maps an error type key to a user-facing error message.
     *
     * @param errorType the error type key to look up
     * @return a user-friendly error message string
     */
    private static String getErrorMessage(String errorType) {
        switch (errorType) {
        case "null input":
            return "Please type in a command or use \"help\" to see the list of commands.";
        case "invalid command":
            return "Invalid command format. Use \"help\" to see the list of all commands.";
        case "invalid arguments":
            return "Invalid arguments format. Check your prefixes and try again.";
        case "argument missing":
            return "One or more required arguments are missing.";
        case "missing d/":
            return "Deck name is required. Use prefix d/.";
        case "missing q/":
            return "Question is required. Use prefix q/.";
        case "missing a/":
            return "Answer is required. Use prefix a/.";
        case "missing i/":
            return "Card index is required. Use prefix i/.";
        case "duplicate d/":
        case "duplicate q/":
        case "duplicate a/":
        case "duplicate i/":
            return "Duplicate prefixes detected. Each prefix should appear only once.";
        case "addCard":
            return "Invalid format for addCard.\nUse: addCard d/<deck> q/<question> a/<answer>";
        case "deleteCard":
            return "Invalid format for deleteCard.\nUse: deleteCard d/<deck> i/<index>";
        default:
            return "Invalid command format. Use \"help\" to see the list of all commands.";
        }
    }
}