package seedu.flashcli.deck;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class DeckManager {
    // Key is deck name, Value is the Deck object
    private HashMap<String, Deck> deckMap = new HashMap<>();

    /**
     * Creates and stores a new Deck.
     * Throws FlashException if name is blank or already exists.
     */
    public void createDeck(String deckName) throws FlashException {
        if (deckName == null || deckName.trim().isEmpty()) {
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }

        if (deckMap.containsKey(deckName)) {
            // Updated to use the new DUPLICATE_NAME error type
            throw new FlashException(ErrorType.DUPLICATE_NAME);
        }

        deckMap.put(deckName, new Deck(deckName));
    }

    /**
     * Returns a list of all deck names for the Ui to print.
     */
    public List<String> listDecks() {
        return new ArrayList<>(deckMap.keySet());
    }

    /**
     * Removes the named deck.
     * @return true if found and removed, false otherwise.
     */
    public boolean deleteDeck(String deckName) {
        return deckMap.remove(deckName) != null;
    }

    /**
     * Retrieves a Deck by name.
     * Throws FlashException if the deck does not exist.
     */
    public Deck getDeck(String deckName) throws FlashException {
        Deck deck = deckMap.get(deckName);
        if (deck == null) {
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }
        return deck;
    }
}
