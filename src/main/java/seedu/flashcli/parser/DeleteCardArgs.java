package seedu.flashcli.parser;

public class DeleteCardArgs {

    private final String deckName;
    private final int cardIndex;

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
