package seedu.flashcli.parser;

import seedu.flashcli.command.*;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

/**
 * Parses and validates user input.
 * Calls the command constructor if the command is valid.
 */

public class Parser {

    // Array of all recognized commands accepted by the application.
    private static final String[] VALID_COMMANDS = {"addCard", "listCards", "deleteCard", "createDeck", "listDecks", "clearDeck", "study", "nextCard", "finish", "exit", "help"};

    private static final String DECK_PREFIX = "d/";
    private static final String QUESTION_PREFIX = "q/";
    private static final String ANSWER_PREFIX = "a/";
    private static final String INDEX_PREFIX = "i/";
    private static final int PREFIX_LEN = 2;

    private Parser() {

    }

    /**
     * Validates that the user input is not blank.
     *
     * @param userInput the raw input string to validate
     * @throws FlashException if userInput is null or contains only whitespace
     */
    private static void validateInput(String userInput) throws FlashException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new FlashException(ErrorType.NULL_INPUT);
        }
    }

    public static Command parse(String userInput) throws FlashException {
        validateInput(userInput);

        String[] tokens  = userInput.split(" ", 2);
        String command   = tokens[0].trim();
        String arguments = tokens.length > 1 ? tokens[1].trim() : "";

        validateCommandName(command);
        return dispatch(command, arguments);
    }

    /**
     * Validates that the command matches one of the entries in VALID_COMMANDS.
     *
     * @param command the command to validate
     * @throws FlashException if the command is not found in the list of valid commands
     */
    private static void validateCommandName(String command) throws FlashException {
        for (String valid : VALID_COMMANDS) {
            if (valid.equals(command)) {
                return;
            }
        }
        throw new FlashException(ErrorType.INVALID_COMMAND);
    }

    /**
     * Validates the appropriate command.
     */
    private static Command dispatch(String command, String arguments) throws FlashException {
        switch (command) {
        case "addCard":
            return parseAddCardCommand(arguments);
        case "listCards":
            return parseListCardsCommand(arguments);
        case "deleteCard":
            return parseDeleteCardCommand(arguments);
        case "createDeck":
            return parseCreateDeckCommand(arguments);
        case "listDecks":
            return new ListDecksCommand();
        case "clearDeck":
            return parseClearDeckCommand(arguments);
        case "study":
            return parseStudyCommand(arguments);
        case "nextCard":
            requireEmptyArgs(arguments);
            return new NextCardCommand();
        case "finish":
            requireEmptyArgs(arguments);
            return new FinishCommand();
        case "exit":
            requireEmptyArgs(arguments);
            return new ExitCommand();
        case "help":
            requireEmptyArgs(arguments);
            return new HelpCommand();
        default:
            throw new FlashException(ErrorType.INVALID_COMMAND);
        }
    }

    // Ensures the d/, q/ and a/ prefixes are contained in the right order, and non-empty descriptions
    private static Command parseAddCardCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX, QUESTION_PREFIX, ANSWER_PREFIX);
        int deckIdx = arguments.indexOf(DECK_PREFIX);
        int questionIdx = arguments.indexOf(QUESTION_PREFIX);
        int answerIdx = arguments.indexOf(ANSWER_PREFIX);
        if (!(deckIdx < questionIdx && questionIdx < answerIdx)) {
            throw new FlashException(ErrorType.INVALID_ADD_CARD);
        }
        String deck = arguments.substring(deckIdx + PREFIX_LEN, questionIdx).trim();
        String question = arguments.substring(questionIdx + PREFIX_LEN, answerIdx).trim();
        String answer = arguments.substring(answerIdx + PREFIX_LEN).trim();
        requireNonEmpty(deck, question, answer);
        return new AddCardCommand(deck, question, answer);
    }

    // Ensures the d/ prefix, and non-empty deck name
    private static Command parseListCardsCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
        return new ListCardsCommand(deck);
    }

    // Ensures the d/ and i/ prefixes are contained in the right order, and non-empty descriptions
    private static Command parseDeleteCardCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX, INDEX_PREFIX);
        int deckIdx = arguments.indexOf(DECK_PREFIX);
        int indexIdx = arguments.indexOf(INDEX_PREFIX);
        if (!(deckIdx < indexIdx)) {
            throw new FlashException(ErrorType.INVALID_DELETE_CARD);
        }
        String deck = arguments.substring(deckIdx + PREFIX_LEN, indexIdx).trim();
        String indexString = arguments.substring(indexIdx + PREFIX_LEN).trim();
        requireNonEmpty(deck, indexString);
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            throw new FlashException(ErrorType.INVALID_INDEX);
        }
        return new DeleteCardCommand(deck, index);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseCreateDeckCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
        return new CreateDeckCommand(deck);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseClearDeckCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
        return new ClearDeckCommand(deck);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseStudyCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
        return new StudyCommand(deck);
    }

    // Ensures each string passed in non-empty
    private static void requireNonEmpty(String... args) throws FlashException {
        for (String arg : args) {
            if (arg.isEmpty()) {
                throw new FlashException(ErrorType.ARGUMENT_MISSING);
            }
        }
    }

    // Ensures each string passed in non-empty
    private static void requireEmptyArgs(String args) throws FlashException {
        if (args != null && !args.trim().isEmpty()) {
            throw new FlashException(ErrorType.UNEXPECTED_ARGUMENTS);
        }
    }

    // Ensures each prefix occurs, and only occurs once
    private static void validatePrefixes(String arguments, String... prefixes) throws FlashException {
        if (arguments == null) {
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
        for (String prefix : prefixes) {
            if (!arguments.contains(prefix)) {
                if (DECK_PREFIX.equals(prefix)) {
                    throw new FlashException(ErrorType.MISSING_DECK);
                }
                if (QUESTION_PREFIX.equals(prefix)) {
                    throw new FlashException(ErrorType.MISSING_QUESTION);
                }
                if (ANSWER_PREFIX.equals(prefix)) {
                    throw new FlashException(ErrorType.MISSING_ANSWER);
                }
                if (INDEX_PREFIX.equals(prefix)) {
                    throw new FlashException(ErrorType.MISSING_INDEX);
                }
                throw new FlashException(ErrorType.INVALID_ARGUMENTS);
            }
            if (arguments.indexOf(prefix) != arguments.lastIndexOf(prefix)) {
                throw new FlashException(ErrorType.DUPLICATE_PREFIX);
            }
        }
    }
}
