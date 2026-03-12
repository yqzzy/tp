package seedu.flashcli.study;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.Deck;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class StudySessionTest {

    /**
     * Helper method to create a deck with sample cards for testing.
     */
    private Deck createSampleDeck() {
        Deck deck = new Deck("Java Basics");
        deck.addCard("What is JVM?", "Java Virtual Machine");
        deck.addCard("What is JRE?", "Java Runtime Environment");
        return deck;
    }

    @Test
    public void start_validDeck_doesNotThrow() {
        StudySession session = new StudySession(createSampleDeck());
        try {
            session.start();
            // Test passes if no exception occurs during initialization
        } catch (Exception e) {
            fail("start() should not throw an exception with a valid deck.");
        }
    }

    @Test
    public void nextCard_multipleCards_traversesSuccessfully() {
        StudySession session = new StudySession(createSampleDeck());
        session.start(); // Moves to 1st card (index 0)

        try {
            session.nextCard(); // Moves to 2nd card (index 1)
            session.nextCard(); // Should handle "End of deck reached"
        } catch (Exception e) {
            fail("nextCard() failed during normal traversal or at deck boundary.");
        }
    }

    @Test
    public void showAnswer_sessionActive_doesNotThrow() {
        StudySession session = new StudySession(createSampleDeck());
        session.start(); // currentIndex becomes 0

        try {
            session.showAnswer();
            // Successfully displays answer for the first card
        } catch (Exception e) {
            fail("showAnswer() failed while a card was active.");
        }
    }

    @Test
    public void showAnswer_sessionNotStarted_handled() {
        StudySession session = new StudySession(createSampleDeck());
        // currentIndex is -1 because start() hasn't been called

        try {
            session.showAnswer();
            // Should print "No card currently active" message without crashing
        } catch (Exception e) {
            fail("showAnswer() should handle inactive sessions without throwing exceptions.");
        }
    }

    @Test
    public void finish_activeSession_resetsAndReturnsTrue() {
        StudySession session = new StudySession(createSampleDeck());
        session.start();

        boolean result = session.finish();

        // Verify finish() returns true as per the method signature
        assertTrue(result, "finish() must return true to exit the main loop.");

        // After finish, trying to show answer should indicate session is reset
        try {
            session.showAnswer();
        } catch (Exception e) {
            fail("Session state after finish() caused a crash.");
        }
    }

    @Test
    public void start_emptyDeck_doesNotThrow() {
        Deck emptyDeck = new Deck("Empty");
        StudySession session = new StudySession(emptyDeck);

        try {
            session.start();
            // nextCard() inside start() should handle the size 0 check
        } catch (Exception e) {
            fail("start() failed on an empty deck.");
        }
    }
}
