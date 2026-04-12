package seedu.flashcli.exception;

public enum ErrorType {

    NULL_INPUT("Please type in a command or use \"help\" to see the list of commands."),
    INVALID_COMMAND("Invalid command. Use \"help\" to see the list of all commands."),
    INVALID_ARGUMENTS("Invalid arguments format. Check your prefixes and try again."),
    ARGUMENT_MISSING("One or more required arguments are missing."),
    UNEXPECTED_ARGUMENTS("Too many arguments are provided."),
    MISSING_DECK("Deck name is required. Use prefix d/."),
    MISSING_QUESTION("Question is required. Use prefix q/."),
    MISSING_ANSWER("Answer is required. Use prefix a/."),
    MISSING_INDEX("Card index is required. Use prefix i/."),
    DUPLICATE_PREFIX("Duplicate prefixes detected. Each prefix should appear only once."),
    INVALID_ADD_CARD("Invalid format for addCard.\nUse: addCard d/<deck> q/<question> a/<answer>"),
    INVALID_DELETE_CARD("Invalid format for deleteCard.\nUse: deleteCard d/<deck> i/<index>"),
    INVALID_INDEX("Invalid index. Enter a positive integer within the range shown by 'listCards'."),
    DECK_NOT_FOUND("Deck not found. Use listDecks to see available decks."),
    CARD_NOT_FOUND("Card index out of bounds. Use listCards to see valid indices."),
    SESSION_ALREADY_IN_PROGRESS("A study session is already running. Please finish it before starting a new one."),
    NO_ACTIVE_SESSION("No study session is currently active. Use the 'study' command first."),
    DUPLICATE_NAME("A deck with this name already exists. Please choose a unique name."),
    INVALID_CONFIDENCE("Invalid confidence level. Please enter an integer between 1 and 5. "),
    INVALID_EDIT("Invalid format for editCard. \nUse: editCard d/<deck> i/<index> q/<new question> a/<new answer>");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
