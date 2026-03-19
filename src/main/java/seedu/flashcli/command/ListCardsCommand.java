package seedu.flashcli.command;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import java.util.ArrayList;
import java.util.Scanner;

public class ListCardsCommand implements Command {
    private final String deckName;

    /**
     * Creates a ListCardCommands object
     *
     * @param deckName The name (identifier) of the deck from which to list all the cards.
     */
    public ListCardsCommand(String deckName) {
        this.deckName = deckName;
    }

    /**
     * Lists all the cards in a specific deck for the user to see.
     *
     * @param deckManager Represents the current deckManager state.
     * @return false, false, indicating the program should not terminate after executing this object.
     */
    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        ArrayList<Card> cardList = deck.listCards();
        ui.showCardList(cardList, deckName);
        return false;
    }
}
