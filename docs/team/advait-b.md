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

### New Feature: Exception Handling

- **What it does:** Introduces a custom exception class (`FlashCLIException`) and an `ErrorType` enum that categorises all anticipated error conditions in the application. 
- **Justification:** A typed enum-based approach ensures that every error scenario is explicitly defined, making the system more robust and easier to debug.
- **Highlights:** Designing the `ErrorType` enum required an upfront audit of all possible failure modes across every command and subsystem.

### Enhancements to Existing Features

- Wrote JUnit tests for the `Parser` component, increasing its line coverage to 98%.
- Created barebone command classes to establish the inheritance hierarchy, ensuring all commands share a consistent interface and can be extended without duplication.
- Refactored print statements out of the core `FlashCLI` class and into a dedicated `UI` class to improve modularity, and made corresponding amendments in `StudySession` to maintain consistency ([#61](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/61))

### Contributions to Team-Based Tasks

- Created the milestone for **v1.0** on GitHub and populated it with **all** the user stories.
- Created the milestone for **v2.0** on GitHub and populated it with **all** the user stories.
- Added deadlines to all milestones to keep the team on schedule.
- Fixed general bugs to ensure the project consistently passed automated build tests throughout development ([#62](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/62), [#64](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/64), [#65](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/65))
- Managed GitHub releases for v2.0.

### Mentoring Contributions

- Added non-trivial PR review feedback on these PRs: [#2](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/2), [#5](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/5), [#54](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/54), [#79](https://github.com/AY2526S2-CS2113-T10-1/tp/pull/79)
- Proactively identified code design issues prior to v2.0 and documented actionable recommendations for adhering to SRP, SOC, LSP in a shared Google Doc distributed to the team.

### Documentation

#### User Guide

- Wrote the product introduction section.
- Wrote the quick start guide.
- Added the command summary table for quick reference.
- Added user stories for v1.0.

#### Developer Guide

- Wrote the design and implementation section describing the architecture of FlashCLI.
- Created the architecture class diagram showing relationships between all major components.
- Added a Parser class diagram and sequence diagram illustrating the parsing flow.
- Documented the Parser design rationale.
