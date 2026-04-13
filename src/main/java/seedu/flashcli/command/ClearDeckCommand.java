package seedu.flashcli.command;

import java.util.Scanner;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.CommandFormat;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

public class ClearDeckCommand implements Command {
    private String deckName;

    public ClearDeckCommand(String deckName) {
        assert deckName != null : "Deck name should not be null";
        this.deckName = deckName;
    }

    /**
     * Creates a deck.
     *
     * @param deckManager Represents the current deckManager state.
     * @param ui Prints out message that deck was created.
     * @return false, indicating the program should not terminate after executing this object.
     * @throws FlashException Throws DECK_NOT_FOUND, indicating that the deckName input by the user does not exist.
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        try {
            Deck deck = deckManager.getDeck(deckName);
            deck.clearCards();
            ui.showDeckCleared(deckName);
        } catch (FlashException e) {
            throw new FlashException(e, CommandFormat.CLEAR_DECK);
        }
        return false;
    }
}
