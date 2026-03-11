package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;

public interface Command {
    public boolean execute(DeckManager deckManager);
}
