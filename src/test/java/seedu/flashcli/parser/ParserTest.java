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

    /**
     * Asserts that parsing the given input throws a FlashException with the expected ErrorType.
     */
    private void assertParseThrows(String input, ErrorType expected) {
        FlashException e = assertThrows(FlashException.class, () -> Parser.parse(input));
        assertEquals(expected, e.getErrorType());
    }

    @Nested
    @DisplayName("Input Validation Tests")
    class InputValidation {

        @Test
        @DisplayName("Parser rejects null input")
        void nullInput_throwsNullInput() {
            assertParseThrows(null, ErrorType.NULL_INPUT);
        }

        @Test
        @DisplayName("Parser rejects blank input")
        void blankInput_throwsNullInput() {
            assertParseThrows("   ", ErrorType.NULL_INPUT);
        }

        @Test
        @DisplayName("Parser rejects unknown command")
        void unknownCommand_throwsInvalidCommand() {
            assertParseThrows("unknownCmd", ErrorType.INVALID_COMMAND);
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
            assertParseThrows("addCard q/What is 2+2? a/4", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("addCard missing question prefix throws MISSING_QUESTION")
        void addCard_missingQuestionPrefix_throwsMissingQuestion() {
            assertParseThrows("addCard d/Math a/4", ErrorType.MISSING_QUESTION);
        }

        @Test
        @DisplayName("addCard missing answer prefix throws MISSING_ANSWER")
        void addCard_missingAnswerPrefix_throwsMissingAnswer() {
            assertParseThrows("addCard d/Math q/What is 2+2?", ErrorType.MISSING_ANSWER);
        }

        @Test
        @DisplayName("addCard wrong prefix order throws INVALID_ARGUMENTS")
        void addCard_wrongPrefixOrder_throwsInvalidArguments() {
            assertParseThrows("addCard q/What is 2+2? d/Math a/4", ErrorType.INVALID_ARGUMENTS);
        }

        @Test
        @DisplayName("addCard empty deck name throws MISSING_DECK")
        void addCard_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("addCard d/ q/What is 2+2? a/4", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("addCard empty question throws MISSING_QUESTION")
        void addCard_emptyQuestion_throwsMissingQuestion() {
            assertParseThrows("addCard d/Math q/ a/4", ErrorType.MISSING_QUESTION);
        }

        @Test
        @DisplayName("addCard empty answer throws MISSING_ANSWER")
        void addCard_emptyAnswer_throwsMissingAnswer() {
            assertParseThrows("addCard d/Math q/What is 2+2? a/", ErrorType.MISSING_ANSWER);
        }

        @Test
        @DisplayName("addCard duplicate deck prefix throws DUPLICATE_PREFIX")
        void addCard_duplicateDeckPrefix_throwsDuplicatePrefix() {
            assertParseThrows("addCard d/Math d/Science q/What is 2+2? a/4", ErrorType.DUPLICATE_PREFIX);
        }

        @Test
        @DisplayName("addCard with no arguments throws MISSING_DECK")
        void addCard_noArguments_throwsMissingDeck() {
            assertParseThrows("addCard", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("listCards Command Tests")
    class ListCardsTests {

        @Test
        @DisplayName("listCards valid command parses successfully")
        void listCards_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("listCards d/Math"));
        }

        @Test
        @DisplayName("listCards missing deck prefix throws MISSING_DECK")
        void listCards_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("listCards Math", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("listCards empty deck name throws MISSING_DECK")
        void listCards_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("listCards d/", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("deleteCard Command Tests")
    class DeleteCardTests {

        @Test
        @DisplayName("deleteCard valid command parses successfully")
        void deleteCard_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("deleteCard d/Math i/1"));
        }

        @Test
        @DisplayName("deleteCard missing deck prefix throws MISSING_DECK")
        void deleteCard_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("deleteCard i/1", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("deleteCard missing index prefix throws MISSING_INDEX")
        void deleteCard_missingIndexPrefix_throwsMissingIndex() {
            assertParseThrows("deleteCard d/Math", ErrorType.MISSING_INDEX);
        }

        @Test
        @DisplayName("deleteCard wrong prefix order throws INVALID_ARGUMENTS")
        void deleteCard_wrongPrefixOrder_throwsInvalidArguments() {
            assertParseThrows("deleteCard i/1 d/Math", ErrorType.INVALID_ARGUMENTS);
        }

        @Test
        @DisplayName("deleteCard non-integer index throws INVALID_INDEX")
        void deleteCard_nonIntegerIndex_throwsInvalidIndex() {
            assertParseThrows("deleteCard d/Math i/abc", ErrorType.INVALID_INDEX);
        }

        @Test
        @DisplayName("deleteCard empty index throws MISSING_INDEX")
        void deleteCard_emptyIndex_throwsMissingIndex() {
            assertParseThrows("deleteCard d/Math i/", ErrorType.MISSING_INDEX);
        }

        @Test
        @DisplayName("deleteCard negative index throws INVALID_INDEX")
        void deleteCard_negativeIndex_throwsInvalidIndex() {
            assertParseThrows("deleteCard d/Math i/-1", ErrorType.INVALID_INDEX);
        }
    }

    @Nested
    @DisplayName("createDeck Command Tests")
    class CreateDeckTests {

        @Test
        @DisplayName("createDeck valid command parses successfully")
        void createDeck_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("createDeck d/Math"));
        }

        @Test
        @DisplayName("createDeck missing deck prefix throws MISSING_DECK")
        void createDeck_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("createDeck Math", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("createDeck empty deck name throws MISSING_DECK")
        void createDeck_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("createDeck d/", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("listDecks Command Tests")
    class ListDecksTests {

        @Test
        @DisplayName("listDecks valid command parses successfully")
        void listDecks_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("listDecks"));
        }

        @Test
        @DisplayName("listDecks with unexpected arguments throws UNEXPECTED_ARGUMENTS")
        void listDecks_unexpectedArguments_throwsUnexpectedArguments() {
            assertParseThrows("listDecks someArgument", ErrorType.UNEXPECTED_ARGUMENTS);
        }
    }

    @Nested
    @DisplayName("clearDeck Command Tests")
    class ClearDeckTests {

        @Test
        @DisplayName("clearDeck valid command parses successfully")
        void clearDeck_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("clearDeck d/Math"));
        }

        @Test
        @DisplayName("clearDeck missing deck prefix throws MISSING_DECK")
        void clearDeck_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("clearDeck Math", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("clearDeck empty deck name throws MISSING_DECK")
        void clearDeck_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("clearDeck d/", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("deleteDeck Command Tests")
    class DeleteDeckTests {

        @Test
        @DisplayName("deleteDeck valid command parses successfully")
        void deleteDeck_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("deleteDeck d/Math"));
        }

        @Test
        @DisplayName("deleteDeck missing deck prefix throws MISSING_DECK")
        void deleteDeck_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("deleteDeck Math", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("deleteDeck empty deck name throws MISSING_DECK")
        void deleteDeck_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("deleteDeck d/", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("study Command Tests")
    class StudyTests {

        @Test
        @DisplayName("study valid command parses successfully")
        void study_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("study d/Math"));
        }

        @Test
        @DisplayName("study missing deck prefix throws MISSING_DECK")
        void study_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("study Math", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("study empty deck name throws MISSING_DECK")
        void study_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("study d/", ErrorType.MISSING_DECK);
        }
    }

    @Nested
    @DisplayName("No-Argument Command Tests")
    class NoArgumentCommandTests {

        @Test
        @DisplayName("exit valid command parses successfully")
        void exit_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("exit"));
        }

        @Test
        @DisplayName("exit unexpected arguments throws UNEXPECTED_ARGUMENTS")
        void exit_unexpectedArguments_throwsUnexpectedArguments() {
            assertParseThrows("exit xyz", ErrorType.UNEXPECTED_ARGUMENTS);
        }

        @Test
        @DisplayName("help valid command parses successfully")
        void help_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("help"));
        }

        @Test
        @DisplayName("help unexpected arguments throws UNEXPECTED_ARGUMENTS")
        void help_unexpectedArguments_throwsUnexpectedArguments() {
            assertParseThrows("help xyz", ErrorType.UNEXPECTED_ARGUMENTS);
        }
    }

    @Nested
    @DisplayName("editCard Command Tests")
    class EditCardTests {

        @Test
        @DisplayName("editCard valid command parses successfully")
        void editCard_valid_doesNotThrow() {
            assertDoesNotThrow(() -> Parser.parse("editCard d/Math i/1 q/What is 3+3? a/6"));
        }

        @Test
        @DisplayName("editCard missing deck prefix throws MISSING_DECK")
        void editCard_missingDeckPrefix_throwsMissingDeck() {
            assertParseThrows("editCard i/1 q/What is 3+3? a/6", ErrorType.MISSING_DECK);
        }

        @Test
        @DisplayName("editCard missing index prefix throws MISSING_INDEX")
        void editCard_missingIndexPrefix_throwsMissingIndex() {
            assertParseThrows("editCard d/Math q/What is 3+3? a/6", ErrorType.MISSING_INDEX);
        }

        @Test
        @DisplayName("editCard wrong prefix order throws INVALID_ARGUMENTS")
        void editCard_wrongPrefixOrder_throwsInvalidArguments() {
            assertParseThrows("editCard i/1 d/Math q/What is 3+3? a/6", ErrorType.INVALID_ARGUMENTS);
        }

        @Test
        @DisplayName("editCard non-numeric index throws INVALID_INDEX")
        void editCard_nonNumericIndex_throwsInvalidIndex() {
            assertParseThrows("editCard d/Math i/abc q/What is 3+3? a/6", ErrorType.INVALID_INDEX);
        }

        @Test
        @DisplayName("editCard empty deck name throws MISSING_DECK")
        void editCard_emptyDeckName_throwsMissingDeck() {
            assertParseThrows("editCard d/ i/1 q/What is 3+3? a/6", ErrorType.MISSING_DECK);
        }

        @Test 
        @DisplayName("ediCard empty question and empty answer throws INVALID_EDIT")
        void editCard_emptyQuestionEmptyAnswer_throwsInvalidEdit(){
            assertParseThrows("editCard d/Math i/1", ErrorType.INVALID_EDIT);
        }

        @Test
        @DisplayName("editCard empty question throws MISSING_QUESTION")
        void editCard_emptyQuestion_throwsMissingQuestion() {
            assertParseThrows("editCard d/Math i/1 q/ a/6", ErrorType.MISSING_QUESTION);
        }

        @Test
        @DisplayName("editCard empty answer throws MISSING_ANSWER")
        void editCard_emptyAnswer_throwsMissingAnswer() {
            assertParseThrows("editCard d/Math i/1 q/What is 3+3? a/", ErrorType.MISSING_ANSWER);
        }
    }
}
