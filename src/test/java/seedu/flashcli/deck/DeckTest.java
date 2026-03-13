package seedu.flashcli.deck;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {

    @Test
    void constructorWithName_setsNameCorrectly() {
        Deck deck = new Deck("Math Deck");
        assertEquals("Math Deck", deck.getDeckName());
        assertEquals(0, deck.getSize());
    }

    @Test
    void defaultConstructor_nameNullAndEmpty() {
        Deck deck = new Deck();
        assertNull(deck.getDeckName());
        assertEquals(0, deck.getSize());
    }

    @Test
    void addCard_increasesSizeAndStoresCorrectly() {
        Deck deck = new Deck("Test");
        deck.addCard("Q1?", "A1");
        deck.addCard("Q2?", "A2");
        assertEquals(2, deck.getSize());
        assertEquals("Q1?", deck.getCard(0).getQuestion());
        assertEquals("A2", deck.getCard(1).getAnswer());
    }

    @Test
    void deleteCard_removesCorrectly() {
        Deck deck = new Deck("Test");
        deck.addCard("Keep", "A");
        deck.addCard("Delete", "B");
        deck.deleteCard(1);
        assertEquals(1, deck.getSize());
        assertEquals("Keep", deck.getCard(0).getQuestion());
    }

    @Test
    void deleteCard_invalidIndex_throwsException() {
        Deck deck = new Deck("Test");
        deck.addCard("Q", "A");
        assertThrows(IndexOutOfBoundsException.class, () -> deck.deleteCard(1));
        assertThrows(IndexOutOfBoundsException.class, () -> deck.deleteCard(-1));
    }

    @Test
    void getCard_invalidIndex_throwsException() {
        Deck deck = new Deck("Test");
        deck.addCard("Q", "A");
        assertThrows(IndexOutOfBoundsException.class, () -> deck.getCard(1));
        assertThrows(IndexOutOfBoundsException.class, () -> deck.getCard(-1));
    }

    @Test
    void getSize_reflectsChanges() {
        Deck deck = new Deck("Test");
        assertEquals(0, deck.getSize());
        deck.addCard("Q1", "A1");
        assertEquals(1, deck.getSize());
        deck.deleteCard(0);
        assertEquals(0, deck.getSize());
    }
}
