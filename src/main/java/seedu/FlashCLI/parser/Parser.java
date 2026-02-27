package seedu.FlashCLI.parser;

import seedu.FlashCLI.exception.ErrorType;
import seedu.FlashCLI.exception.FlashException;

/**
 * Parses and validates user input.
 * Calls the command constructor if the command is valid.
 */

// TODO (@sid): Connect to command constructors after validation
public class Parser {

    // Array of all recognized commands accepted by the application.
    private static final String[] VALID_COMMANDS = {
            "addCard", "listCards", "deleteCard",
            "createDeck", "listDecks", "clearDeck",
            "study", "nextCard", "finish",
            "exit", "help"
    };

    private static final String DECK_PREFIX = "d/";
    private static final String QUESTION_PREFIX = "q/";
    private static final String ANSWER_PREFIX = "a/";
    private static final String INDEX_PREFIX = "i/";

    private static final int PREFIX_LEN = 2;

    protected String command;
    protected String arguments;

    /**
     * The input is split into its command and arguments.
     * The command is then validated and executed.
     *
     * @param userInput the raw input string entered by the user
     * @throws FlashException if the input is null or blank, or if the command is not valid
     */
    public Parser(String userInput) throws FlashException {
        validateInput(userInput);
        String[] words = userInput.split(" ", 2);
        command = words[0].trim();
        arguments = words.length > 1 ? words[1] : "";
        validateCommandName(command);
        validateCommandArguments(command, arguments);
    }

    /**
     * Validates that the user input is not blank.
     *
     * @param userInput the raw input string to validate
     * @throws FlashException if userInput is null or contains only whitespace
     */
    private void validateInput(String userInput) throws FlashException {
        if (userInput == null || userInput.trim().isEmpty()) {
            throw new FlashException(ErrorType.NULL_INPUT);
        }
    }

    /**
     * Validates that the command matches one of the entries in VALID_COMMANDS.
     *
     * @param command the command to validate
     * @throws FlashException if the command is not found in the list of valid commands
     */
    private void validateCommandName(String command) throws FlashException {
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
    public void validateCommandArguments(String command, String arguments) throws FlashException {
        switch (command) {
        case "addCard":
            validateAddCardCommand(arguments);
            break;
        case "listCards":
            validateListCardsCommand(arguments);
            break;
        case "deleteCard":
            validateDeleteCardCommand(arguments);
            break;
        case "createDeck":
            validateCreateDeckCommand(arguments);
            break;
        case "listDecks":
            break;
        case "clearDeck":
            validateClearDeckCommand(arguments);
            break;
        case "study":
            validateStudyCommand(arguments);
            break;
        case "nextCard":
            validateNextCardCommand(arguments);
            break;
        case "finish":
            validateFinishCommand(arguments);
            break;
        case "exit":
            validateExitCommand(arguments);
            break;
        case "help":
            validateHelpCommand(arguments);
            break;
        default:
            throw new FlashException(ErrorType.INVALID_COMMAND);
        }
    }

    // Ensures the d/, q/ and a/ prefixes are contained in the right order, and non-empty descriptions
    private void validateAddCardCommand(String arguments) throws FlashException {
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
    }

    // Ensures the d/ prefix, and non-empty deck name
    private void validateListCardsCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
    }

    // Ensures the d/ and i/ prefixes are contained in the right order, and non-empty descriptions
    private void validateDeleteCardCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX, INDEX_PREFIX);
        int deckIdx = arguments.indexOf(DECK_PREFIX);
        int indexIdx = arguments.indexOf(INDEX_PREFIX);
        if (!(deckIdx < indexIdx)) {
            throw new FlashException(ErrorType.INVALID_DELETE_CARD);
        }
        String deck = arguments.substring(deckIdx + PREFIX_LEN, indexIdx).trim();
        String index = arguments.substring(indexIdx + PREFIX_LEN).trim();
        requireNonEmpty(deck, index);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private void validateCreateDeckCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private void validateClearDeckCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private void validateStudyCommand(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deck = arguments.substring(arguments.indexOf(DECK_PREFIX) + PREFIX_LEN).trim();
        requireNonEmpty(deck);
    }

    private void validateNextCardCommand(String arguments) throws FlashException {
        requireEmptyArgs(arguments);
    }

    private void validateFinishCommand(String arguments) throws FlashException {
        requireEmptyArgs(arguments);
    }

    private void validateExitCommand(String arguments) throws FlashException {
        requireEmptyArgs(arguments);
    }

    private void validateHelpCommand(String arguments) throws FlashException {
        requireEmptyArgs(arguments);
    }

    // Ensures each string passed in non-empty
    private void requireNonEmpty(String... args) throws FlashException {
        for (String arg : args) {
            if (arg.isEmpty()) {
                throw new FlashException(ErrorType.ARGUMENT_MISSING);
            }
        }
    }

    // Ensures each string passed in non-empty
    private void requireEmptyArgs(String args) throws FlashException {
        if (!(args == null)) {
            throw new FlashException(ErrorType.UNEXPECTED_ARGUMENTS);
        }
    }

    // Ensures each prefix occurs, and only occurs once
    private void validatePrefixes(String arguments, String... prefixes) throws FlashException {
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