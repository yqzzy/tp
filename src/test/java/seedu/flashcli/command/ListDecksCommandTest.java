package seedu.flashcli.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ListDecksCommandTest {
    private static final String LINE = "_______________________________"; //ensure this is in sync with UI
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void execute_validProgram_showDecks() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner(System.in);
        Command command = new CreateDeckCommand("MA1513");
        command.execute(deckManager, ui, in);
        Command command2 = new CreateDeckCommand("MA1514");
        command2.execute(deckManager, ui, in);
        Command command3 = new CreateDeckCommand("MA1515");
        command3.execute(deckManager, ui, in);
        outContent.reset();

        Command listDecksCommand = new ListDecksCommand();
        assertFalse(listDecksCommand.execute(deckManager, ui, in));

        String expectedOutput = ""
                + LINE + "\n"
                + "Here are all the decks you have currently: \n"
                + "1. MA1513\n"
                + "2. MA1514\n"
                + "3. MA1515\n"
                + LINE + "\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));

    }
}
