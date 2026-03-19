package seedu.flashcli.command;

import seedu.flashcli.deck.Deck;

import java.util.Scanner;

import seedu.flashcli.deck.Card;
import seedu.flashcli.deck.DeckManager;
import seedu.flashcli.study.SessionManager;
import seedu.flashcli.ui.Ui;
import seedu.flashcli.exception.FlashException;

public class StudyCommand implements Command {
    private final String deckName;

    public StudyCommand(String deckName) {
        this.deckName = deckName;
    }

    @Override
    public boolean execute(DeckManager deckManager, Ui ui, Scanner in) throws FlashException {
        Deck deck = deckManager.getDeck(deckName);
        if (deck.getSize() == 0) {
            ui.showEmptyDeck();
            return false;
        }
        SessionManager sessionManager = new SessionManager();
        sessionManager.startSession(deck);

        // Show the first question immediately
        Card first = sessionManager.getCurrentCard();
        ui.showStudyQuestion(first.getQuestion());

        boolean showingAnswer = false; // false = next Enter shows answer, true = next Enter advances card

        String line;
        while (!(line = in.nextLine()).equals("q")) {
            if (!showingAnswer) {
                // Reveal answer for current card
                Card current = sessionManager.getCurrentCard();
                ui.showStudyAnswer(current.getAnswer());
                showingAnswer = true;
            } else {
                // Advance to next card
                boolean finished = sessionManager.nextCard();
                if (finished) {
                    int reviewed = sessionManager.finishSession();
                    ui.showStudySessionEnd(reviewed);
                    return false;
                }
                Card next = sessionManager.getCurrentCard();
                ui.showStudyQuestion(next.getQuestion());
                showingAnswer = false;
            }
        }
        int reviewed = sessionManager.finishSession();
        ui.showStudySessionQuit(reviewed);
        return false;
    }
}
