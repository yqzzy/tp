package seedu.flashcli.command;

import java.util.Scanner;

import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.CommandFormat;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

public class DeleteDeckCommand implements Command {
    private final String deckName;

    /**
     * Creates a DeleteDeckCommand object.
     *
     * @param deckName the name of the deck to delete; must not be null or blank
     */
    public DeleteDeckCommand(String deckName) {
        assert deckName != null : "Deck name should not be null";
        this.deckName = deckName;
    }

    /**
     * Deletes the named deck from the deck manager.
     *
     * @param deckManager represents the current deckManager state
     * @param ui prints out message that deck was deleted
     * @param in the input scanner (unused by this command)
     * @return {@code false}, indicating the program should not terminate after executing this command
     * @throws FlashException if {@code deckName} is null/blank ({@code INVALID_ARGUMENTS})
     *                        or no deck with that name exists ({@code DECK_NOT_FOUND})
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        try {
            ui.deleteConfirmationPrompt(deckName);
            String userConfirmation = in.nextLine();
            if (!userConfirmation.equals("yes")) {
                return false;
            }
            deckManager.deleteDeck(deckName);
            ui.showDeckDeleted(deckName);
        } catch (FlashException e) {
            throw new FlashException(e, CommandFormat.DELETE_DECK);
        }
        return false;
    }
}
