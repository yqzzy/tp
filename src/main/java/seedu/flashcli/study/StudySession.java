package seedu.flashcli.study;

import seedu.flashcli.deck.Deck;
import seedu.flashcli.deck.Card;

public class StudySession {
    private Deck deck;
    private int currentIndex = -1;

    public StudySession(Deck deck) {
        this.deck = deck;
    }

    /**
     * displays the question of the first card by calling nextCard()
      */
    public void start() {
        String startStudySession = "Studying deck: %s%n";
        System.out.printf((startStudySession), deck.getDeckName());
        this.nextCard();
    }

    /**
     * displays question of next card referenced by currentIndex
      */
    public boolean nextCard() {
        int size = deck.getSize();
        if (currentIndex + 1 < size) {
            currentIndex ++;
            Card currentCard = deck.getCard(currentIndex);
            String cardFront = "%d. %s%n";
            System.out.printf((cardFront), currentIndex+1, currentCard.getQuestionString());
            return false;
        } else {
            System.out.println("End of deck reached!");
            return this.finish();
        }
    }

    /**
     * displays answer of current card
      */
    public void showAnswer() {
        if (currentIndex >= 0) {
            Card currentCard = deck.getCard(currentIndex);
            String cardBack = "%d. %s%n";
            System.out.printf((cardBack), currentIndex+1, currentCard.getAnswerString());
        } else {
            System.out.println("No card currently active. Start study session first. ");
        }
    }

    /**
     * displays summary of number of cards reviewed
     * reset currentIndex and returns true to exit session and go back to main menu in main loop
      */
    public boolean finish() {
        System.out.println("Study session end!");
        System.out.printf("You reviewed %d cards in the '%s' deck.%n", (currentIndex + 1), deck.getDeckName());
        currentIndex = -1;
        return true;
    }
}
