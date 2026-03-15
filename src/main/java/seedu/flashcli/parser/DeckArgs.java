package seedu.flashcli.parser;

/**
 * Immutable object holding a deck name for commands that only require d/.
 */
public class DeckArgs {

    private final String deckName;

    public DeckArgs(String deckName) {
        this.deckName = deckName;
    }

    public String getDeckName() {
        return deckName;
    }
}
