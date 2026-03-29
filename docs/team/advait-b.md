# Advait - Project Portfolio Page

## Overview

FlashCLI is a lightweight command-line flashcard application for students who prefer a fast, keyboard-first study workflow. It allows users to create, manage, and review flashcard decks entirely from the terminal, making it ideal for students comfortable with CLI environments who want minimal friction in their study sessions.

### Summary of Contributions


### Code Contributed

[RepoSense Dashboard](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=advait-b&breakdown=true)

### New Feature: Command Parsing and Extraction

- **What it does:** Parses raw user input from the command line and extracts the corresponding command type and its arguments. It serves as the entry point for translating input into executable application logic.
- **Justification:** Without a robust parser, the application would be unable to correctly interpret varied user inputs, making reliable command execution impossible. Centralising this logic ensures all commands are handled consistently and predictably.
- **Highlights:** The parser was designed as a stateless utility class, adhering to the single responsibility principle by handling only parsing concerns. Implementing it required careful consideration of edge cases.

### New Feature: FlashCLIException Class and ErrorType Enum

- **What it does:** Introduces a custom exception class (`FlashCLIException`) and an `ErrorType` enum that categorises all anticipated error conditions in the application. 
- **Justification:** A typed enum-based approach ensures that every error scenario is explicitly defined, making the system more robust and easier to debug.
- **Highlights:** Designing the `ErrorType` enum required an upfront audit of all possible failure modes across every command and subsystem.

### Enhancements to Existing Features

- Wrote JUnit tests for the `Parse` component, increasing its line coverage to 98%.
- Created barebone command classes to establish the inheritance hierarchy, ensuring all commands share a consistent interface and can be extended without duplication.
- Refactored print statements out of the core `FlashCLI` class and into a dedicated `UI` class to improve modularity, and made corresponding amendments in `StudySession` to maintain consistency.
