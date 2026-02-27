package seedu.FlashCLI.exception;

public enum ErrorType {

    NULL_INPUT("Please type in a command or use \"help\" to see the list of commands."),
    INVALID_COMMAND("Invalid command format. Use \"help\" to see the list of all commands."),
    INVALID_ARGUMENTS("Invalid arguments format. Check your prefixes and try again."),
    ARGUMENT_MISSING("One or more required arguments are missing."),
    UNEXPECTED_ARGUMENTS("Too many arguments are provided."),
    MISSING_DECK("Deck name is required. Use prefix d/."),
    MISSING_QUESTION("Question is required. Use prefix q/."),
    MISSING_ANSWER("Answer is required. Use prefix a/."),
    MISSING_INDEX("Card index is required. Use prefix i/."),
    DUPLICATE_PREFIX("Duplicate prefixes detected. Each prefix should appear only once."),
    INVALID_ADD_CARD("Invalid format for addCard.\nUse: addCard d/<deck> q/<question> a/<answer>"),
    INVALID_DELETE_CARD("Invalid format for deleteCard.\nUse: deleteCard d/<deck> i/<index>");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}