package seedu.flashcli;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.study.StudySession;

class FlashCLITest {

    @Test
    void testDeckManagerAndStudySession() {
        System.out.println("--- Testing DeckManager ---");
        DeckManager manager = new DeckManager();
        manager.createDeck("Java Basics");
        manager.createDeck("To Delete");
        manager.listDecks();
        manager.deleteDeck("To Delete");
        manager.listDecks();
        System.out.println("\n");

        System.out.println("--- Testing StudySession ---");
        // Manually adding cards to the deck to test the session
        manager.addCardToDeck("Java Basics", "What is a Class?", "A blueprint for objects");
        manager.addCardToDeck("Java Basics", "What is an Object?", "An instance of a class");

        Deck myDeck = manager.getDeck("Java Basics");
        StudySession session = new StudySession(myDeck);
        session.start();      // Should print Question 1
        session.showAnswer(); // Should print Answer 1
        session.nextCard();   // Should print Question 2
        session.finish();

        myDeck.listCards();
        manager.deleteCardFromDeck("Java Basics", 1);
        myDeck.listCards();
    }
}
