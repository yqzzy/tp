package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest {
    @Test
    public void execute_exitCommand_returnsTrue() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Command command = new ExitCommand();
        Scanner in = new Scanner(System.in);
        Ui ui = new Ui();
        assertTrue(command.execute(deckManager, ui, in));
    }
}
