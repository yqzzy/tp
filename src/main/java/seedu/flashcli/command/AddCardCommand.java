package seedu.flashcli.command;

import java.util.Scanner;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.CommandFormat;
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
        assert addCardArgs != null : "AddCardArgs should not be null";
        assert addCardArgs.getDeckName() != null : "addCardArgs.deckName should not be null";
        assert addCardArgs.getQuestion() != null : "addCardArgs.question should not be null";
        assert addCardArgs.getAnswer() != null : "addCardArgs.answer should not be null";

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
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        try {
            Deck deck = deckManager.getDeck(deckName);
            Card card = deck.addCard(question, answer);
            ui.showCardAdded(card, deckName);
            return false;
        } catch (FlashException e) {
            throw new FlashException(e, CommandFormat.ADD_CARD);
        }
    }
}
