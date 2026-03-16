package seedu.flashcli.parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
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
@Tag("parser")
public class ParserTest {

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidation {
        @Test
        @DisplayName("Parser rejects null input")
        void nullInput_throwsNullInput() {
            FlashException e = assertThrows(FlashException.class, () -> Parser.parse(null));
            assertEquals(ErrorType.NULL_INPUT, e.getErrorType());
        }

        @Test
        @DisplayName("Parser rejects blank input")
        void blankInput_throwsNullInput() {
            FlashException e = assertThrows(FlashException.class, () -> Parser.parse("   "));
            assertEquals(ErrorType.NULL_INPUT, e.getErrorType());
        }

        @Test
        @DisplayName("Parser rejects unknown command")
        void unknownCommand_throwsInvalidCommand() {
            FlashException e = assertThrows(FlashException.class, () -> Parser.parse("unknownCmd"));
            assertEquals(ErrorType.INVALID_COMMAND, e.getErrorType());
        }
    }

    @Nested
    @DisplayName("addCard Command Tests")
    class AddCardTests {

        @Test
        @DisplayName("addCard valid command parses successfully")
        void addCard_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("addCard d/Math q/What is 2+2? a/4"));
        }

        @Test
        @DisplayName("addCard missing deck prefix throws MISSING_DECK")
        void addCard_missingDeckPrefix_throwsMissingDeck() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard q/What is 2+2? a/4"));
            assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
        }

        @Test
        @DisplayName("addCard missing question prefix throws MISSING_QUESTION")
        void addCard_missingQuestionPrefix_throwsMissingQuestion() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/Math a/4"));
            assertEquals(ErrorType.MISSING_QUESTION, e.getErrorType());
        }

        @Test
        @DisplayName("addCard missing answer prefix throws MISSING_ANSWER")
        void addCard_missingAnswerPrefix_throwsMissingAnswer() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/Math q/What is 2+2?"));
            assertEquals(ErrorType.MISSING_ANSWER, e.getErrorType());
        }

        @Test
        @DisplayName("addCard wrong prefix order throws INVALID_ARGUMENTS")
        void addCard_wrongPrefixOrder_throwsInvalidArguments() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard q/What is 2+2? d/Math a/4"));
            assertEquals(ErrorType.INVALID_ARGUMENTS, e.getErrorType());
        }

        @Test
        @DisplayName("addCard empty deck name throws MISSING_DECK")
        void addCard_emptyDeckName_throwsMissingDeck() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/ q/What is 2+2? a/4"));
            assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
        }

        @Test
        @DisplayName("addCard empty question throws MISSING_QUESTION")
        void addCard_emptyQuestion_throwsMissingQuestion() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/Math q/ a/4"));
            assertEquals(ErrorType.MISSING_QUESTION, e.getErrorType());
        }

        @Test
        @DisplayName("addCard empty answer throws MISSING_ANSWER")
        void addCard_emptyAnswer_throwsMissingAnswer() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/Math q/What is 2+2? a/"));
            assertEquals(ErrorType.MISSING_ANSWER, e.getErrorType());
        }

        @Test
        @DisplayName("addCard duplicate deck prefix throws DUPLICATE_PREFIX")
        void addCard_duplicateDeckPrefix_throwsDuplicatePrefix() {
            FlashException e = assertThrows(FlashException.class,
                    () -> Parser.parse("addCard d/Math d/Science q/What is 2+2? a/4"));
            assertEquals(ErrorType.DUPLICATE_PREFIX, e.getErrorType());
        }

    }

    // listCards Tests

    @Test
    void listCards_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("listCards d/Math"));
    }

    @Test
    void listCards_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("listCards Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void listCards_emptyDeckName_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("listCards d/"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    // deleteCard Tests

    @Test
    void deleteCard_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("deleteCard d/Math i/1"));
    }

    @Test
    void deleteCard_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("deleteCard i/1"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void deleteCard_missingIndexPrefix_throwsMissingIndex() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("deleteCard d/Math"));
        assertEquals(ErrorType.MISSING_INDEX, e.getErrorType());
    }

    @Test
    void deleteCard_wrongPrefixOrder_throwsInvalidArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("deleteCard i/1 d/Math"));
        assertEquals(ErrorType.INVALID_ARGUMENTS, e.getErrorType());
    }

    @Test
    void deleteCard_nonIntegerIndex_throwsInvalidIndex() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("deleteCard d/Math i/abc"));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }

    @Test
    void deleteCard_emptyIndex_throwsMissingIndex() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("deleteCard d/Math i/"));
        assertEquals(ErrorType.MISSING_INDEX, e.getErrorType());
    }

    // createDeck Tests

    @Test
    void createDeck_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("createDeck d/Math"));
    }

    @Test
    void createDeck_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("createDeck Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void createDeck_emptyDeckName_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("createDeck d/"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    // listDecks Tests

    @Test
    void listDecks_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("listDecks"));
    }

    @Test
    void listDecks_unexpectedArguments_throwsUnexpectedArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("listDecks xyz"));
        assertEquals(ErrorType.UNEXPECTED_ARGUMENTS, e.getErrorType());
    }

    // clearDeck Tests

    @Test
    void clearDeck_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("clearDeck d/Math"));
    }

    @Test
    void clearDeck_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("clearDeck Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void clearDeck_emptyDeckName_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("clearDeck d/"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    // study Tests

    @Test
    void study_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("study d/Math"));
    }

    @Test
    void study_missingDeckPrefix_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("study Math"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    @Test
    void study_emptyDeckName_throwsMissingDeck() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("study d/"));
        assertEquals(ErrorType.MISSING_DECK, e.getErrorType());
    }

    // No-argument command Tests

    @Test
    void nextCard_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("nextCard"));
    }

    @Test
    void nextCard_unexpectedArguments_throwsUnexpectedArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("nextCard xyz"));
        assertEquals(ErrorType.UNEXPECTED_ARGUMENTS, e.getErrorType());
    }

    @Test
    void finish_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("finish"));
    }

    @Test
    void finish_unexpectedArguments_throwsUnexpectedArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("finish xyz"));
        assertEquals(ErrorType.UNEXPECTED_ARGUMENTS, e.getErrorType());
    }

    @Test
    void exit_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("exit"));
    }

    @Test
    void exit_unexpectedArguments_throwsUnexpectedArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("exit xyz"));
        assertEquals(ErrorType.UNEXPECTED_ARGUMENTS, e.getErrorType());
    }

    @Test
    void help_valid_doesNotThrow() {
        assertDoesNotThrow(() -> Parser.parse("help"));
    }

    @Test
    void help_unexpectedArguments_throwsUnexpectedArguments() {
        FlashException e = assertThrows(FlashException.class,
                () -> Parser.parse("help xyz"));
        assertEquals(ErrorType.UNEXPECTED_ARGUMENTS, e.getErrorType());
    }
}
