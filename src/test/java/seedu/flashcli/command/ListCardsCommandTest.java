package seedu.flashcli.command;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.parser.AddCardArgs;
import seedu.flashcli.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListCardsCommandTest {
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
    public void execute_validDeck_listCards() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner(System.in);
        Command commandCreateDeck = new CreateDeckCommand("MA1513");
        commandCreateDeck.execute(deckManager, ui, in);
        AddCardArgs addCardArgs = new AddCardArgs("MA1513", "1+1", "2");
        Command commandAddCard = new AddCardCommand(addCardArgs);
        commandAddCard.execute(deckManager, ui, in);

        AddCardArgs addCardArgs2 = new AddCardArgs("MA1513", "2+2", "4");
        Command commandAddCard2 = new AddCardCommand(addCardArgs2);
        commandAddCard2.execute(deckManager, ui, in);

        AddCardArgs addCardArgs3 = new AddCardArgs("MA1513", "3+3", "6");
        Command commandAddCard3 = new AddCardCommand(addCardArgs3);
        commandAddCard3.execute(deckManager, ui, in);

        outContent.reset();

        Command commandListCards = new ListCardsCommand("MA1513");
        commandListCards.execute(deckManager, ui, in);

        String expectedOutput = ""
                + LINE + "\n"
                + "Here are all the cards in the deck MA1513: \n"
                + "1. 1+1\n"
                + "2\n"
                + "\n"
                + "2. 2+2\n"
                + "4\n"
                + "\n"
                + "3. 3+3\n"
                + "6\n"
                + "\n"
                + LINE + "\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));
    }
}
