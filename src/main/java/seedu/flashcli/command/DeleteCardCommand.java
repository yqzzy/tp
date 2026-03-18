package seedu.flashcli.command;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.DeleteCardArgs;
import seedu.flashcli.ui.Ui;

public class DeleteCardCommand implements Command {
    private String deckName;
    private int cardIndex;

    /**
     * Creates a DeleteCardCommand object
     *
     * @param deleteCardArgs Dataclass contains all attributes of an DeleteCardCommand object.
     */
    public DeleteCardCommand(DeleteCardArgs deleteCardArgs) {
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
    public boolean execute(DeckManager deckManager, Ui ui) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        if (deck == null) {
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }
        Card card = deck.deleteCard(cardIndex);
        ui.showCardDeleted(card, deckName);
        return false;
    }
}
