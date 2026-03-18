package seedu.flashcli.command;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;
import seedu.flashcli.study.StudySession;
import seedu.flashcli.ui.Ui;

import java.util.Scanner;

public class StudyCommand implements Command {
    private Scanner in = new Scanner(System.in);
    private String deckName;

    /**
     * Creates a StudyCommand object.
     *
     * @param deckName The name (identifier) of the deck which we want to study.
     */
    public StudyCommand(String deckName) {
        this.deckName = deckName;
    }

    /**
     * Runs a study session for s specific deck.
     *
     * @param deckManager Represents the current deckManager state.
     * @return false, indicating the program should not terminate after executing this object.
     * @throws FlashException Throws DECK_NOT_FOUND, indicating that deckName input by the user does not exist.
     */
    public boolean execute(DeckManager deckManager, Ui ui) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        if (deck == null) {
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }
        StudySession studySession = new StudySession(deck);
        studySession.startSession();
        boolean showQn = false; //tells program to show qn if true, ans if false
        boolean endSession = false;

        //logic to alternate between showing the next card (question), and revealing ans for the current card.
        while (!endSession && !in.nextLine().equals("q")) {
            if (showQn) {
                endSession = studySession.nextCard();
                showQn = false;
            } else {
                studySession.showAnswer();
                showQn = true;
            }
        }
        return false;
    }
}
