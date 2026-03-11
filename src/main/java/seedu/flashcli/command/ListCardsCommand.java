package seedu.flashcli.command;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;

public class ListCardsCommand implements Command {
    private String deckName;

    public ListCardsCommand(String deckName) {
        this.deckName = deckName;
    }
    @Override
    public boolean execute(DeckManager deckManager) {
        Deck deck = deckManager.getDeck(deckName);
        deck.listCards();
        return false;
    }


}
