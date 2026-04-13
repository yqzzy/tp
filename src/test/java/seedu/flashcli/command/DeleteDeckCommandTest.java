package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.DeckArgs;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeleteDeckCommandTest {

    @Test
    public void execute_existingDeck_deletesSuccessfully() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner("yes\n");

        DeckArgs deckArgs = new DeckArgs("MA1513");
        Command commandCreateDeck = new CreateDeckCommand(deckArgs.getDeckName());
        commandCreateDeck.execute(deckManager, ui, in);

        Command commandDeleteDeck = new DeleteDeckCommand(deckArgs.getDeckName());
        assertFalse(commandDeleteDeck.execute(deckManager, ui, in));

        FlashException ex = assertThrows(FlashException.class, () -> deckManager.getDeck("MA1513"));
        assertEquals(ErrorType.DECK_NOT_FOUND, ex.getErrorType());
    }

    @Test
    public void execute_nonExistentDeck_throwsException() {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner("yes\n");

        DeckArgs deckArgs = new DeckArgs("MA1513");
        Command commandDeleteDeck = new DeleteDeckCommand(deckArgs.getDeckName());
        FlashException ex = assertThrows(FlashException.class,
                () -> commandDeleteDeck.execute(deckManager, ui, in));
        assertEquals(ErrorType.DECK_NOT_FOUND, ex.getErrorType());
    }
}
