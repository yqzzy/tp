# Ong Yi Qian - Project Portfolio Page

## Overview

FlashCLI is a lightweight command-line flashcard application for students who prefer a
fast, keyboard-first study workflow. It allows users to create, manage, and review
flashcard decks entirely from the terminal, making it ideal for students comfortable with
CLI environments who want minimal friction in their study sessions.

---

## Summary of Contributions

### Code Contributed

[RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=yqzzy&breakdown=true)

---

### New Feature: `deleteDeck` Command

- **What it does:** Allows users to permanently delete a named deck and all its cards
  with a single command.
- **Justification:** Without this command, users had no way to remove decks they no
  longer needed, leaving the deck list cluttered over time.
- **Highlights:** Implementing this required a design decision on whether `deleteDeck`
  in `DeckManager` should validate deck existence internally or rely on a prior `getDeck()`
  call as other commands do. The final decision was to validate internally — `deleteDeck`
  does not need the `Deck` object back and a public method should not assume callers always
  pre-validate, making it safer to call from any context. The feature was implemented
  end-to-end: `DeleteDeckCommand`, `DeckManager.deleteDeck()`, `Parser` support, and
  `Ui.showDeckDeleted()`.

---

### Enhancements to Existing Features

- Implemented the full `DeckManager` class — the central data manager that every
  deck-related command goes through. Added null/blank input guards, duplicate name
  detection, and post-condition assertions on all methods.
- Implemented the full `SessionManager` class — the facade managing the study session
  lifecycle. Designed `finishSession()` to be safe to call with no active session,
  simplifying `StudyCommand`'s multiple exit paths without requiring defensive checks.
- Implemented the full `StudySession` class — handles index-based card traversal with
  pre/post-condition assertions and confidence-based sorting on construction.
- Added `java.util.logging.Logger` to `DeckManager`, `SessionManager`, and `StudySession`
  with `FINE`/`WARNING`/`INFO` level logging at all public method boundaries.
- Fixed `build.gradle` — removed `enabled = false` from the `test` block that was
  silently preventing all tests from running, and added `enableAssertions = true` to both
  the `test` and `run` blocks.

---

### Contributions to Team-Based Tasks
- Set up and verified Checkstyle passing (`./gradlew check`) before each PR, and fixed
  Checkstyle violations in generated files (`DeleteDeckCommand.java`, `DeckManagerTest.java`,
  `Ui.java`).

---

### Documentation

#### User Guide

- Wrote the **`study` command** section — full step-by-step session flow, confidence
  rating scale (1–5 with meanings), all three exit paths, empty deck behaviour, and
  persistence note.
- Added the **Table of Contents** with anchor links to all sections.
- Added the **Notes on Command Format** section explaining prefix notation and ordering.
- Added two **FAQ** entries (data transfer; card deletion and confidence reset).
- Standardised formatting across all feature entries.

#### Developer Guide

- Wrote the **Study Module** section covering class structure, study session flow,
  confidence-based card ordering, and design rationale (SRP, Facade Pattern, Defensive
  Programming, Immutability of the Original Deck).
- Wrote the **Acknowledgements** section citing Anki, Quizlet, and AB3.
- Created three PlantUML diagrams styled with the team's `style.puml`:
    - `study_class.puml` — class diagram of the Study package
    - `study_sequence.puml` — sequence diagram of the full study session flow
    - `study_sorting.puml` — sequence diagram of confidence-based card sorting on session start
