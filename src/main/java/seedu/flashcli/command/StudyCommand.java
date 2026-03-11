package seedu.flashcli.command;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.study.StudySession;

import java.util.Scanner;

public class StudyCommand implements Command {
    private Scanner in = new Scanner(System.in);
    private Deck deck;

    public StudyCommand(Deck deck) {
        this.deck = deck;
    }

    public boolean execute(DeckManager deckManager) {
        StudySession studySession = new StudySession(deck);
        studySession.start();
        boolean showQn = false; //tells program to show qn if true, ans if false
        boolean endSession = false;
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
