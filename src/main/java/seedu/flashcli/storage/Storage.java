package seedu.flashcli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import seedu.flashcli.deck.DeckManager;

/**
 * Manages data persistence for the FlashCLI application.
 * Handles saving and loading of flashcard data to/from JSON files,
 * with automatic versioning through the HistoryManager.
 */
public class Storage {
    private final Path filePath;
    private final Gson gson;
    private HistoryManager historyManager;

    /**
     * Constructs a Storage instance for the specified file path.
     *
     * @param storageFilePath the path to the main data file (e.g., "data/flashcards.json")
     */
    public Storage(String storageFilePath) {
        filePath = Paths.get(storageFilePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Path dataDir = filePath.getParent();
            String baseName = filePath.getFileName().toString().replace(".json", "");
            historyManager = new HistoryManager(dataDir.toString(), baseName);
        } catch (IOException e) {
            System.err.println("Warning: Failed to initialize HistoryManager. Versioning disabled.");
            this.historyManager = null;
        }
    }

    /**
     * Loads the DeckManager data from the main JSON file.
     * If the main file is corrupted, attempts to restore from the latest historical version.
     *
     * @return the loaded DeckManager, or an empty one if loading fails
     */
    public DeckManager load() {
        if (!Files.exists(filePath)) {
            return new DeckManager();
        }
        try {
            String jsonData = Files.readString(filePath);
            DeckManager manager = parse(jsonData);

            if (manager != null) {
                return manager;
            }

            System.err.println("Warning: Main data file is corrupted. Attempting to restore from latest historical version...");
            return restoreFromLatestHistory();

        } catch (IOException e) {
            System.err.println("Error occur in reading the file: " + e.getMessage());
            return new DeckManager();
        }
    }

    /**
     * Saves the given DeckManager data to the main JSON file.
     * Automatically creates a historical version before overwriting existing data.
     *
     * @param list the DeckManager data to save
     */
    public void save(DeckManager list) {
        try {
            Files.createDirectories(filePath.getParent());
            if (historyManager != null && Files.exists(filePath)) {
                try {
                    DeckManager currentDataOnDisk = load();
                    historyManager.saveVersion(currentDataOnDisk);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to create historical version: " + e.getMessage());
                }
            }
            String jsonData = serialise(list);
            Files.writeString(filePath, jsonData);
        } catch (IOException e) {
            System.err.println("Error storing data, data may not be saved: " + e.getMessage());
        }
    }

    /**
     * Returns a list of timestamps for all available historical versions.
     *
     * @return list of version identifiers, empty if history is unavailable
     */
    public List<String> getHistoryVersions() {
        if (historyManager == null) {
            System.err.println("HistoryManager is not initialized.");
            return List.of();
        }
        try {
            return historyManager.listVersions();
        } catch (IOException e) {
            System.err.println("Error listing history versions: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Retrieves a historical version by its index in the list (0 = newest).
     *
     * @param index the index of the version to retrieve
     * @return the DeckManager from that version, or empty if retrieval fails
     */
    public DeckManager retrieveHistoryByIndex(int index) {
        if (historyManager == null) {
            System.err.println("HistoryManager is not initialized.");
            return new DeckManager();
        }
        try {
            return historyManager.retrieveByIndex(index);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return new DeckManager();
        } catch (IOException e) {
            System.err.println("Error retrieving historical version: " + e.getMessage());
            return new DeckManager();
        }
    }

    /**
     * Retrieves a historical version by its exact timestamp.
     *
     * @param timestamp the version identifier (format: yyyyMMdd_HHmmss)
     * @return the DeckManager from that version, or empty if retrieval fails
     */
    public DeckManager retrieveHistoryByTimestamp(String timestamp) {
        if (historyManager == null) {
            System.err.println("HistoryManager is not initialized.");
            return new DeckManager();
        }
        try {
            return historyManager.retrieveByTime(timestamp);
        } catch (IOException e) {
            System.err.println("Error retrieving historical version: " + e.getMessage());
            return new DeckManager();
        }
    }

    /**
     * Moves all historical files to the waste directory.
     */
    public void cleanAllHistory() {
        if (historyManager == null) {
            System.err.println("HistoryManager is not initialized.");
            return;
        }
        try {
            historyManager.deleteAllHistory();
        } catch (IOException e) {
            System.err.println("Error cleaning all history: " + e.getMessage());
        }
    }

    /**
     * Moves selected historical files to the waste directory.
     *
     * @param timestamps the list of version identifiers to delete
     */
    public void cleanSelectHistory(List<String> timestamps) {
        if (historyManager == null) {
            System.err.println("HistoryManager is not initialized.");
            return;
        }
        try {
            historyManager.deleteSelectHistory(timestamps);
        } catch (IOException e) {
            System.err.println("Error cleaning selected history: " + e.getMessage());
        }
    }

    // Private helper methods

    private String serialise(DeckManager list) {
        return gson.toJson(list);
    }

    private DeckManager parse(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new DeckManager();
        }
        try {
            DeckManager manager = gson.fromJson(data, DeckManager.class);
            removeDuplicateDecks(manager);
            return manager;
        } catch (com.google.gson.JsonSyntaxException e) {
            System.err.println("Warning: Encountered invalid JSON data in main file.");
            return null;
        }
    }

    private DeckManager restoreFromLatestHistory() {
        if (historyManager == null) {
            System.err.println("Cannot restore: HistoryManager is not initialized.");
            return new DeckManager();
        }

        try {
            List<String> versions = historyManager.listVersions();
            if (versions.isEmpty()) {
                System.err.println("No historical versions available for recovery.");
                return new DeckManager();
            }

            System.err.println("Found " + versions.size() + " historical versions. Attempting to restore from latest: " + versions.get(0));
            DeckManager restored = historyManager.retrieveByIndex(0);
            removeDuplicateDecks(restored);
            System.err.println("Successfully restored data from historical version: " + versions.get(0));
            return restored;

        } catch (IOException e) {
            System.err.println("Failed to restore from historical version: " + e.getMessage());
            return new DeckManager();
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to restore from historical version: " + e.getMessage());
            return new DeckManager();
        }
    }

    private void removeDuplicateDecks(DeckManager manager) {
        try {
            java.lang.reflect.Field deckMapField = DeckManager.class.getDeclaredField("deckMap");
            deckMapField.setAccessible(true);

            @SuppressWarnings("unchecked")
            java.util.HashMap<String, seedu.flashcli.deck.Deck> deckMap =
                    (java.util.HashMap<String, seedu.flashcli.deck.Deck>) deckMapField.get(manager);

            if (deckMap == null) {
                return;
            }
            int originalSize = deckMap.size();

            java.util.HashMap<String, seedu.flashcli.deck.Deck> uniqueMap = new java.util.HashMap<>();
            java.util.HashSet<String> duplicateNames = new java.util.HashSet<>();

            for (java.util.Map.Entry<String, seedu.flashcli.deck.Deck> entry : deckMap.entrySet()) {
                String deckName = entry.getKey();
                seedu.flashcli.deck.Deck deck = entry.getValue();

                if (!uniqueMap.containsKey(deckName)) {
                    uniqueMap.put(deckName, deck);
                } else {
                    duplicateNames.add(deckName);
                    System.err.println("Warning: Found duplicate deck '" + deckName +
                            "'. Keeping the first occurrence.");
                }
            }
            if (duplicateNames.size() > 0) {
                deckMapField.set(manager, uniqueMap);
                System.err.println("Removed " + duplicateNames.size() +
                        " duplicate deck(s) during loading.");
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Warning: Could not check for duplicate decks: " + e.getMessage());
        }
    }
}
