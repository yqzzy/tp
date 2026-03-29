# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

FlashCLI is organised into six distinct layers, each with a clearly defined responsibility.
The architecture diagram below shows the relationships between all major classes.

![Architecture Diagram](diagrams/architecture.png)

- **Entry point** - `FlashCLI` initialises the application, owns the `DeckManager` and `Storage`
  instances, and drives the main input loop.
- **UI** - `Ui` handles all terminal output, keeping display logic separate from business logic.
- **Parser** - `Parser` and `ArgumentExtractor` translate raw user input into typed `Command`
  objects, throwing `FlashException` on any invalid input.
- **Command** - The `Command` interface defines a single `execute()` method. Each concrete
  subclass (e.g. `AddCardCommand`, `StudyCommand`) encapsulates the logic for one user action.
- **Deck** - `DeckManager` owns a collection of `Deck` objects, each of which contains
  zero or more `Card` objects. This layer holds all flashcard data at runtime.
- **Study** - `StudyCommand` delegates to `SessionManager`, which manages a single active
  `StudySession`. Cards are ordered by ascending confidence level so weaker cards are drilled first.
- **Storage** - `Storage` persists the full `DeckManager` state to `data/storage.json` after
  every command. `HistoryManager` maintains versioned snapshots in `data/history/`.
- **Exception** - `FlashException` wraps an `ErrorType` enum value, giving every error a
  consistent message and a single catch point in `FlashCLI.executeCommand()`.
### Parser Module

The Parser component is responsible for converting raw user input into executable `Command` objects.
It is designed as a `<<Facade>>`, exposing a single static entry point `Parser.parse(userInput)`
that hides all internal parsing complexity from the rest of the application.

#### Class Structure

The diagram below shows the classes in the Parser component and how they interact.

![Parser Class Diagram](diagrams/parser_class.png)

**Key classes:**
- `Parser` - facade that validates input and dispatches to the correct parse helper
- `ArgumentExtractor` - handles all prefix-based argument extraction and validation
- `AddCardArgs`, `DeleteCardArgs`, `DeckArgs` - immutable data holders for parsed arguments
- `Command` - interface implemented by all command objects returned by the parser

#### Parse Flow

The sequence diagram below shows the high-level flow when `FlashCLI` calls `Parser.parse(userInput)`.

![Parser Sequence Diagram](diagrams/parser_sequence.png)

1. `validateInput()` checks that the input is non-null and non-blank
2. `validateCommandName()` checks the command keyword against the list of valid commands
3. If either check fails, a `FlashException` is thrown back to `FlashCLI`
4. Otherwise, the `dispatch` interaction (detailed below) is triggered

#### Dispatch

The diagram below shows what happens inside the `dispatch` ref frame.

![Parser Dispatch Diagram](diagrams/parser_dispatch.png)

`Parser` routes to one of several private helpers (e.g. `parseAddCardCommand`, `requireEmpty`).
For commands that require arguments, the helper delegates to `ArgumentExtractor.parse*Args(...)`,
which validates prefixes, validates their order, extracts the values, and returns a typed args object.
`Parser` then constructs and returns the appropriate `Command`.

### Parser Design Rationale

The `Parser` and `ArgumentExtractor` classes were designed with the following principles in mind:

* **Single Responsibility Principle (SRP):** `Parser` focuses solely on converting raw user input
  into a typed `Command` object. It has no knowledge of how `Ui` displays output, how `DeckManager`
  stores data, or how `Storage` persists state. `ArgumentExtractor` is further split out to handle
  all prefix validation and value extraction, keeping `Parser` itself thin. This separation makes
  both classes independently testable and reusable.

* **Defensive Programming:** `Parser` is the application's first line of defence against malformed
  input. It validates that input is non-blank, that the command keyword is recognised, that all
  required prefixes are present exactly once, and that prefixes appear in the correct order - all
  before any command logic executes. Any violation throws a `FlashException` immediately. This means
  every `Command` object that reaches `FlashCLI.executeCommand()` is guaranteed to be well-formed,
  and no downstream class needs to re-validate its inputs.

* **Stateless Utility:** `Parser` and `ArgumentExtractor` are implemented as stateless classes with
  a `private` constructor and all-`static` methods. There is no need to instantiate either class,
  which simplifies the design and makes the parsing pipeline easy to reason about - given the same
  input string, `Parser.parse()` always produces the same `Command`.

### Storage

The `Storage` and `HistoryManager` components are responsible for saving the application's data (flashcards and decks) to disk and loading it back. A key enhancement is the integrated **version control system**, which automatically creates a historical backup every time data is saved, allowing users to recover from accidental data loss or corruption.

#### Class Structure

The diagram below illustrates the relationship between the main `Storage` class and its helper, the `HistoryManager`.

![Storage Class Diagram](diagrams/storage_class.png)
*Diagram: Shows the composition where `Storage` owns one `HistoryManager`. Lists core public methods for both classes.*

**Key Classes:**
*   `Storage` - The primary facade for data persistence. It manages the main data file (e.g., `flashcards.json`) and delegates historical versioning operations to the `HistoryManager`.
*   `HistoryManager` - A dedicated class that manages the lifecycle of historical data snapshots, following the **Single Responsibility Principle**. It handles saving, listing, retrieving, and cleaning historical versions in the `./data/history/` and `./data/waste/` directories.

#### Save & Load Flow with Auto-Backup

The sequence diagram below shows the enhanced flow when the application saves data. The critical addition is the automatic creation of a historical version **before** the new data overwrites the existing file.

![Storage Save Sequence](diagrams/storage_sequence.png)
*Diagram: Illustrates the sequence of `Command` -> `Storage.save()` -> `Storage.load()` (to get current data) -> `HistoryManager.saveVersion()` (for backup) -> write to main file.*

1.  A `Command` (e.g., `AddCardCommand`) calls `storage.save(currentDeckManager)` upon successful execution.
2.  `Storage` checks if the main data file exists and if `HistoryManager` is initialized.
3.  If conditions are met, `Storage` loads the current data from disk (`currentDataOnDisk`).
4.  `Storage` delegates to `historyManager.saveVersion(currentDataOnDisk)`. The `HistoryManager` generates a new timestamped file (e.g., `flashcards_20260317_001.json`) in the `./data/history/` directory.
5.  Finally, `Storage` serializes the new `currentDeckManager` and writes it to the main data file, completing the operation.

#### Design Decisions & Implementation

**1. Historical Version Naming and Storage**
*   **Decision:** Historical files are named using a `{basename}_{date}_{sequence}.json` pattern (e.g., `flashcards_20260317_001.json`). Files are stored in `./data/history/` and moved to `./data/waste/` upon deletion.
*   **Rationale:** The `date_sequence` format is more human-readable and organized than a pure epoch timestamp or a long datetime string (`yyyyMMdd_HHmmss`). The date prefix allows for potential future cleanup policies (e.g., keep only last 7 days), and the auto-incrementing 3-digit sequence number guarantees uniqueness within the same day.
*   **Alternative Considered:** Using the exact save time (`yyyyMMdd_HHmmss`). This was rejected as it is less readable for the purpose of manual inspection and doesn't as naturally prevent collisions if multiple saves occur within the same second.

**2. The `waste` Directory and Safe Deletion**
*   **Decision:** Historical versions are "deleted" by being moved to a `./data/waste/` subdirectory, not permanently erased from disk.
*   **Rationale:** This acts as a second-level safety net or a "recycle bin". If a user accidentally cleans the history via the application, the files are still physically recoverable by a system administrator or advanced user from the `waste` directory, preventing total data loss from a UI mistake.
*   **Implementation:** The `deleteAllHistory()` and `deleteSelectHistory()` methods use `Files.move()` with the `StandardCopyOption.REPLACE_EXISTING` flag, ensuring the operation succeeds even if a file with the same name already exists in the `waste` directory.

**3. Loose Coupling and Graceful Degradation**
*   **Decision:** The `HistoryManager` is initialized in the `Storage` constructor. If initialization fails (e.g., due to insufficient file permissions), it is set to `null`, and all history-related features are silently disabled.
*   **Rationale:** This ensures the **core save/load functionality remains available** even if the versioning enhancement fails. The application should not crash because an optional backup feature cannot be initialized. All public history proxy methods in `Storage` (like `getHistoryVersions()`, `cleanAllHistory()`) check for a `null` `HistoryManager` and fail gracefully with a warning message to `System.err`.
*   **Alternative Considered:** Making `HistoryManager` a mandatory component and throwing an exception from the `Storage` constructor if it fails to initialize. This was rejected for being too brittle, as versioning is a non-core enhancement to the primary data persistence responsibility.

**4. Robust JSON Parsing with Gson**
*   **Decision:** Using the Gson library for JSON serialization/deserialization. The `parse()` method in `Storage` and `parseHistoricalData()` in `HistoryManager` catch `JsonSyntaxException`.
*   **Rationale:** JSON is a human-readable, debuggable format. Gson handles the complexity of serializing the entire `DeckManager` object graph. Catching `JsonSyntaxException` is crucial for robustness; if the data file becomes corrupted (e.g., edited manually and saved incorrectly), the application will log a warning, return an empty `DeckManager`, and continue running, rather than crashing on startup.
*   **Error Handling:** Upon catching a `JsonSyntaxException`, the methods print a warning to `System.err` and return a new, empty `DeckManager` object. This provides a clear error signal in the console while allowing the user to continue using the application with a blank state.

## Product scope
### Target user profile

{Describe the target user profile}

### Value proposition

{Describe the value proposition: what problem does it solve?}

## User Stories

## User Stories

| Version | As a ...                    | I want to ...                                        | So that I can ...                                  |
|---------|-----------------------------|------------------------------------------------------|----------------------------------------------------|
| v1.0    | Student starting revision   | create subjects for my modules                       | my flashcards are clearly separated                |
| v1.0    | Student starting revision   | see an overview of all my subjects                   | I know what I am currently revising                |
| v1.0    | Student starting revision   | start with a clean revision space                    | I can organise my own material from scratch        |
| v1.0    | Student learning new content| quickly add a question and answer                    | I can capture important points while studying      |
| v1.0    | Student starting revision   | see how many flashcards I have under each subject    | I can gauge revision workload                      |
| v1.0    | Student                     | have the app work without internet access            | I can revise anywhere                              |
| v1.0    | Student revising for an exam| test myself using my flashcards                      | I can practise active recall                       |
| v1.0    | Student revising for an exam| see questions before answers                         | I am forced to think before checking               |
| v1.0    | Student revising for an exam| reveal answers only when I choose to                 | I can assess my recall honestly                    |
| v1.0    | Student                     | focus my revision on one subject at a time           | I am not overwhelmed                               |
| v1.0    | Student                     | revise flashcards quickly without leaving my CLI workflow | I maintain my productivity environment        |
| v1.0    | Student                     | have my data stored locally                          | I remain in control of my revision material        |
| v1.0    | Student learning new content| delete flashcards that are no longer useful          | outdated content does not distract me              |
| v2.0    | Student revising for an exam| quiz myself with a random subset of the cards        | I can vary my practice                             |
| v2.0    | Student revising for an exam| revisit flashcards I struggled with                  | I can focus on weak areas                          |
| v2.0    | Student learning new content| edit flashcards                                      | I can refine wording as my understanding improves  |
| v2.0    | Student starting revision   | mark my confidence when inputting flashcards         | I can see how my confidence level changed during revision |
| v2.0    | Student revising for an exam| see statistics about my flashcard collection         | I can track my study progress                      |

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
