package seedu.flashcli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import seedu.flashcli.deck.DeckManager;

public class Storage {
    private final Path filePath;
    private final Gson gson;


    public Storage(String storageFilePath){
        filePath = Paths.get(storageFilePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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
