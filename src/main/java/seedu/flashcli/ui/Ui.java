package seedu.flashcli.ui;

import seedu.flashcli.deck.Card;

import java.util.ArrayList;
import java.util.List;

public class Ui {
    private static final String LINE = "_______________________________";

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
     * Prints user message when a deck has been successfully deleted.
     *
     * @param deckName Name of the deck deleted.
     */
    public void showDeckDeleted(String deckName) {
        System.out.println(LINE);
        System.out.println("Deleted deck: " + deckName);
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
            System.out.println((i + 1) + ". " + cardList.get(i).getQuestion() + "\n" + cardList.get(i).getAnswer() + "\n");
        }
        System.out.println(LINE);
    }

    public void showHelp() {
        String helpMessage = """
                FlashCLI Command Guide
                ----------------------
                
                createDeck d/DECK_NAME
                    Create a new subject deck
                
                listDecks
                    List all subject decks
                
                clearDeck d/DECK_NAME
                    Clear all flashcards in a deck
                
                deleteDeck d/DECK_NAME
                    Delete a deck and all its cards
                
                addCard d/DECK_NAME q/QUESTION a/ANSWER
                    Add a flashcard to a deck
                
                listCards d/DECK_NAME
                    List all flashcards in a deck
                
                deleteCard d/DECK_NAME i/INDEX
                    Delete a flashcard by index
                
                editCard d/DECK_NAME i/INDEX q/QUESTION a/ANSWER
                    Edit a flashcard's question and/or answer
                
                study d/DECK_NAME
                    Start study mode
                    - Press Enter to reveal the answer
                    - Rate your confidence 1–5 after each card
                    - Type 'q' to quit early
                
                help
                    Show this help message
                
                exit
                    Exit the program
                
                =================================================
                """;
        System.out.println(helpMessage);
    }

    /**
     * Prints the current question at the start of or during a study session.
     *
     * @param question The question text of the current card.
     */
    public void showStudyQuestion(String question) {
        System.out.println("Q: " + question);
        System.out.println("(Press Enter to reveal answer, or type 'q' to quit)");
    }

    /**
     * Prints the answer for the current card during a study session.
     *
     * @param answer The answer text of the current card.
     */
    public void showStudyAnswer(String answer) {
        System.out.println("A: " + answer);
    }

    /**
     * Prints the end-of-deck message and session summary.
     *
     * @param reviewed Number of cards reviewed in the session.
     */
    public void showStudySessionEnd(int reviewed) {
        System.out.println("End of deck!");
        System.out.println("Session ended. Cards reviewed: " + reviewed);
    }

    /**
     * Prints the session summary when the user quits mid-session.
     *
     * @param reviewed Number of cards reviewed before quitting.
     */
    public void showStudySessionQuit(int reviewed) {
        System.out.println("Session ended. Cards reviewed: " + reviewed);
    }

    /**
     * Prints a message when the user tries to study an empty deck.
     */
    public void showEmptyDeck() {
        System.out.println(LINE);
        System.out.println("Deck is empty. Add cards before studying.");
        System.out.println(LINE);
    }

    /**
     * Prints out error message together with the user input that caused it.
     *
     * @param errorMsg String to print out.
     */
    public void showError(String userInput, String errorMsg) {
        System.out.println("Error: " + errorMsg);
    }

    public void showConfidencePrompt(){
        System.out.println("Please rate your confidence (1-5): ");
    }

    public void showCardEdited(Card card, String deckName){
        System.out.println(LINE);
        System.out.println("Edited card in deck " + deckName + ":\n" 
            + card.getQuestion() + "\n" 
            + card.getAnswer());
        System.out.println(LINE); 
    }
}
