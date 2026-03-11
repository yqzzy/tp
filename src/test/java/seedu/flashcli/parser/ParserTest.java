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
}