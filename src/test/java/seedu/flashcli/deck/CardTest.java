package seedu.flashcli.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CardTest {

    @Test
    void constructorWithParams_setsFieldsCorrectly() {
        Card card = new Card("What is Java?", "A programming language.");
        assertEquals("What is Java?", card.getQuestion());
        assertEquals("A programming language.", card.getAnswer());
    }

    @Test
    void defaultConstructor_fieldsAreNull() {
        Card card = new Card();
        assertNull(card.getQuestion());
        assertNull(card.getAnswer());
    }

    @Test
    void getQuestionString_formatsCorrectly() {
        Card card = new Card("Question text", "Answer text");
        assertEquals("Question text", card.getQuestion());
    }

    @Test
    void getAnswerString_formatsCorrectly() {
        Card card = new Card("Q", "A");
        assertEquals("A", card.getAnswer());
    }

    @Test
    void getFormattedStrings_withNullValues() {
        Card card = new Card();
        assertNull(card.getQuestion());
        assertNull(card.getAnswer());
        assertEquals(card.getConfidenceLevel(),0);
    }
}
