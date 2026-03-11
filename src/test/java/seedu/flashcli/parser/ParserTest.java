package seedu.flashcli.parser;

import org.junit.jupiter.api.Test;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Unit tests for the Parser class.
 * Tests are grouped by command type.
 */
public class ParserTest {

    // Input Validation Tests

    @Test
    void nullInput_throwsNullInput() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser(null));
        assertEquals(ErrorType.NULL_INPUT, e.getErrorType());
    }

    @Test
    void blankInput_throwsNullInput() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("   "));
        assertEquals(ErrorType.NULL_INPUT, e.getErrorType());
    }

    @Test
    void unknownCommand_throwsInvalidCommand() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("unknownCmd"));
        assertEquals(ErrorType.INVALID_COMMAND, e.getErrorType());
    }

    // addCard Tests

    @Test
    void addCard_valid_doesNotThrow() {
        assertDoesNotThrow(() -> new Parser("addCard d/Math q/What is 2+2? a/4"));
    }

    @Test
    void addCard_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard q/What is 2+2? a/4"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void addCard_missingQuestionPrefix_throwsMissingQuestion() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/Math a/4"));
        assertEquals(ErrorType.MISSING_QUESTION, e.getErrorType());
    }

    @Test
    void addCard_missingAnswerPrefix_throwsMissingAnswer() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/Math q/What is 2+2?"));
        assertEquals(ErrorType.MISSING_ANSWER, e.getErrorType());
    }

    @Test
    void addCard_wrongPrefixOrder_throwsInvalidAddCard() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard q/What is 2+2? d/Math a/4"));
        assertEquals(ErrorType.INVALID_ADD_CARD, e.getErrorType());
    }

    @Test
    void addCard_emptyDeckName_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/ q/What is 2+2? a/4"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    @Test
    void addCard_emptyQuestion_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/Math q/ a/4"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    @Test
    void addCard_emptyAnswer_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/Math q/What is 2+2? a/"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    @Test
    void addCard_duplicateDeckPrefix_throwsDuplicatePrefix() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("addCard d/Math d/Science q/What is 2+2? a/4"));
        assertEquals(ErrorType.DUPLICATE_PREFIX, e.getErrorType());
    }

    // listCards Tests

    @Test
    void listCards_valid_doesNotThrow() {
        assertDoesNotThrow(() -> new Parser("listCards d/Math"));
    }

    @Test
    void listCards_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("listCards Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void listCards_emptyDeckName_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("listCards d/"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    // deleteCard Tests

    @Test
    void deleteCard_valid_doesNotThrow() {
        assertDoesNotThrow(() -> new Parser("deleteCard d/Math i/1"));
    }

    @Test
    void deleteCard_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("deleteCard i/1"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void deleteCard_missingIndexPrefix_throwsMissingIndex() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("deleteCard d/Math"));
        assertEquals(ErrorType.MISSING_INDEX, e.getErrorType());
    }

    @Test
    void deleteCard_wrongPrefixOrder_throwsInvalidDeleteCard() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("deleteCard i/1 d/Math"));
        assertEquals(ErrorType.INVALID_DELETE_CARD, e.getErrorType());
    }

    @Test
    void deleteCard_nonIntegerIndex_throwsInvalidIndex() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("deleteCard d/Math i/abc"));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }

    @Test
    void deleteCard_emptyIndex_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class,
                () -> new Parser("deleteCard d/Math i/"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    // createDeck Tests

    @Test
    void createDeck_valid_doesNotThrow() {
        assertDoesNotThrow(() -> new Parser("createDeck d/Math"));
    }

    @Test
    void createDeck_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("createDeck Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void createDeck_emptyDeckName_throwsArgumentMissing() {
        FlashException e = assertThrows(FlashException.class, () -> new Parser("createDeck d/"));
        assertEquals(ErrorType.ARGUMENT_MISSING, e.getErrorType());
    }

    // listDecks Tests

    @Test
    void listDecks_valid_doesNotThrow() {
        assertDoesNotThrow(() -> new Parser("listDecks"));
        assertDoesNotThrow(() -> new Parser("listDecks xyz"));
        assertDoesNotThrow(() -> new Parser("listDecks    "));
    }
    
}