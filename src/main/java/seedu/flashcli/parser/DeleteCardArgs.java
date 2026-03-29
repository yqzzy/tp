package seedu.flashcli.parser;

/**
 * Immutable object grouping the two arguments needed for the deleteCard command.
 */
public class DeleteCardArgs {

    private final String deckName;
    private final int cardIndex;

    /**
     * Constructs an DeleteCardArgs with the two deleteCard fields.
     */
    public DeleteCardArgs(String deckName, int cardIndex) {
        this.deckName  = deckName;
        this.cardIndex = cardIndex;
    }

    public String getDeckName() {
        return deckName;
    }

    public int getCardIndex() {
        return cardIndex;
    }
}
