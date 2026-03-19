package seedu.flashcli.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.exception.FlashException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck("cs2113");
    }

    @Test
    void constructor_validName_setsNameCorrectly() {
        assertEquals("cs2113", deck.getDeckName());
    }

    @Test
    void emptyConstructor_createsDeckWithNullName() {
        Deck emptyDeck = new Deck();
        assertEquals(null, emptyDeck.getDeckName());
        assertEquals(0, emptyDeck.getSize());
    }

    @Test
    void addCard_validQuestionAndAnswer_increasesSize() {
        deck.addCard("question 1?", "answer 1");
        assertEquals(1, deck.getSize());
    }

    @Test
    void deleteCard_validIndex_removesCardAndDecreasesSize() throws FlashException {
        deck.addCard("Q1", "A1");
        deck.addCard("Q2", "A2");
        
        Card removedCard = deck.deleteCard(0);
        
        assertNotNull(removedCard);
        assertEquals(1, deck.getSize());
    }

    @Test
    void deleteCard_negativeIndex_throwsFlashException() {
        deck.addCard("Q1", "A1");
        
        assertThrows(FlashException.class, () -> {
            deck.deleteCard(-1);
        });
    }

    @Test
    void deleteCard_outOfBoundsIndex_throwsFlashException() {
        deck.addCard("Q1", "A1");
        assertThrows(FlashException.class, () -> {
            deck.deleteCard(1);
        });
    }

    @Test
    void getCard_validIndex_returnsCardSuccessfully() throws FlashException {
        Card addedCard = deck.addCard("Q1", "A1");
        Card retrievedCard = deck.getCard(0);
        assertEquals(addedCard, retrievedCard);
    }

    @Test
    void getCard_negativeIndex_throwsFlashException() {
        deck.addCard("Q1", "A1");
        assertThrows(FlashException.class, () -> {
            deck.getCard(-1);
        });
    }

    @Test
    void getCard_outOfBoundsIndex_throwsFlashException() {
        assertThrows(FlashException.class, () -> {
            deck.getCard(0);
        });
    }

    @Test
    void clearCards_deckWithCards_sizeBecomesZero() {
        deck.addCard("Q1", "A1");
        deck.addCard("Q2", "A2");
        deck.clearCards();
        assertEquals(0, deck.getSize());
        assertTrue(deck.listCards().isEmpty());
    }

    @Test
    void getCards_returnsUnmodifiableList() {
        deck.addCard("Q1", "A1");
        List<Card> unmodifiableList = deck.getCards();
        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableList.add(new Card("Q2", "A2"));
        });
    }
}
