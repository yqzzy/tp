package seedu.flashcli.study;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.Deck;
import seedu.flashcli.exception.FlashException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StudySessionTest {

    private Deck sampleDeck;

    @BeforeEach
    void setUp() {
        sampleDeck = new Deck("Java");
        sampleDeck.addCard("Q1", "A1");
        sampleDeck.addCard("Q2", "A2");
    }

    @Test
    public void getCurrentCard_initialState_returnsFirstCard() throws FlashException {
        StudySession session = new StudySession(sampleDeck);
        // currentIndex starts at 0
        assertEquals("Q1", session.getCurrentCard().getQuestion());
    }

    @Test
    public void nextCard_traversal_returnsCorrectFinishedStatus() throws FlashException {
        StudySession session = new StudySession(sampleDeck);

        // At start (index 0), nextCard() moves to index 1
        boolean isFinished = session.nextCard();
        assertFalse(isFinished, "Should not be finished after moving to second card");
        assertEquals("Q2", session.getCurrentCard().getQuestion());

        // At index 1, nextCard() moves to index 2 (size is 2)
        isFinished = session.nextCard();
        assertTrue(isFinished, "Should be finished after the last card");
    }

    @Test
    public void finish_activeSession_returnsCorrectCount() {
        StudySession session = new StudySession(sampleDeck);
        session.nextCard(); // Move to index 1

        // finish() calculates (currentIndex + 1), which is (1 + 1) = 2
        int count = session.finish();
        assertEquals(2, count);
    }
}
