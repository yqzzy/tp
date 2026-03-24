# Developer Guide

## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

### Parser

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

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
