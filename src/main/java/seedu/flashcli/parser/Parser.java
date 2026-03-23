package seedu.flashcli.parser;

import seedu.flashcli.command.AddCardCommand;
import seedu.flashcli.command.ClearDeckCommand;
import seedu.flashcli.command.CreateDeckCommand;
import seedu.flashcli.command.DeleteCardCommand;
import seedu.flashcli.command.ExitCommand;
import seedu.flashcli.command.HelpCommand;
import seedu.flashcli.command.ListCardsCommand;
import seedu.flashcli.command.ListDecksCommand;
import seedu.flashcli.command.StudyCommand;
import seedu.flashcli.command.Command;
import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Converts raw user input into a command object for execution.
 * Validates input is non-blank, and validates command keyword.
 * Dispatches to the appropriate helper, then constructs and returns the command.
 */
public class Parser {

    private static final Logger logger = Logger.getLogger("Parser");

    private static final String[] VALID_COMMANDS = {
        "addCard", "listCards", "deleteCard", "createDeck",
        "listDecks", "clearDeck", "study", "exit", "help"
    };

    private Parser() {

    }

    /**
     * Parses user input into an executable command.
     *
     * @param userInput String typed by the user.
     * @return The command corresponding to the userInput.
     * @throws FlashException if blank input, unrecognised command or invalid arguments.
     */

    public static Command parse(String userInput) throws FlashException {
        logger.log(Level.FINE, "parse() called with: \"{0}\"", userInput == null ? "null" : userInput);
        validateInput(userInput);
        // Splits the user input into two words (command, argument)
        String[] tokens = userInput.split(" ", 2);
        String command = tokens[0].trim();
        String arguments = tokens.length > 1 ? tokens[1].trim() : "";
        validateCommandName(command);
        logger.log(Level.FINE, "Dispatching command: \"{0}\" with arguments: \"{1}\"",
                new Object[]{command, arguments});
        return dispatch(command, arguments);
    }

    /**
     * Validates that the user input is not blank.
     *
     * @param userInput The raw input string to validate.
     * @throws FlashException If userInput is null or contains only whitespace.
     */
    private static void validateInput(String userInput) throws FlashException {
        if (userInput == null || userInput.trim().isEmpty()) {
            logger.log(Level.WARNING, "validateInput failed: input was null or blank");
            throw new FlashException(ErrorType.NULL_INPUT);
        }
    }

    /**
     * Validates that the command matches one of the entries in VALID_COMMANDS.
     *
     * @param command The command to validate.
     * @throws FlashException If the command is not found in VALID_COMMANDS.
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
     * Routes the command keyword to the appropriate parse helper.
     *
     * @return The command corresponding to the keyword.
     * @throws FlashException If the arguments are invalid.
     */
    private static Command dispatch(String command, String arguments) throws FlashException {
        assert !command.isEmpty() : "dispatch() received an empty command";
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
            return requireEmpty(arguments, new ListDecksCommand());
        case "clearDeck":
            return parseClearDeckCommand(arguments);
        case "study":
            return parseStudyCommand(arguments);
        case "exit":
            return requireEmpty(arguments, new ExitCommand());
        case "help":
            return requireEmpty(arguments, new HelpCommand());
        default:
            throw new AssertionError("Unexpected command reached dispatch: " + command);
        }
    }

    // Parses arguments and returns an AddCardCommand.
    private static Command parseAddCardCommand(String arguments) throws FlashException {
        AddCardArgs args = ArgumentExtractor.parseAddCardArgs(arguments);
        return new AddCardCommand(args);
    }

    // Parses arguments and returns a DeleteCardCommand.
    private static Command parseDeleteCardCommand(String arguments) throws FlashException {
        DeleteCardArgs args = ArgumentExtractor.parseDeleteCardArgs(arguments);
        return new DeleteCardCommand(args);
    }

    // Parses arguments and returns a ListCardsCommand.
    private static Command parseListCardsCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new ListCardsCommand(args.getDeckName());
    }

    // Parses arguments and returns a CreateDeckCommand.
    private static Command parseCreateDeckCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new CreateDeckCommand(args.getDeckName());
    }

    // Parses arguments and returns a ClearDeckCommand.
    private static Command parseClearDeckCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new ClearDeckCommand(args.getDeckName());
    }

    // Parses arguments and returns a StudyCommand.
    private static Command parseStudyCommand(String arguments) throws FlashException {
        DeckArgs args = ArgumentExtractor.parseDeckArgs(arguments);
        return new StudyCommand(args.getDeckName());
    }

    /**
     * Returns the command if no arguments are present, as expected for certain commands.
     *
     * @throws FlashException If arguments are non-empty.
     */
    private static Command requireEmpty(String args, Command command) throws FlashException {
        assert args != null : "requireEmpty called with null args";
        if (!args.trim().isEmpty()) {
            logger.log(Level.WARNING, "requireEmpty failed: unexpected arguments \"{0}\"", args);
            throw new FlashException(ErrorType.UNEXPECTED_ARGUMENTS);
        }
        return command;
    }
}
