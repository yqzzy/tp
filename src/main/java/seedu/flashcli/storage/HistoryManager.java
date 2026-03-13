package seedu.flashcli.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import seedu.flashcli.deck.DeckManager;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages historical versions of data files.
 * Provides functionality to save, retrieve, and clean up historical versions.
 */
public class HistoryManager {
    private final Path dataDir;
    private final Path historyDir;
    private final Path wasteDir;
    private final String baseFileName;
    private final Gson gson;
    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Constructs a HistoryManager for the given data directory and base file name.
     *
     * @param dataDirectoryPath The directory containing the main data file.
     * @param baseFileName      The base name of the data file (without extension).
     * @throws IOException If required directories cannot be created.
     */
    public HistoryManager(String dataDirectoryPath, String baseFileName) throws IOException {
        this.dataDir = Paths.get(dataDirectoryPath);
        this.historyDir = dataDir.resolve("history");
        this.wasteDir = dataDir.resolve("waste");
        this.baseFileName = baseFileName;
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Ensure required directories exist
        Files.createDirectories(historyDir);
        Files.createDirectories(wasteDir);
    }

    /**
     * Saves a snapshot of the current data as a historical version.
     * The historical file is named as: {baseFileName}_{timestamp}.json
     *
     * @param currentData The current DeckManager data to archive.
     * @throws IOException If the historical file cannot be written.
     */
    public void saveVersion(DeckManager currentData) throws IOException {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        String historyFileName = String.format("%s_%s.json", baseFileName, timestamp);
        Path historyFile = historyDir.resolve(historyFileName);

        String jsonData = gson.toJson(currentData);
        Files.writeString(historyFile, jsonData);
    }

    /**
     * Lists all available historical versions, sorted by timestamp (newest first).
     *
     * @return A list of timestamps identifying each historical version.
     * @throws IOException If the history directory cannot be read.
     */
    public List<String> listVersions() throws IOException {
        List<Path> historyFiles = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(historyDir, "*.json")) {
            for (Path entry : stream) {
                historyFiles.add(entry);
            }
        }

        // Sort by filename (which contains timestamp) in descending order
        return historyFiles.stream()
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.startsWith(baseFileName + "_"))
                .map(name -> name.replace(baseFileName + "_", "").replace(".json", ""))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a historical version by its index in the list (0 = newest).
     *
     * @param index The index of the version to retrieve.
     * @return The DeckManager data from that historical version.
     * @throws IOException If the file cannot be read.
     * @throws IllegalArgumentException If the index is out of bounds.
     */
    public DeckManager retrieveByIndex(int index) throws IOException {
        List<String> versions = listVersions();
        if (index < 0 || index >= versions.size()) {
            throw new IllegalArgumentException(
                    String.format("Invalid history index. Must be between 0 and %d.", versions.size() - 1)
            );
        }
        String targetTimestamp = versions.get(index);
        return retrieveByTime(targetTimestamp);
    }

    /**
     * Retrieves a historical version by its exact timestamp.
     *
     * @param timestamp The timestamp of the version to retrieve (format: yyyyMMdd_HHmmss).
     * @return The DeckManager data from that historical version.
     * @throws IOException If the file cannot be read or does not exist.
     */
    public DeckManager retrieveByTime(String timestamp) throws IOException {
        String historyFileName = String.format("%s_%s.json", baseFileName, timestamp);
        Path historyFile = historyDir.resolve(historyFileName);

        if (!Files.exists(historyFile)) {
            throw new IOException("Historical version not found: " + timestamp);
        }

        String jsonData = Files.readString(historyFile);
        return parseHistoricalData(jsonData);
    }

    /**
     * Moves all historical files to the waste directory.
     *
     * @throws IOException If any file cannot be moved.
     */
    public void deleteAllHistory() throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(historyDir, "*.json")) {
            for (Path historyFile : stream) {
                Path wasteFile = wasteDir.resolve(historyFile.getFileName());
                Files.move(historyFile, wasteFile);
            }
        }
    }

    /**
     * Moves selected historical files (identified by timestamps) to the waste directory.
     *
     * @param timestamps The timestamps of the versions to delete.
     * @throws IOException If any file cannot be moved or a timestamp is invalid.
     */
    public void deleteSelectHistory(List<String> timestamps) throws IOException {
        for (String timestamp : timestamps) {
            String historyFileName = String.format("%s_%s.json", baseFileName, timestamp);
            Path historyFile = historyDir.resolve(historyFileName);
            Path wasteFile = wasteDir.resolve(historyFileName);

            if (!Files.exists(historyFile)) {
                throw new IOException("Historical version not found: " + timestamp);
            }
            Files.move(historyFile, wasteFile);
        }
    }

    /**
     * Parses JSON data from a historical file into a DeckManager object.
     * Returns an empty DeckManager if the JSON is corrupted or empty.
     */
    private DeckManager parseHistoricalData(String jsonData) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            return new DeckManager();
        }
        try {
            return gson.fromJson(jsonData, DeckManager.class);
        } catch (JsonSyntaxException e) {
            System.err.println("Warning: Corrupted JSON in historical file. Returning empty DeckManager.");
            return new DeckManager();
        }
    }
}