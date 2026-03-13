package seedu.flashcli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import seedu.flashcli.deck.DeckManager;

public class Storage {
    private final Path filePath;
    private final Gson gson;
    private HistoryManager historyManager;


    public Storage(String storageFilePath){
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



    public DeckManager load(){
        if (!Files.exists(filePath)) {
            return new DeckManager();
        }
        try {
            String jsonData = Files.readString(filePath);
            return parse(jsonData);
        } catch (IOException e) {
            System.err.println("Error occur in reading the file: " + e.getMessage());
            return new DeckManager();
        }
    }


    public void save(DeckManager list){
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
     * Returns an empty list if history is not available or an error occurs.
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
     * Returns an empty DeckManager if the index is invalid or an error occurs.
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
     * Retrieves a historical version by its exact timestamp (format: yyyyMMdd_HHmmss).
     * Returns an empty DeckManager if the timestamp is not found or an error occurs.
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
     * Prints a warning if the operation fails.
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
     * Moves selected historical files (identified by timestamps) to the waste directory.
     * Prints a warning if the operation fails.
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




    private String serialise(DeckManager list){
        return gson.toJson(list);
    }

    private DeckManager parse(String data) {
        if (data == null || data.trim().isEmpty()) {
            return new DeckManager();
        }
        try {
            return gson.fromJson(data, DeckManager.class);
        } catch (com.google.gson.JsonSyntaxException e) {
            // Handles corrupted or invalid JSON format gracefully.
            // Returns an empty DeckManager instead of propagating the exception.
            System.err.println("Warning: Encountered invalid JSON data. Returning empty DeckManager.");
            return new DeckManager();
        }
    }
}
