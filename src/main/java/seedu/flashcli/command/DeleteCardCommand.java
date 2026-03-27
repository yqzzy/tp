package seedu.flashcli.command;

import java.util.Scanner;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.DeleteCardArgs;
import seedu.flashcli.ui.Ui;

public class DeleteCardCommand implements Command {
    private final String deckName;
    private final int cardIndex;

    /**
     * Creates a DeleteCardCommand object
     *
     * @param deleteCardArgs Dataclass contains all attributes of an DeleteCardCommand object.
     */
    public DeleteCardCommand(DeleteCardArgs deleteCardArgs) {
        assert deleteCardArgs != null : "deleteCardArgs should not be null";
        assert deleteCardArgs.getDeckName() != null : "deleteCardArgs.deckName should not be null";

        this.deckName = deleteCardArgs.getDeckName();
        this.cardIndex = deleteCardArgs.getCardIndex();
    }

    /**
     * Deletes a card from a specific deck.
     *
     * @param deckManager Represents the current deckManager state.
     * @param ui Prints out message that card was deleted.
     * @return false, indicating the program should not terminate after executing this object.
     * @throws FlashException Throws DECK_NOT_FOUND, indicating that deckName input by the user does not exist.
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        Card card = deck.deleteCard(cardIndex);
        ui.showCardDeleted(card, deckName);
        return false;
    }
}
