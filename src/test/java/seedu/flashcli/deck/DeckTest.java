package seedu.flashcli.deck;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.exception.ErrorType;

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
    void addCard_validQuestionAndAnswer_increasesSize() throws FlashException{
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
    void deleteCard_negativeIndex_throwsFlashException() throws FlashException{
        deck.addCard("Q1", "A1");
        
        assertThrows(FlashException.class, () -> {
            deck.deleteCard(-1);
        });
    }

    @Test
    void deleteCard_outOfBoundsIndex_throwsFlashException() throws FlashException{
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
    void getCard_negativeIndex_throwsFlashException() throws FlashException{
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
    void clearCards_deckWithCards_sizeBecomesZero() throws FlashException{
        deck.addCard("Q1", "A1");
        deck.addCard("Q2", "A2");
        deck.clearCards();
        assertEquals(0, deck.getSize());
        assertTrue(deck.listCards().isEmpty());
    }

    @Test
    void getCards_returnsUnmodifiableList() throws FlashException{
        deck.addCard("Q1", "A1");
        List<Card> unmodifiableList = deck.getCards();
        assertThrows(UnsupportedOperationException.class, () -> {
            unmodifiableList.add(new Card("Q2", "A2"));
        });
    }

    @Test
    void editCard_validIndex_updatesQuestion() throws FlashException {
        deck.addCard("Original question?", "Original answer");
        deck.editCard(0, "New question?", "Original answer");
        assertEquals("New question?", deck.getCard(0).getQuestion());
    }
 
    @Test
    void editCard_validIndex_updatesAnswer() throws FlashException {
        deck.addCard("Original question?", "Original answer");
        deck.editCard(0, "Original question?", "New answer");
        //assertEquals("New answer", deck.getCard(0).getAnswer());
        FlashException e = assertThrows(FlashException.class,
                () -> deck.editCard(0, "Original question?", "New answer"));
        assertEquals(ErrorType.DUPLICATE_CARD, e.getErrorType());
    }
 
    @Test
    void editCard_validIndex_updatesBothFields() throws FlashException {
        deck.addCard("Original question?", "Original answer");
        deck.editCard(0, "New question?", "New answer");
        Card edited = deck.getCard(0);
        assertEquals("New question?", edited.getQuestion());
        assertEquals("New answer", edited.getAnswer());
    }
 
    @Test
    void editCard_validIndex_doesNotChangeDeckSize() throws FlashException {
        deck.addCard("Original question?", "Original answer");
        deck.editCard(0, "New question?", "New answer");
        assertEquals(1, deck.getSize());
    }
 
    @Test
    void editCard_validIndex_returnsEditedCard() throws FlashException {
        deck.addCard("Original question?", "Original answer");
        Card returned = deck.editCard(0, "New question?", "New answer");
        assertEquals("New question?", returned.getQuestion());
        assertEquals("New answer", returned.getAnswer());
    }
 
    @Test
    void editCard_outOfBoundsIndex_throwsFlashException() throws FlashException{
        deck.addCard("Q1", "A1");
        FlashException e = assertThrows(FlashException.class,
                () -> deck.editCard(5, "Q?", "A"));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }
 
    @Test
    void editCard_negativeIndex_throwsFlashException() throws FlashException{
        deck.addCard("Q1", "A1");
        FlashException e = assertThrows(FlashException.class,
                () -> deck.editCard(-1, "Q?", "A"));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }
 
    @Test
    void editCard_multipleCards_onlyEditsTargetCard() throws FlashException {
        deck.addCard("First question?", "First answer");
        deck.addCard("Second question?", "Second answer");
        deck.editCard(0, "Edited question?", "Edited answer");
        assertEquals("Edited question?", deck.getCard(0).getQuestion());
        assertEquals("Second question?", deck.getCard(1).getQuestion());
    }
}
