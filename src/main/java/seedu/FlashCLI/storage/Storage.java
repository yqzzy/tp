package seedu.FlashCLI.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import seedu.FlashCLI.deck.DeckManager;

public class Storage {
    private final Path filePath;
    private final Gson gson;

    /**
     * Create a storage class
     * @param Storage_file_Path, prefer to store in path "data/flashcards.json"
     */
    public Storage(String Storage_file_Path){
        filePath = Paths.get(Storage_file_Path);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * read all stored data
     * @return a DeckManager item with all data
     */
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

    /**
     * used to store all data in DeckManager
     * @param list, the DeckManager item
     */
    public void save(DeckManager list){
        try {
            Files.createDirectories(filePath.getParent());
            String jsonData = serialise(list);
            Files.writeString(filePath, jsonData);
        } catch (IOException e) {
            System.err.println("Error storing data, data may not be saved: " + e.getMessage());
        }
    }

    private String serialise(DeckManager list){
        return gson.toJson(list);
    }

    private DeckManager parse(String data){
        if (data == null || data.trim().isEmpty()) {
            return new DeckManager();
        }
        return gson.fromJson(data, DeckManager.class);
    }
}
