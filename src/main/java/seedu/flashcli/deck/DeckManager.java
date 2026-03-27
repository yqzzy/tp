package seedu.flashcli.deck;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeckManager {
    private static final Logger LOGGER = Logger.getLogger(DeckManager.class.getName());

    // Key is deck name, Value is the Deck object
    private HashMap<String, Deck> deckMap = new HashMap<>();

    /**
     * Creates and stores a new Deck.
     *
     * @param deckName the name of the deck to create; must not be null or blank
     * @throws FlashException if {@code deckName} is null/blank ({@code INVALID_ARGUMENTS})
     *                        or a deck with that name already exists ({@code DUPLICATE_NAME})
     */
    public void createDeck(String deckName) throws FlashException {
        LOGGER.log(Level.FINE, "Attempting to create deck: {0}", deckName);

        if (deckName == null || deckName.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "createDeck called with null or blank name");
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }

        if (deckMap.containsKey(deckName)) {
            LOGGER.log(Level.WARNING, "createDeck failed - duplicate deck name: {0}", deckName);
            throw new FlashException(ErrorType.DUPLICATE_NAME);
        }

        Deck newDeck = new Deck(deckName);
        deckMap.put(deckName, newDeck);

        // Post-condition: deck must now be present in the map
        assert deckMap.containsKey(deckName) : "Deck was not inserted into deckMap after createDeck";
        assert deckMap.get(deckName) != null : "Null deck stored for name: " + deckName;

        LOGGER.log(Level.INFO, "Deck created successfully: {0}", deckName);
    }

    /**
     * Returns a list of all deck names for the Ui to print.
     *
     * @return a new {@link List} containing all current deck names; never null, may be empty
     */
    public List<String> listDecks() {
        LOGGER.log(Level.FINE, "Listing all decks. Current count: {0}", deckMap.size());

        List<String> names = new ArrayList<>(deckMap.keySet());

        // Post-condition: returned list size must match map size
        assert names.size() == deckMap.size()
                : "listDecks() returned a list with different size than deckMap";

        return names;
    }

    /**
     * Removes the named deck.
     *
     * @param deckName the name of the deck to remove; must not be null or blank
     * @throws FlashException if {@code deckName} is null/blank ({@code INVALID_ARGUMENTS})
     *                        or no deck with that name exists ({@code DECK_NOT_FOUND})
     */
    public void deleteDeck(String deckName) throws FlashException {
        if (deckName == null || deckName.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "deleteDeck called with null or blank name");
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }

        LOGGER.log(Level.FINE, "Attempting to delete deck: {0}", deckName);

        if (!deckMap.containsKey(deckName)) {
            LOGGER.log(Level.WARNING, "deleteDeck: no deck found with name: {0}", deckName);
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }

        deckMap.remove(deckName);

        // Post-condition: deck must be gone after removal
        assert !deckMap.containsKey(deckName)
                : "Deck still present in deckMap after deleteDeck: " + deckName;

        LOGGER.log(Level.INFO, "Deck deleted: {0}", deckName);
    }

    /**
     * Retrieves a Deck by name.
     *
     * @param deckName the name of the deck to retrieve; must not be null or blank
     * @return the {@link Deck} associated with {@code deckName}
     * @throws FlashException if {@code deckName} is null/blank ({@code INVALID_ARGUMENTS})
     *                        or no deck with that name exists ({@code DECK_NOT_FOUND})
     */
    public Deck getDeck(String deckName) throws FlashException {
        if (deckName == null || deckName.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "getDeck called with null or blank name");
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }

        LOGGER.log(Level.FINE, "Retrieving deck: {0}", deckName);

        Deck deck = deckMap.get(deckName);
        if (deck == null) {
            LOGGER.log(Level.WARNING, "getDeck: deck not found: {0}", deckName);
            throw new FlashException(ErrorType.DECK_NOT_FOUND);
        }

        // Post-condition: returned deck must match the requested name
        assert deck.getDeckName().equals(deckName)
                : "Returned deck name mismatch. Expected: " + deckName + ", got: " + deck.getDeckName();

        LOGGER.log(Level.FINE, "Deck retrieved: {0}", deckName);
        return deck;
    }
}
