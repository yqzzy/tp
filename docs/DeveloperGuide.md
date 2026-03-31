# Developer Guide

## Acknowledgements
FlashCLI was built from scratch as an original project. The following sources provided
inspiration for certain features and design decisions:

**Anki** ([https://apps.ankiweb.net](https://apps.ankiweb.net))
- The confidence-based card ordering feature in FlashCLI's Study module was inspired by
  Anki's spaced repetition system, where cards are scheduled based on how well the user
  knows them. Our implementation is entirely new and uses a simpler ascending confidence
  sort rather than Anki's full SM-2 spaced repetition algorithm.
- The concept of storing a per-card confidence/difficulty rating that persists across
  sessions was also drawn from Anki's card rating system (Again / Hard / Good / Easy).
  Our implementation uses a 1–5 integer scale instead.

**Quizlet** ([https://quizlet.com](https://quizlet.com))
- The basic flashcard deck-and-card data model (decks containing question-answer card pairs)
  was inspired by Quizlet's sets and terms structure. Our implementation is entirely new.
- The study session workflow of showing a question, waiting for user input, then revealing
  the answer, was inspired by Quizlet's Learn mode. Our implementation is entirely new
  and adapted for a CLI environment.

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

## Parser
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

## Storage

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

## Study

The Study package implements FlashCLI's active-recall workflow: a user selects a deck,
studies cards one at a time in order of ascending confidence, rates their confidence after
each answer, and receives a session summary when they finish or quit.

The component consists of three collaborating classes:

| Class | Responsibility |
|---|---|
| `StudyCommand` | Entry point — bridges the Command layer to the study subsystem and owns the input loop |
| `SessionManager` | Owns and lifecycle-manages the single active `StudySession` |
| `StudySession` | Sorts cards by confidence on construction, tracks position, provides card access |

#### Class Structure

The diagram below shows the Study package and its relationships to the surrounding layers.

![Study Class Diagram](diagrams/study_class.png)

Key design decisions visible in the diagram:

- `SessionManager` **composes** `StudySession` (0..1) — at most one session is active at any
  time. Calling `startSession()` while a session is already active throws
  `SESSION_ALREADY_IN_PROGRESS`.
- `StudySession` holds a **final** reference to a **sorted copy** of the deck — the original
  deck's order is preserved; only the session's internal view is sorted by confidence.
- `StudyCommand` depends on `SessionManager`, `DeckManager`, and `Ui` but **not** on
  `StudySession` directly. All session interaction is mediated through `SessionManager`.
- `Card` carries a `confidenceLevel` field (default `0`) which `StudyCommand` updates after
  each answer via `card.setConfidenceLevel(confidence)`. This persists to `Storage` and
  influences card ordering in future sessions.

#### How a Study Session Works

The sequence diagram below shows the end-to-end flow from `StudyCommand.execute()` being
called to the session summary being printed.

![Study Sequence Diagram](diagrams/study_sequence.png)

The flow has three phases:

**1. Setup**

`StudyCommand` retrieves the deck from `DeckManager`. If the deck is empty,
`Ui.showEmptyDeck()` is called and the command returns immediately. Otherwise,
`SessionManager.startSession(deck)` is called, which creates a new `StudySession`.
On construction, `StudySession` makes a sorted copy of the deck's cards, ordering them
by `confidenceLevel` ascending so the cards the user is least confident about are drilled
first. The first question is shown immediately before entering the loop.

**2. Loop**

On each iteration, the user presses Enter to reveal the current card's answer.
After the answer is shown, `StudyCommand` prompts the user for a confidence rating (1–5)
via `Ui.showConfidencePrompt()`. The rating is validated in an inner loop — non-integer
input and out-of-range values show an error and re-prompt; typing `q` during the confidence
prompt exits the session immediately. Once a valid rating is received, the card's
`confidenceLevel` is updated. `SessionManager.nextCard()` then advances the index.
If the end of the deck is reached, the session ends automatically.

**3. Teardown**

`SessionManager.finishSession()` delegates to `StudySession.finish()`, which calculates
cards reviewed (capped at deck size), sets `currentIndex = -1` as a consumed sentinel,
and returns the count. `Ui` prints the session summary.

#### Confidence-Based Card Ordering

The diagram below shows what happens inside `StudySession`'s constructor when
`startSession(deck)` is called.

![Study Sorting Diagram](diagrams/study_sorting.png)

`StudySession` does not modify the original `Deck` object. Instead it:

1. Retrieves the card list via `deck.listCards()`
2. Sorts the list in-place by `getConfidenceLevel()` ascending using
   `Comparator.comparing(Card::getConfidenceLevel)`
3. Creates a new `Deck` with the same name and assigns the sorted list via `setCards()`
4. Stores this sorted copy as `this.deck`

This means cards the user rated lowest in previous sessions appear first in the next
session, implementing a lightweight spaced-repetition policy without requiring any
additional data structures or scheduling algorithms.

#### Defensive Coding

The Study package applies defensive programming at every public boundary.

**Guards and assertions applied:**

| Method | Guard type | What it checks |
|---|---|---|
| `StudySession(deck)` | Explicit null check | `deck != null`; throws `IllegalArgumentException` |
| `StudySession(deck)` | Post-condition assert | `currentIndex == 0` after construction |
| `startSession(deck)` | Explicit null check | `deck != null`; throws `INVALID_ARGUMENTS` |
| `startSession(deck)` | State check | No active session; throws `SESSION_ALREADY_IN_PROGRESS` |
| `startSession(deck)` | Post-condition assert | `hasActiveSession()` and deck name matches |
| `getCurrentCard()` | Bounds check | `0 <= currentIndex < deck.getSize()`; throws `CARD_NOT_FOUND` |
| `getCurrentCard()` | Post-condition assert | returned `card != null` |
| `nextCard()` | Pre-condition assert | `currentIndex >= 0` (session not already consumed) |
| `nextCard()` | Post-condition assert | index advanced by exactly 1 |
| `finish()` | Pre-condition assert | `currentIndex >= 0` (not called twice) |
| `finish()` | Invariant assert | `0 <= finalCount <= deck.getSize()` |
| `finishSession()` | Post-condition assert | `activeSession == null` after clearing |
| `finishSession()` | Post-condition assert | returned count `>= 0` |

All explicit guards throw `FlashException` (user-facing errors) or `IllegalArgumentException`
(programmer errors). Assertions catch logic errors during development when the JVM is run
with `-ea` and have no effect in production.

Logging is applied at three levels across `SessionManager` and `StudySession`:
- `FINE` — normal control flow (method entry, index values)
- `WARNING` — bad inputs or unexpected state (null deck, no active session, out-of-bounds)
- `INFO` — significant state changes (session started, session finished with card count)

#### Design Rationale

**Why separate `SessionManager` from `StudySession`?**

An earlier design had `StudyCommand` interact with `StudySession` directly. This was
rejected because it forced `StudyCommand` to manage session lifecycle — checking for null,
clearing state after finish — violating the Single Responsibility Principle. With
`SessionManager` as an intermediary:

- `StudyCommand` only calls high-level operations: `startSession`, `getCurrentCard`,
  `nextCard`, `finishSession`.
- `StudySession` only manages index arithmetic and card retrieval.
- The confidence-sorting logic is entirely contained in `StudySession`'s constructor
  and can be changed (e.g., switching to a spaced-repetition algorithm) without touching
  `StudyCommand` or `SessionManager`.

**Why sort a copy of the deck rather than the original?**

Sorting the original deck's `cardList` in `Deck` would change the card order permanently,
affecting `listCards` output and card indices used by `deleteCard` and `editCard`.
Creating a sorted copy in `StudySession` isolates ordering to the study subsystem,
leaving the deck's authoritative order intact.

**Alternatives considered:**

| Alternative | Reason rejected |
|---|---|
| Merge `SessionManager` into `StudyCommand` | Mixes command logic with session lifecycle; harder to unit test |
| Sort cards in `DeckManager` before passing to `StudyCommand` | `DeckManager` should not know about study ordering; violates SRP |
| Use an iterator pattern over `Deck` | Cleaner API but adds infrastructure to the Deck layer unnecessarily |
| Sort the original `cardList` in `Deck` | Corrupts card indices used by other commands |


## Add Card

The `AddCardCommand` feature allows the user to add a new flashcard to an existing deck by
supplying a deck name, question, and answer. This feature spans both the parsing and execution
phases of the application: the `Parser` first converts the raw user input into an
`AddCardCommand`, after which the command executes the business logic and the updated
`DeckManager` state is persisted to storage.

#### Class Structure

The diagram below shows the main classes involved in the `AddCardCommand` feature and their
relationships.

![AddCardCommand Class Diagram](diagrams/add_card_command_class.png)

**Key classes:**

* `Command` - interface that defines the common `execute(deckManager, ui, in)` method
* `AddCardCommand` - concrete command that stores the parsed `deckName`, `question`, and `answer`
  needed to perform the add-card operation
* `AddCardArgs` - temporary parsed data object returned by the parser before the
  `AddCardCommand` is constructed
* `DeckManager` - provides access to the target `Deck`
* `Deck` - owns the list of `Card` objects and performs the actual insertion
* `Ui` - displays the success message after the card is added
* `Storage` - persists the updated `DeckManager` after execution
* `FlashException` - thrown when the specified deck does not exist

The class diagram highlights that `AddCardCommand` is the central coordinator for this feature.
It depends on the parser-side `AddCardArgs` object during construction, and during execution it
interacts with the domain layer (`DeckManager`, `Deck`, `Card`) and the UI layer.

#### Sequence Flow

The sequence diagram below shows the full flow of the `addcard` feature, from user input to
data persistence.

![AddCardCommand Sequence Diagram](diagrams/add_card_sequence.png)

The flow can be divided into three stages:

**1. Parsing**

1. The user enters the `addcard` command into `FlashCLI`
2. `FlashCLI` calls `Parser.parse(userInput)`
3. `Parser` delegates argument extraction to `ArgumentExtractor.parseAddCardArgs(...)`
4. `ArgumentExtractor` validates prefixes and extracts values into an `AddCardArgs` object
5. `Parser` constructs and returns an `AddCardCommand`

At the end of this stage, a fully validated `AddCardCommand` is ready for execution.

**2. Execution**

1. `FlashCLI` calls `AddCardCommand.execute(deckManager, ui, in)`
2. `AddCardCommand` calls `deckManager.getDeck(deckName)`
3. An `alt` branch is evaluated:

  * **If the deck exists:**

    1. A new `Card(question, answer)` is created
    2. `deck.addCard(card)` is called to insert the card
    3. `ui.showAddedCard(card, deckName)` displays a success message
  * **If the deck does not exist:**

    * A `FlashException` is thrown and propagated back to `FlashCLI`

This ensures that invalid operations are rejected early while valid operations modify only the
necessary part of the data model.

**3. Persistence**

After successful execution:

1. `FlashCLI` calls `storage.save(deckManager)`
2. `Storage` writes the updated state to disk (and triggers history backup if enabled)

This guarantees that all changes made by `AddCardCommand` are durable across sessions.

#### Design Rationale

**Encapsulation of Command Logic**

All logic related to adding a card is encapsulated within `AddCardCommand`. This follows the
Command pattern, ensuring that each user action is self-contained and independent. 

**Separation of Concerns**

* `Parser` handles input validation and object construction
* `AddCardCommand` handles business logic
* `DeckManager` and `Deck` handle data storage
* `Ui` handles output formatting
* `Storage` handles persistence

This separation improves modularity and makes each component easier to test and maintain.

**Defensive Programming**

* All inputs are validated during parsing, so `AddCardCommand` receives only valid data
* `deckManager.getDeck(deckName)` returning `null` is explicitly handled
* A `FlashException` is thrown for invalid deck names, ensuring consistent error handling

**Consistency with Other Commands**

`AddCardCommand` follows the same execution pattern as other commands:

1. Retrieve required data from `DeckManager`
2. Perform the operation on the domain model
3. Display output via `Ui`
4. Persist changes via `Storage`

This consistent structure simplifies understanding and extending the system with new commands.

### Additional Design Considerations

**Early Validation via Parser**

All argument validation is performed in the `Parser` and `ArgumentExtractor` before the
`AddCardCommand` is constructed. This ensures that the command operates only on valid,
well-formed data and does not need to handle parsing-related errors.

An alternative approach would be to perform validation inside `AddCardCommand.execute()`,
but this was rejected as it would duplicate validation logic across commands and violate
the Single Responsibility Principle.

---

**Delegation to Domain Classes**

`AddCardCommand` delegates data-related operations to `DeckManager` and `Deck` instead of
directly modifying internal data structures. This ensures that each class maintains clear
ownership of its responsibilities and preserves encapsulation.

---

**Persistence Outside Command**

Data persistence is handled by `FlashCLI` after command execution, rather than within
`AddCardCommand`. This design keeps command classes focused purely on business logic and
avoids coupling them with storage concerns.

This also makes it easier to change the storage implementation in the future without
modifying command logic.

### Future Improvements

**Duplicate Card Detection**

Currently, duplicate cards can be added to a deck. A future enhancement could check for
existing cards with the same question and answer and either prevent duplication or prompt
the user for confirmation.

---

**Richer Card Metadata**

The `Card` model currently stores only a question and answer. It can be extended to include
additional attributes such as tags, enabling more advanced
features like filtering and improved study strategies.

---

**Batch Card Addition**

Support for adding multiple cards in a single command (e.g. via file input) could improve
usability for users importing large sets of flashcards.


## Delete Card

The `DeleteCardCommand` feature allows the user to remove a specific flashcard from a deck
by specifying the deck name and the index of the card. Similar to `AddCardCommand`, this
feature spans both parsing and execution, ensuring that only valid inputs are processed and
that changes are persisted after execution.

#### Class Structure

The diagram below shows the main classes involved in the `DeleteCardCommand` feature and
their relationships.

![DeleteCardCommand Class Diagram](diagrams/delete_card_command_class.png)

**Key classes:**

* `Command` - interface that defines the common `execute(deckManager, ui, in)` method
* `DeleteCardCommand` - concrete command that stores the `deckName` and `cardIndex`
* `DeleteCardArgs` - parsed data object returned by the parser
* `DeckManager` - provides access to the target `Deck`
* `Deck` - manages the list of `Card` objects and performs deletion
* `Ui` - displays the result of the deletion
* `Storage` - persists the updated `DeckManager` after execution
* `FlashException` - thrown for invalid deck or index

#### Sequence Flow

The sequence diagram below shows the full flow of the `deletecard` feature.

![DeleteCardCommand Sequence Diagram](diagrams/delete_card_sequence.png)

The flow can be divided into three stages:

**1. Parsing**

1. The user enters the `deletecard` command into `FlashCLI`
2. `FlashCLI` calls `Parser.parse(userInput)`
3. `Parser` delegates to `ArgumentExtractor.parseDeleteCardArgs(...)`
4. Arguments are validated and returned as a `DeleteCardArgs` object
5. `Parser` constructs and returns a `DeleteCardCommand`

---

**2. Execution**

1. `FlashCLI` calls `DeleteCardCommand.execute(deckManager, ui, in)`
2. `DeleteCardCommand` calls `deckManager.getDeck(deckName)`
3. An `alt` branch is evaluated:

  * **If the deck exists && valid index:**

    1. The command calls `deck.removeCard(cardIndex)`
    2. The removed `Card` is returned
    3. `ui.showDeletedCard(card, deckName)` displays a success message
  * **else:**

    * A `FlashException` is thrown


---

**3. Persistence**

After successful execution:

1. `FlashCLI` calls `storage.save(deckManager)`
2. `Storage` writes the updated state to disk (and triggers history backup if enabled)

This ensures that deleted cards are permanently removed across sessions.

---

#### Design Rationale

**Encapsulation of Command Logic**

All logic related to deleting a card is encapsulated within `DeleteCardCommand`, following
the Command pattern. This ensures that each command is self-contained and independent.

---

**Separation of Concerns**

* `Parser` handles input validation and object construction
* `DeleteCardCommand` handles business logic
* `DeckManager` and `Deck` handle data storage
* `Ui` handles output formatting
* `Storage` handles persistence

This separation improves modularity and maintainability.

---

**Defensive Programming**

* Inputs are validated during parsing to ensure correct format
* Invalid deck names and indices are handled via `FlashException`

---

**Consistency with Other Commands**

`DeleteCardCommand` follows the same execution pattern as other commands:

1. Retrieve the deck
2. Perform the operation on the domain model
3. Display output via `Ui`
4. Persist changes via `Storage`

This consistency simplifies understanding and extension of the system.

---

#### Additional Design Considerations

**Early Validation via Parser**

All argument validation is performed before constructing the command. This ensures that
`DeleteCardCommand` operates only on valid data and does not duplicate parsing logic.

---

**Delegation to Domain Classes**

The command delegates deletion to the `Deck` class rather than directly modifying internal
data structures, preserving encapsulation and clear ownership.

---

**Persistence Outside Command**

Persistence is handled by `FlashCLI`, not the command itself. This avoids coupling command
logic with storage concerns and improves flexibility.

---

#### Alternatives Considered

**Direct Data Manipulation in Command**

Allowing the command to directly manipulate internal card lists was rejected as it breaks
encapsulation and increases coupling.

---

**Validation Inside Command**

Performing validation inside `DeleteCardCommand` was rejected because it duplicates logic
handled by the parser and reduces code clarity.

---

**Storage Handling Inside Command**

Calling `storage.save()` inside the command was rejected as it mixes concerns and makes
commands harder to test.

---

#### Future Improvements

**Confirmation Before Deletion**

A confirmation step (e.g. `Are you sure?`) could be added to prevent accidental deletions.

---

**Bulk Deletion Support**

Support for deleting multiple cards in a single command could improve efficiency for users
managing large decks.

---

**Soft Deletion**

Instead of permanently removing cards, a soft-delete mechanism could allow recovery of
recently deleted cards.

---

**Improved Index Handling**

Support for more flexible indexing (e.g. ranges or filtering) could make deletion more
powerful and user-friendly.




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

1. **Usability**

  * The application should be usable entirely via a Command Line Interface (CLI) with clear and consistent command formats.
  * Error messages should be descriptive and guide the user to correct input formats.

2. **Performance**

  * The application should respond to user commands within 1 second under normal usage.
  * Operations such as adding, deleting, and listing cards should remain efficient for decks with up to 1,000 cards.

3. **Reliability**

  * The application should not crash due to invalid user input; all errors must be handled using `FlashException`.
  * Data should be automatically saved after every successful command execution.

4. **Data Persistence**

  * All user data must be stored locally and persist across sessions.
  * Historical backups should be created automatically before overwriting existing data.

5. **Portability**

  * The application should run on any system with Java 17 installed without requiring additional setup.

6. **Maintainability**

  * The codebase should follow modular design principles (e.g., separation of concerns across Parser, Command, UI, Storage).
  * Components should be independently testable.

7. **Scalability**

  * The system should handle multiple decks and a moderate number of cards per deck without significant performance degradation.

8. **Security**

  * User data should be stored locally and not transmitted over any network.
  * The application should not require internet access to function.

9. **Compatibility**

  * The application should be compatible with major operating systems (Windows, macOS, Linux) that support Java 17.


## Glossary

* **Deck** – A collection of flashcards grouped under a specific topic or subject.
* **Card** – A single flashcard containing a question and an answer.
* **DeckManager** – The component that manages all decks and provides access to them at runtime.
* **Command** – An executable action triggered by user input (e.g., add card, delete card).
* **Parser** – Converts raw user input into a structured `Command` object.
* **Ui** – Handles all output displayed to the user in the CLI.
* **Storage** – Responsible for saving and loading application data to/from disk.
* **Study Session** – A session where users review cards in order of increasing difficulty/confidence.
* **Confidence Level** – A numerical rating (e.g., 1–5) indicating how well a user knows a card.
* **FlashException** – A custom exception used to handle user-facing errors consistently.


## Instructions for Manual Testing

This section provides a step-by-step path for a manual tester to verify the core functionalities of FlashCLI. The instructions are designed to be followed sequentially, building upon the state created by previous steps. All command formats and expected outputs are derived from the current implementation.

**Prerequisites for all tests:**
1.  Ensure you are in the project's root directory.
2.  Compile and run FlashCLI using `` or your preferred method.
3.  The `./data/` directory should be empty or non-existent at the start of testing.

### E.1 Testing Basic Deck Management

#### Creating and Listing Decks
1.  **Creating a deck**
  *   **Command:** `createDeck d/Mathematics`
  *   **Expected Output:** `Deck created successfully: Mathematics`

2.  **Listing all decks**
  *   **Command:** `listDecks`
  *   **Expected Output:** `Here are all the decks you have currently: 1.Mathematics`

3. **Create another deck and verify:**
*   **Command:** `createDeck d/Sci./gradlew runence` and then `listDecks`
*   **Expected Output:** `Here are all the decks you have currently: 1.Mathematics 2.Science`

#### Adding and Managing Cards
1. **Add a card to a deck:**
*   **Command:** `addCard d/Mathematics q/What is 2+2? a/4`
*   **Expected Output:** `Added Card: What is 2+2?  4  to deck Mathematics`

2.  **Add a second card with different content:**
*   **Command:** `addCard d/Mathematics q/What is the capital of France? a/Paris`
*   **Expected Output:** `Added Card: What is the capital of France?  Paris  to deck Mathematics`

3.  **List cards in a deck:**
*   **Command:** `listCards d/Mathematics`
*   **Expected Output:** `Here are all the cards in the deck Mathematics:` + cards

4.  **Edit an existing card:**
*   **Command:** `editCard d/Mathematics i/1 q/What is 5+7? a/12`
*   **Expected Output:** `Edited card in deck Mathematics:` + new card

5.  **Delete a card:**
*   **Command:** `deleteCard d/Mathematics i/2`
*   **Expected Output:** `Delete Card:` + card information + `from deck Deckname`

6.  **Delete a card:**
*   **Command:** `clearDeck d/Mathematics`
*   **Expected Output:** `Cleared deck: Mathematics`

### E.2 Testing Study Sessions

1. **Start a study session with a deck that has cards:**
*   **Command:** `study d/Mathematics`
*   **Expected Output:** Shows the first question with instructions to press Enter for answer or 'q' to quit.

2. **Reveal the answer:**
*   **Command:** Press `Enter` (empty input)
*   **Expected Output:** Shows the answer to the current question.

3. **Rate confidence level:**
*   **Command:** `input 1-5`
*   **Expected Output:**  None (move to next question)

3. **Move to the next card:**
*   **Command:** Press `Enter` again
*   **Expected Output:** Shows the next question or indicates end of deck.

4. **Quit a study session early:**
*   **Command:** Type `q` and press Enter during a study session
*   **Expected Output:** Returns to normal command mode with a summary.

5. **Study an empty deck:**
*   **Command:** `study d/Mathematics`
*   **Expected Output:** Appropriate message indicating the deck has no cards.

### E.3 Testing Data Persistence and Automatic Backup

These tests verify that data is automatically saved and historical versions are created in the background.

1. **Initial data file creation:**
- Start with no `./data/` directory
- Create a deck and add a card
- Check that `./data/flashcards.json` is created

2. **Automatic backup on subsequent saves:**
- Ensure `./data/flashcards.json` exists
- Make another change (add card, edit card, etc.)
- Check that `./data/history/` directory is created
- Verify a backup file exists with format `flashcards_YYYYMMDD_NNN.json`

3. **Data persistence across sessions:**
- Create some decks and cards
- Exit the application using `exit` command
- Restart the application
- Use `listDecks` to verify data was loaded correctly

4. **Handling corrupted data files:**
- Manually edit `./data/flashcards.json` to make it invalid JSON
- Restart the application
- Expected: Application starts without crashing, may show a warning but continues with empty data

### E.4 Testing Error Handling

1. **Unknown command:**
*   **Command:** `invalid commands here`
*   **Expected Output:** `Error msg: Invalid command format. Use "help" to see the list of all commands.`

2. **Missing required prefixes:**
*   **Command:** `addCard q/Question only`
*   **Expected Output:** `Error msg: Deck name is required. Use prefix d/.`

3. **Invalid card index:**
*   **Command:** `deleteCard d/Mathematics i/100`
*   **Expected Output:** `Error msg: Input an integer index number`

4. **Duplicate deck creation:**
*   **Command:** `createDeck d/Mathematics` repeat twice
*   **Expected Output:** `Error msg: A deck with this name already exists. Please choose a unique name.`

5. **Operating on non-existent deck:**
*   **Command:** `addCard d/NonExistent q/Test? a/Test`
*   **Expected Output:** `Error msg: Deck not found. Use listDecks to see available decks.`

### E.5 Testing Utility Commands

1. **Help command:**
*   **Command:** `help`
*   **Expected Output:** Expected: Shows list of available commands with their formats

2. **Exit command:**
*   **Command:** `exit`
*   **Expected Output:** Application closes with goodbye message

### E.6 Recommended Test Sequence

For comprehensive testing, follow this sequence:

1. Start with a clean state (no `./data/` directory)
2. Test deck creation and listing (E.1.1-3)
3. Test card operations (E.1.4-6)
4. Test study sessions (E.2)
5. Test data persistence by exiting and restarting (E.3.3)
6. Test error cases (E.4)
7. Test automatic backup by making multiple changes (E.3.2)

Note: The automatic backup feature (E.3.2) creates historical versions in the background. You can verify this by checking the `./data/history/` directory after making changes to your data.

