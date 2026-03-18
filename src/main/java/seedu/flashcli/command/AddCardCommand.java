package seedu.flashcli.command;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.ui.Ui;

public class AddCardCommand implements Command {
    private String deckName;
    private String question;
    private String answer;

    /**
     * Creates an AddCardCommand object
     *
     * @param addCardArgs Dataclass contains all attributes of an AddCardCommand object.
     */
    public AddCardCommand(AddCardArgs addCardArgs) {
        this.deckName = addCardArgs.getDeckName();
        this.question = addCardArgs.getQuestion();
        this.answer = addCardArgs.getAnswer();
    }

    /**
     * Adds a card to a specific deck.
     *
     * @param deckManager Represents the current deckManager state.
     * @param ui Prints out message that card was added.
     * @return false, indicating the program should not terminate after executing this object.
     * @throws FlashException Throws DECK_NOT_FOUND, indicating that deckName input by the user does not exist.
     */
    public boolean execute(DeckManager deckManager, Ui ui) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        if (deck == null) {
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }
        Card card = deck.addCard(question, answer);
        ui.showCardAdded(card, deckName);
        return false;
    }
}
