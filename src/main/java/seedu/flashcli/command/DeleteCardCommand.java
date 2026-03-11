package seedu.flashcli.command;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;

public class DeleteCardCommand implements Command {
    private String deckName;
    private int cardIndex;
    public DeleteCardCommand(String deckName, int cardIndex) {
        this.deckName = deckName;
    }
    @Override
    public boolean execute(DeckManager deckManager) {
        Deck deck = deckManager.getDeck(deckName);
        deck.deleteCard(cardIndex);
        return false;
    }
}
