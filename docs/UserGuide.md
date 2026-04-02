# FlashCLI User Guide

## Table of Contents

- [Introduction](#introduction)
- [Quick Start](#quick-start)
- [Notes on Command Format](#notes-on-command-format)
- [Features](#features)
   - [Viewing help: `help`](#viewing-help--help)
   - [Creating a deck: `createDeck`](#creating-a-deck--createdeck)
   - [Listing all decks: `listDecks`](#listing-all-decks--listdecks)
   - [Deleting a deck: `deleteDeck`](#deleting-a-deck--deletedeck)
   - [Clearing a deck: `clearDeck`](#clearing-a-deck--cleardeck)
   - [Adding a card: `addCard`](#adding-a-card--addcard)
   - [Listing all cards: `listCards`](#listing-all-cards--listcards)
   - [Deleting a card: `deleteCard`](#deleting-a-card--deletecard)
   - [Editing a card: `editCard`](#editing-a-card--editcard)
   - [Studying a deck: `study`](#studying-a-deck--study)
   - [Exiting the program: `exit`](#exiting-the-program--exit)
   - [Saving data](#saving-data)
- [FAQ](#faq)
- [Command Summary](#command-summary)

---

## Introduction

FlashCLI is a lightweight command-line flashcard application for students who prefer a fast, keyboard-first study workflow. You organise your flashcards into named decks, add question-and-answer cards to each deck, and run interactive study sessions that adapt to your self-reported confidence level - showing the cards you are least confident about first. This guide walks you through how to install and use FlashCLI.

---

## Quick Start

1. **Install Java 17.** FlashCLI requires Java 17 or later. Confirm your version with:
   ```
   java -version
   ```
2. **Download FlashCLI.** Either clone the repository or download the pre-built JAR from the releases page and place it anywhere on your computer.
3. **Open a terminal** at the project root (if you cloned) or the folder containing the JAR.
4. **Run FlashCLI.**
    * **From source:**
        * macOS / Linux: `./gradlew run`
        * Windows: `.\gradlew.bat run`
    * **From JAR:** `java -jar flashcli.jar`

   You should see the welcome message:
   ```
   Welcome to FlashCLI!
   ```
5. Type `help` to see every available command, then start by creating your first deck with `createDeck`.

**Data persistence:** FlashCLI automatically saves all your decks and cards to `data/storage.json` after every command. You do not need to save manually.

---

## Features 


### Viewing help : `help`

Displays a list of possible commands. 

**Format:** `help`

- Takes no arguments.

**Example:**
```
help
```
---

### Creating a deck : `createDeck`
Creates a new, empty deck with the given name.

**Format:** `createDeck d/DECK_NAME`

- `DECK_NAME` must not be blank.
- A deck with that exact name must not already exist.

**Example:**
```
createDeck d/CS2113 Finals
```
---

### Listing all decks : `listDecks`
Displays the names of every deck currently stored.

**Format:** `listDecks`

- Takes no arguments.

**Example:**
```
listDecks
```

---

### Deleting a deck : `deleteDeck`

Permanently removes the named deck and all its cards.

**Format:** `deleteDeck d/DECK_NAME`

**Example:**
```
deleteDeck d/Linear Algebra
```

---

### Clearing a deck : `clearDeck`

Removes all cards from an existing deck without deleting the deck itself.

**Format:** `clearDeck d/DECK_NAME`

**Example:**
```
clearDeck d/CS2113 Finals
```

---

### Adding a card : `addCard`

Adds a new question-and-answer card to an existing deck.

**Format:** `addCard d/DECK_NAME q/QUESTION a/ANSWER`

- Prefixes **must** appear in the order `d/` → `q/` → `a/`.
- Each of `DECK_NAME`, `QUESTION`, and `ANSWER` must be non-blank.
- The new card starts with a confidence level of `0` (unreviewed).

**Example:**
```
addCard d/CS2113 Finals q/What does OOP stand for? a/Object-Oriented Programming
```
---

### Listing all cards : `listCards`

Displays every card in a deck, showing each card's index, question, and answer.

**Format:** `listCards d/DECK_NAME`

**Example:**
```
listCards d/CS2113 Finals
```

---

### Deleting a card : `deleteCard`

Removes a card at a specified position from a deck.

**Format:** `deleteCard d/DECK_NAME i/INDEX`

- `INDEX` is **1-based** (the first card is `i/1`).
- Prefixes must appear in the order `d/` → `i/`.

**Example:**
```
deleteCard d/CS2113 Finals i/2
```

---

### Editing a card : `editCard`

Replaces the question and answer of an existing card.

**Format:** `editCard d/DECK_NAME i/INDEX q/NEW_QUESTION a/NEW_ANSWER`

- Prefixes **must** appear in the order `d/` → `i/` → `q/` → `a/`.
- `INDEX` is **1-based**.
- Both `NEW_QUESTION` and `NEW_ANSWER` must be non-blank.
- The card's confidence level is not changed by this command.

**Example:**
```
editCard d/CS2113 Finals i/1 q/What does OOP stand for? a/Object-Oriented Programming
```

---

### Studying a deck : `study`
Starts an interactive study session for the specified deck.

Format: `study d/DECK_NAME`

- The deck must exist and contain at least one card.
- Cards are shown in order of ascending confidence level — cards you are least confident
  about appear first. Cards that have never been studied (default confidence `0`) always
  appear before any rated card.
- During a session, only the study session controls listed below are valid. Regular
  FlashCLI commands (e.g. `addCard`, `listDecks`) cannot be used mid-session.

**Study session controls:**

| Input | Action |
|-------|--------|
| Enter (empty line) | Reveal the current card's answer |
| `1` – `5` | Submit your confidence rating after the answer is shown |
| `q` | Quit the session early |

**Study session flow:**

1. FlashCLI displays the first card's question.
2. Press **Enter** to reveal the answer.
3. Rate your confidence from **1** (lowest) to **5** (highest) and press **Enter**.
    - `1` — Did not know at all
    - `2` — Barely remembered
    - `3` — Remembered with difficulty
    - `4` — Remembered well
    - `5` — Knew it perfectly
4. FlashCLI advances to the next card. Repeat steps 2–3 until all cards are reviewed
   or you type `q` to quit.
5. At the end of the session, FlashCLI displays the number of cards reviewed.

> **Note:** Your confidence ratings are saved automatically. The next time you study the
> same deck, cards with lower confidence ratings will appear first.

Example:
```
study d/CS2113 Finals
```
```
Q: What does OOP stand for?
(Press Enter to reveal answer, or type 'q' to quit)

A: Object-Oriented Programming
Please rate your confidence (1-5):
4
Q: What is polymorphism?
(Press Enter to reveal answer, or type 'q' to quit)
q
Session ended. Cards reviewed: 1
```

**Quitting mid-session:**

Typing `q` at any point — whether at a question prompt or at the confidence rating prompt —
ends the session immediately and displays how many cards were reviewed.

**Studying an empty deck:**

If you attempt to study a deck that has no cards, FlashCLI will display:
```
_______________________________
Deck is empty. Add cards before studying.
_______________________________
```

---

### Exiting the program : `exit`

---

## FAQ

**Q: How do I transfer my data to another computer?**  
**A**: Copy the `data/storage.json` file from your current FlashCLI folder to the same location on the new computer. Ensure FlashCLI is not running while copying the file.

---

**Q: What happens if I accidentally delete a deck or card?**  
**A**: Deletions are permanent and cannot be undone. You may need to manually restore from a backup of `data/storage.json` if available.

---

**Q: Why does my command not work even though it looks correct?**  
**A**: Ensure that:
- Prefixes are in the correct order (e.g. `d/` → `q/` → `a/`)
- There are no missing prefixes
- Inputs are not blank  
  FlashCLI is strict about command format.

---

**Q: Can I use commands during a study session?**  
**A**: No. Only study session inputs (`Enter`, `1–5`, `q`) are accepted during a study session. Other commands will not work until the session ends.

---

**Q: Why is my deck not appearing after I create it?**  
**A**: Ensure that:
- The deck name is not blank
- A deck with the same name does not already exist  
  If the command fails, FlashCLI will display an error message.

---

**Q: Where is my data stored?**  
**A**: All data is stored locally in `data/storage.json` in the same directory as the application.

---

**Q: Can I edit the storage file manually?**  
**A**: It is not recommended. Incorrect edits may corrupt your data and cause FlashCLI to fail when loading.

---

**Q: What happens if I enter an invalid index for a card?**  
**A**: FlashCLI will display an error message. Ensure the index is within the range shown by `listCards`.

---

**Q: Do I need to save my data manually?**  
**A**: No. FlashCLI automatically saves all changes after every command.

---

**Q: Why are my cards shown in a different order during study?**  
**A**: Cards are automatically ordered by confidence level (lowest first) to help you focus on weaker areas first.

---

## Command Summary

| Command | Format | Example |
|---------|--------|---------|
| `help` | `help` | `help` |
| `createDeck` | `createDeck d/<DECK_NAME>` | `createDeck d/CS2113 Finals` |
| `listDecks` | `listDecks` | `listDecks` |
| `clearDeck` | `clearDeck d/<DECK_NAME>` | `clearDeck d/CS2113 Finals` |
| `deleteDeck` | `deleteDeck d/<DECK_NAME>` | `deleteDeck d/Linear Algebra` |
| `addCard` | `addCard d/<DECK_NAME> q/<QUESTION> a/<ANSWER>` | `addCard d/CS2113 Finals q/What does OOP stand for? a/Object-Oriented Programming` |
| `listCards` | `listCards d/<DECK_NAME>` | `listCards d/CS2113 Finals` |
| `deleteCard` | `deleteCard d/<DECK_NAME> i/<INDEX>` | `deleteCard d/CS2113 Finals i/2` |
| `editCard` | `editCard d/<DECK_NAME> i/<INDEX> q/<QUESTION> a/<ANSWER>` | `editCard d/CS2113 Finals i/1 q/What does OOP stand for? a/Object-Oriented Programming` |
| `study` | `study d/<DECK_NAME>` | `study d/CS2113 Finals` |
| `exit` | `exit` | `exit` |

**Study session controls** (only valid while a session is active):

| Input | Action |
|-------|--------|
| Enter (empty line) | Reveal the current card's answer / advance to the next card |
| `1` – `5` | Submit your confidence rating for the current card |
| `q` | Quit the session early and return to the main prompt |
