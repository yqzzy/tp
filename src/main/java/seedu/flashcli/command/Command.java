package seedu.flashcli.command;

import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

public interface Command {
    /**
     * Executes a command.
     *
     * @param deckManager Represents the current deckManager state.
     * @return A boolean which is true if the program should terminate after this command is executed, else false.
     * @throws FlashException If appropriate error occurs.
     */
    public boolean execute(DeckManager deckManager, Ui ui) throws FlashException;
}
