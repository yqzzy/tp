package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AddCardCommandTest {

    @Test
    public void execute_valid_addsCard() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner scanner = new Scanner(System.in);

        Command create = new CreateDeckCommand("MA1513");
        create.execute(deckManager, ui, scanner);

        AddCardArgs addCardArgs = new AddCardArgs("MA1513", "1+1", "2");
        Command add = new AddCardCommand(addCardArgs);

        assertFalse(add.execute(deckManager, ui, scanner));

        assertEquals("1+1",
                deckManager.getDeck("MA1513").getCard(0).getQuestion());
        assertEquals("2",
                deckManager.getDeck("MA1513").getCard(0).getAnswer());
    }
}