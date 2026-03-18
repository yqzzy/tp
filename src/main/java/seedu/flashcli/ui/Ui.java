package seedu.flashcli.ui;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.Deck;

import java.util.ArrayList;
import java.util.List;

public class Ui {
    private final String LINE = "_______________________________";
    /**
     * Prints user message when a card has been successfully added to a specific deck.
     *
     * @param card Card that was added.
     * @param deckName Name of deck to which the card was added.
     */
    public void showCardAdded(Card card, String deckName) {
        System.out.println(LINE);
        System.out.println("Added Card: \n"
                + card.getQuestionString() + "\n"
                + card.getAnswerString() + "\n"
                + "to deck " + deckName
                );
        System.out.println(LINE);
    }

    /**
     * Prints user message when a card has been successfully deleted from a specific deck.
     *
     * @param card Card that was deleted.
     * @param deckName Name of deck from which the card was deleted.
     */
    public void showCardDeleted(Card card, String deckName) {
        System.out.println(LINE);
        System.out.println("Deleted Card: \n"
                + card.getQuestionString() + "\n"
                + card.getAnswerString() + "\n"
                + "from deck " + deckName
        );
        System.out.println(LINE);
    }

    /**
     * Prints user message when a deck has been successfully created.
     *
     * @param deckName Name of the deck created.
     */
    public void showDeckCreated(String deckName) {
        System.out.println(LINE);
        System.out.println("Created deck: " + deckName);
        System.out.println(LINE);
    }

    //Prints goodbye message when program is to terminate.
    public void bye() {
        System.out.println(LINE);
        System.out.println("Session Ending. Goodbye!");
        System.out.println(LINE);
    }

    /**
     * Prints out a list of all the decks currently.
     *
     * @param deckNameList String of ordered list of names of current decks in deckManager.
     */
    public void showDeckList(List<String> deckNameList) {
        System.out.println(LINE);
        System.out.println("Here are all the decks you have currently: ");
        for (int i = 0; i < deckNameList.size(); i++) {
            System.out.println(i + ". " + deckNameList.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints out a list of all the cards in a specific deck.
     *
     * @param cardList List of cards from a specific deck.
     */
    public void showCardList(ArrayList<Card> cardList, String deckName) {
        System.out.println(LINE);
        System.out.println("Here are all the cards in the deck " + deckName + ": ");
        for (int i = 0; i < cardList.size(); i++) {
            System.out.println(i + ". " + cardList.get(i).getQuestionString() + "\n" + cardList.get(i).getAnswerString() + "\n");
        }
        System.out.println(LINE);
    }

    /**
     * Prints out error message.
     *
     * @param errorMsg String to print out.
     */
    public void showError(String errorMsg) {
        System.out.println(errorMsg);
    }
}
