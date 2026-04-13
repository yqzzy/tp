package seedu.flashcli.exception;

/**
 * Enum mapping to Strings to show user the correct format of their intended command.
 */
public enum CommandFormat {

    ADD_CARD("Use: addCard d/<deck> q/<question> a/<answer>"),
    DELETE_CARD("Use: deleteCard d/<deck> i/<index>"),
    EDIT_CARD("Use: editCard d/<deck> i/<index> q/<new question> a/<new answer>"),
    CREATE_DECK("Use: createDeck d/<deck>"),
    DELETE_DECK("Use: deleteDeck d/<deck>"),
    CLEAR_DECK("Use: clearDeck d/<deck>"),
    LIST_CARDS("Use: listCards d/<deck>"),
    LIST_DECKS("Use: listDecks"),
    STUDY("Use: study d/<deck>"),
    HELP("Use: help"),
    EXIT("Use: exit");

    private final String format;

    CommandFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
