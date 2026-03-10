package seedu.flashcli.deck;

import java.util.HashMap;

public class DeckManager {
    // Key is deck name, Value is Deck obj
    private HashMap<String, Deck> deckMap = new HashMap<>();

    /**
     * takes in the deckName, creates a new Deck object and
     * adds it to the deckMap
      */
    public void createDeck(String deckName) {
        if (!deckMap.containsKey(deckName)) {
            deckMap.put(deckName, new Deck(deckName));
            System.out.println("Deck '" + deckName + "' added!");
        } else {
            System.out.println("Deck already exists!");
        }
    }

    /**
     * prints out the deckName of every deck
     */
    public void listDecks() {
        int count = 1;
        for (String name : deckMap.keySet()) {
            System.out.printf("%d. %s%n", count, name);
            count++;
        }
    }

    /**
     * deletes the Deck object by the specified name
     */
    public void deleteDeck(String deckName) {
        if (deckMap.remove(deckName) != null) {
            System.out.printf("Deck '%s' removed!%n", deckName);
        } else {
            System.out.println("Deck not found.");
        }
    }

    /**
     * add card to a deck of the specified name
     */
    public void addCardToDeck(String deckName, String question, String answer) {
        Deck deck = deckMap.get(deckName);
        if (deck != null) {
            deck.addCard(question, answer);
        } else {
            System.out.println("Target deck not found.");
        }
    }

    /**
     * delete card from a deck of the specified name
     */
    public void deleteCardFromDeck(String deckName, int cardIndex) {
        Deck deck = deckMap.get(deckName);
        if (deck != null) {
            deck.deleteCard(cardIndex);
        } else {
            System.out.println("Target deck not found.");
        }
    }

    /**
     * returns the Deck object by the specified name
     */
    public Deck getDeck(String deckName) {
        return deckMap.get(deckName);
    }

}
