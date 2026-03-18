package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExitCommandTest {
    @Test
    public void execute_exitCommand_returnsTrue() {
        DeckManager deckManager = new DeckManager();
        Command command = new ExitCommand();
        Ui ui = new Ui();
        try {
            assertTrue(command.execute(deckManager, ui));
        } catch (FlashException e) {
            System.out.println(e.getMessage());
        }
    }
}
