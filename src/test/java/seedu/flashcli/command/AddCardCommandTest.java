package seedu.flashcli.command;

import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class AddCardCommandTest {

    @Test
    public void execute_valid_addsCard() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner scanner = new Scanner("");

        Command create = new CreateDeckCommand("MA1513");
        create.execute(deckManager, ui, scanner);

        Command add = new AddCardCommand("MA1513", "1+1", "2");

        assertFalse(add.execute(deckManager, ui, scanner));

        assertEquals("Question: 1+1",
                deckManager.getDeck("MA1513").getCard(0).getQuestionString());
        assertEquals("Answer: 2",
                deckManager.getDeck("MA1513").getCard(0).getAnswerString());
    }
}