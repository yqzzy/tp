# Huang Yingrui - Project Portfolio Page

## Overview

FlashCLI is a lightweight command-line flashcard application for students who prefer a fast, keyboard-first study workflow. It allows users to create, manage, and review flashcard decks entirely from the terminal, making it ideal for students comfortable with CLI environments who want minimal friction in their study sessions.

---

## Summary of Contributions

### Code Contributed

[RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=T10&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=&tabOpen=true&tabType=authorship&tabAuthor=cigaho&tabRepo=AY2526S2-CS2113-T10-1%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)

---

### New Feature: Data Persistence with Automatic Version Control

- **What it does:** Implements a robust system to automatically save the user's flashcard data (`DeckManager` state) to a local JSON file (`flashcards.json`). A key enhancement is the integrated **automatic version control**. Every time the data is saved, a historical snapshot is created in a `./data/history/` directory. This allows recovery from accidental data loss or corruption. Deleted history versions are moved to a `./data/waste/` directory as a safety net.
- **Justification:** Prior to this feature, user data was volatile and could be lost upon application exit or due to errors. The automatic backup system provides essential data safety, a critical feature for a study tool where recreating lost flashcards is time-consuming and frustrating.
- **Highlights:**
    - **Seamless Integration:** The `Storage.save()` method automatically triggers a backup via the `HistoryManager` before overwriting the main data file, requiring no extra user steps.
    - **Robust Error Handling:** The JSON parsing logic gracefully handles corrupted data files. If the main `flashcards.json` file contains invalid JSON, the application logs a warning, loads an empty state, and continues running instead of crashing.
    - **Safe Cleanup:** The `waste` directory acts as a recycle bin. When users clean their history, files are moved (not deleted), allowing for manual recovery if needed.
    - **Design for Reliability:** The `HistoryManager` is loosely coupled with the core `Storage` class. If the history feature fails to initialize (e.g., due to file permissions), the core save/load functionality remains operational, ensuring the application's primary purpose is never compromised.

---

### Enhancements to Existing Features

- **Implemented the full `Storage` class** – The primary facade for all data persistence operations. It handles the serialization/deserialization of the `DeckManager` object graph to/from JSON using the Gson library.
- **Implemented the full `HistoryManager` class** – A dedicated component following the **Single Responsibility Principle** to manage the lifecycle of historical data snapshots. It handles generating unique, timestamped filenames (e.g., `flashcards_20250317_001.json`), listing available versions, retrieving specific versions, and cleaning up old history.
- **Enhanced the `save` mechanism for all commands** – Modified the command execution flow so that any command that changes the `DeckManager` state (e.g., `AddCardCommand`, `CreateDeckCommand`) calls `Storage.save()`, thereby triggering the automatic persistence and backup process.
- **Comprehensive Unit Testing** – Created JUnit tests for the `Storage` and `HistoryManager` classes, covering core use cases like saving, loading, historical version creation, listing, and cleanup operations.

---

### Contributions to Team-Based Tasks
- **Build & Quality Assurance:** Fixed and verified the Gradle build configuration to ensure the project compiled and all tests ran correctly. Ran Checkstyle (`./gradlew check`) before commits to maintain code quality.
- **Integration Support:** Assisted in integrating the data persistence system with the command layer, ensuring data is automatically saved after every user action that modifies state.

---

### Documentation

#### User Guide
- **Primary Author of Appendix E: Instructions for Manual Testing** – Wrote the comprehensive, step-by-step manual testing guide that provides a clear path for testers to verify all user-facing features. This includes specific command examples and expected outputs, ensuring alignment with the actual application behavior.
- **Contributed to the FAQ and Command Guide sections** – Added explanations related to data location, file safety, and the behavior of the automatic save feature.

#### Developer Guide
- **Primary Author of the "Data Persistence with Version Control" section** – Documented the complete design and implementation of the `Storage` and `HistoryManager` components, including the architectural rationale, key decisions, and considered alternatives.
- **Created supporting PlantUML diagrams:**
    - `storage_class.puml` – Class diagram illustrating the relationship between the `Storage` and `HistoryManager` classes and their core methods.
    - `storage_save_sequence.puml` – Sequence diagram detailing the automated save-and-backup process triggered by a user command.
- **Updated multiple appendices** – Ensured the Developer Guide was complete by contributing to the **Glossary** (defining terms like *History Version*, *Waste Directory*), **Non-Functional Requirements** (adding requirements for data reliability and performance), and **Product Scope**.

---

### Contributions to Project Setup & Maintenance
- **Configuration Management:** Updated the project's `.gitignore` file to prevent automatically generated data files (e.g., `./data/flashcards.json`, `./data/history/*.json`) from being committed to version control, keeping the repository clean.