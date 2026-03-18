package seedu.flashcli.ui;

import seedu.flashcli.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Ui {
    private final String LINE = "_______________________________";

    public void hello() {
        System.out.println("Welcome to FlashCLI!");
    }

    /**
     * Prints user message when a card has been successfully added to a specific deck.
     *
     * @param card Card that was added.
     * @param deckName Name of deck to which the card was added.
     */
    public void showCardAdded(Card card, String deckName) {
        System.out.println(LINE);
        System.out.println("Added Card: \n"
                + card.getQuestion() + "\n"
                + card.getAnswer() + "\n"
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
                + card.getQuestion() + "\n"
                + card.getAnswer() + "\n"
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
     * Prints user message when a deck has been successfully cleared.
     *
     * @param deckName Name of the deck cleared.
     */
    public void showDeckCleared(String deckName) {
        System.out.println(LINE);
        System.out.println("Cleared deck: " + deckName);
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
            System.out.println((i+1) + ". " + deckNameList.get(i));
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
            System.out.println((i+1) + ". " + cardList.get(i).getQuestion() + "\n" + cardList.get(i).getAnswer() + "\n");
        }
        System.out.println(LINE);
    }

    public void showHelp() {
        String helpMessage = """
            FlashCLI Command Guide

            createsub d/DECK_NAME
                Create a new subject deck

            listsub
                List all subject decks

            addcard d/DECK q/QUESTION a/ANSWER
                Add a flashcard to a deck

            listcards d/DECK
                List all flashcards in a deck

            study d/DECK
                Start study mode
            
            q
                End study mode
                
            enter (keystroke)
                show next card/answer during an active study session

            delete d/DECK i/INDEX
                Delete a flashcard by index

            clear d/DECK
                Clear all flashcards in a deck

            help
                Show this help message

            exit
                Exit the program
                
            =================================================
            """;

        System.out.println(helpMessage);
    }

    /**
     * Prints out error message together with the user input that caused it.
     *
     * @param errorMsg String to print out.
     */
    public void showError(String userInput, String errorMsg) {
        System.out.println("Error encountered: \n"
            + "Error msg: " + errorMsg + "\n"
            + "User Input: " + userInput
            );
    }
}
