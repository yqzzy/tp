package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;

public class ListDecksCommand implements Command {
    @Override
    public boolean execute(DeckManager deckManager) {
        deckManager.listDecks();
        return false;
    }
}
