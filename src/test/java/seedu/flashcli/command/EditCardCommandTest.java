package seedu.flashcli.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.parser.EditCardArgs;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EditCardCommandTest {

    private DeckManager deckManager;
    private Ui ui;
    private Scanner in;

    @BeforeEach
    void setUp() throws FlashException {
        deckManager = new DeckManager();
        ui = new Ui();
        in = new Scanner(System.in);

        // Create a deck and add a card to use across tests
        new CreateDeckCommand("Math").execute(deckManager, ui, in);
        new AddCardCommand(new AddCardArgs("Math", "What is 2+2?", "4")).execute(deckManager, ui, in);
    }

    @Test
    void execute_validEdit_updatesQuestionAndAnswer() throws FlashException {
        EditCardArgs args = new EditCardArgs("Math", 0, "What is 3+3?", "6");
        Command editCommand = new EditCardCommand(args);

        assertFalse(editCommand.execute(deckManager, ui, in));

        Card edited = deckManager.getDeck("Math").getCard(0);
        assertEquals("What is 3+3?", edited.getQuestion());
        assertEquals("6", edited.getAnswer());
    }

    @Test
    void execute_validEdit_doesNotChangeDeckSize() throws FlashException {
        EditCardArgs args = new EditCardArgs("Math", 0, "What is 3+3?", "6");
        new EditCardCommand(args).execute(deckManager, ui, in);

        assertEquals(1, deckManager.getDeck("Math").getSize());
    }

    @Test
    void execute_invalidIndex_throwsFlashException() {
        EditCardArgs args = new EditCardArgs("Math", 5, "Q?", "A");
        Command editCommand = new EditCardCommand(args);

        FlashException e = assertThrows(FlashException.class,
                () -> editCommand.execute(deckManager, ui, in));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }

    @Test
    void execute_negativeIndex_throwsFlashException() {
        EditCardArgs args = new EditCardArgs("Math", -1, "Q?", "A");
        Command editCommand = new EditCardCommand(args);

        FlashException e = assertThrows(FlashException.class,
                () -> editCommand.execute(deckManager, ui, in));
        assertEquals(ErrorType.INVALID_INDEX, e.getErrorType());
    }

    @Test
    void execute_deckNotFound_throwsFlashException() {
        EditCardArgs args = new EditCardArgs("NonExistentDeck", 0, "Q?", "A");
        Command editCommand = new EditCardCommand(args);

        FlashException e = assertThrows(FlashException.class,
                () -> editCommand.execute(deckManager, ui, in));
        assertEquals(ErrorType.DECK_NOT_FOUND, e.getErrorType());
    }

    @Test
    void execute_returnsfalse() throws FlashException {
        EditCardArgs args = new EditCardArgs("Math", 0, "New Q?", "New A");
        assertFalse(new EditCardCommand(args).execute(deckManager, ui, in));
    }
}
