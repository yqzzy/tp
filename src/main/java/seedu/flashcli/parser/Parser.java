package seedu.flashcli.parser;

import seedu.flashcli.command.*;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

/**
 * Parses and validates user input.
 * Calls the command constructor if the command is valid.
 */
public class Parser {

    private static final String[] VALID_COMMANDS = {
            "addCard", "listCards", "deleteCard", "createDeck",
            "listDecks", "clearDeck", "study", "nextCard", "finish", "exit", "help"
    };

    private Parser() {}

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
        String[] tokens = userInput.split(" ", 2);
        String command = tokens[0].trim();
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
        AddCardArgs args = ArgumentExtractor.parseAddCardArgs(arguments);
        return new AddCardCommand(args.getDeckName(), args.getQuestion(), args.getAnswer());
    }

    // Ensures the d/ prefix, and non-empty deck name
    private static Command parseListCardsCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new ListCardsCommand(args.getDeckName());
    }

    // Ensures the d/ and i/ prefixes are contained in the right order, and non-empty descriptions
    private static Command parseDeleteCardCommand(String arguments) throws FlashException {
        DeleteCardArgs args = ArgumentExtractor.parseDeleteCardArgs(arguments);
        return new DeleteCardCommand(args.getDeckName(), args.getCardIndex());
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseCreateDeckCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new CreateDeckCommand(args.getDeckName());
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseClearDeckCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new ClearDeckCommand(args.getDeckName());
    }

    // Ensures the d/ prefix is contained, and non-empty deck name
    private static Command parseStudyCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new StudyCommand(args.getDeckName());
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
}
