package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;

public class CreateDeckCommand implements Command {
    private String deckName;

    public CreateDeckCommand(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public boolean execute(DeckManager deckManager) {
        deckManager.createDeck(deckName);
        return false;
    }
}
