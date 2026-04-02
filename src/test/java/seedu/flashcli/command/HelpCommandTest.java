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

public class HelpCommandTest {
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
    public void execute_validProgram_showHelp() throws FlashException {
        DeckManager deckManager = new DeckManager();
        Ui ui = new Ui();
        Scanner in = new Scanner(System.in);
        Command helpCommand = new HelpCommand();
        outContent.reset();
        helpCommand.execute(deckManager, ui, in);
        String expectedOutput = """
        FlashCLI Command Guide
            ----------------------
            
            createDeck d/DECK_NAME
                Create a new subject deck
            
            listDecks
                List all subject decks
            
            clearDeck d/DECK_NAME
                Clear all flashcards in a deck
            
            deleteDeck d/DECK_NAME
                Delete a deck and all its cards
            
            addCard d/DECK_NAME q/QUESTION a/ANSWER
                Add a flashcard to a deck
            
            listCards d/DECK_NAME
                List all flashcards in a deck
            
            deleteCard d/DECK_NAME i/INDEX
                Delete a flashcard by index
            
            editCard d/DECK_NAME i/INDEX q/QUESTION a/ANSWER
                Edit a flashcard's question and/or answer
            
            study d/DECK_NAME
                Start study mode
                - Press Enter to reveal the answer
                - Rate your confidence 1–5 after each card
                - Type 'q' to quit early
            
            help
                Show this help message
            
            exit
                Exit the program
            
            =================================================
            """ + "\n";

        assertEquals(expectedOutput.replaceAll("\\s+", ""),
                outContent.toString().replaceAll("\\s+", ""));
    }
}
