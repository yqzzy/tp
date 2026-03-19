package seedu.flashcli.parser;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Performs all prefix-based argument validation and extraction for the parser package.
 * - Validates that required prefixes are present and appear exactly once.
 * - Validates that prefixes appear in the correct left-to-right order.
 * - Extracts values from the argument string.
 * - Returns typed args objects to Parser.
 *
 */
public class ArgumentExtractor {

    private static final Logger logger = Logger.getLogger("ArgumentExtractor");

    /** Prefix tokens for possible arguments. */
    private static final String DECK_PREFIX = "d/";
    private static final String QUESTION_PREFIX = "q/";
    private static final String ANSWER_PREFIX = "a/";
    private static final String INDEX_PREFIX = "i/";
    private static final int PREFIX_LEN = 2;

    private ArgumentExtractor() {

    }

    /**
     * Parses the argument string for any command requiring only a deck name.
     *
     * @return DeckArgs containing the deck name.
     * @throws FlashException If d/ is missing, duplicated, or the deck name is blank.
     */
    public static DeckArgs parseDeckArgs(String arguments) throws FlashException {
        logger.log(Level.FINE, "parseDeckArgs called with: \"{0}\"", arguments);
        validatePrefixes(arguments, DECK_PREFIX);
        String deckName = extractAfter(arguments, DECK_PREFIX);
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        assert !deckName.isEmpty() : "parseDeckArgs deck issue";
        logger.log(Level.FINE, "parseDeckArgs succeeded: deckName=\"{0}\"", deckName);
        return new DeckArgs(deckName);
    }

    /**
     * Parses the argument string for the addCard command.
     * Expects d/, q/, and a/ in that order.
     *
     * @return addCardArgs containing deck name, question, and answer.
     * @throws FlashException If any prefix or extracted value issue detected.
     */
    public static AddCardArgs parseAddCardArgs(String arguments) throws FlashException {
        logger.log(Level.FINE, "parseAddCardArgs called with: \"{0}\"", arguments);
        // Ensure each prefix is contained exactly once in the right order
        validatePrefixes(arguments, DECK_PREFIX, QUESTION_PREFIX, ANSWER_PREFIX);
        validatePrefixOrder(arguments, DECK_PREFIX, QUESTION_PREFIX, ANSWER_PREFIX);
        // Extract the string parameters
        String deckName = extractBetween(arguments, DECK_PREFIX, QUESTION_PREFIX);
        String question = extractBetween(arguments, QUESTION_PREFIX, ANSWER_PREFIX);
        String answer = extractAfter(arguments, ANSWER_PREFIX);
        // Ensure each string is non-empty
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        validateNonEmpty(question, ErrorType.MISSING_QUESTION);
        validateNonEmpty(answer,   ErrorType.MISSING_ANSWER);
        // Assertions for programmer error catching
        assert !deckName.isEmpty() : "parseAddCardArgs deck issue";
        assert !question.isEmpty() : "parseAddCardArgs question issue";
        assert !answer.isEmpty() : "parseAddCardArgs answer issue";
        logger.log(Level.FINE, "parseAddCardArgs succeeded");
        // Return the AddCardArgs
        return new AddCardArgs(deckName, question, answer);
    }

    /**
     * Parses the argument string for the deleteCard command.
     * Expects d/ followed by i/.
     *
     * @return DeleteCardArgs containing deck name and zero-based card index.
     * @throws FlashException If any prefix or extracted value issue detected.
     */
    public static DeleteCardArgs parseDeleteCardArgs(String arguments) throws FlashException {
        logger.log(Level.FINE, "parseDeleteCardArgs called with: \"{0}\"", arguments);
        // Ensure each prefix is contained exactly once in the right order
        validatePrefixes(arguments, DECK_PREFIX, INDEX_PREFIX);
        validatePrefixOrder(arguments, DECK_PREFIX, INDEX_PREFIX);
        // Extract the string parameters
        String deckName = extractBetween(arguments, DECK_PREFIX, INDEX_PREFIX);
        String indexStr = extractAfter(arguments, INDEX_PREFIX);
        // Ensure each string is non-empty
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        validateNonEmpty(indexStr, ErrorType.MISSING_INDEX);
        // Assertions for programmer error catching
        assert !deckName.isEmpty() : "parseDeleteCardArgs deck issue";
        int cardIndex = parseIndex(indexStr);
        assert cardIndex >= 0 : "parseDeleteCardArgs index issue";
        logger.log(Level.FINE, "parseDeleteCardArgs succeeded");
        return new DeleteCardArgs(deckName, cardIndex);
    }

    /**
     * Converts a one-based index string from the user into a zero-based integer.
     *
     * @return Zero-based integer index.
     * @throws FlashException If indexStr cannot be parsed as an integer.
     */
    private static int parseIndex(String indexStr) throws FlashException {
        try {
            int result = Integer.parseInt(indexStr) - 1;
            assert result >= 0 : "parseIndex result issue";
            return result;
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "parseIndex failed: \"{0}\" is not a valid integer", indexStr);
            throw new FlashException(ErrorType.INVALID_INDEX);
        }
    }

    /**
     * Validates that each required prefix appears exactly once.
     *
     * @param arguments Raw argument string to inspect.
     * @param prefixes Prefix tokens that must each appear exactly once.
     * @throws FlashException If any prefix is absent or appears more than once.
     */
    private static void validatePrefixes(String arguments, String... prefixes) throws FlashException {
        if (arguments == null) {
            logger.log(Level.WARNING, "validatePrefixes called with null arguments");
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
        assert prefixes != null && prefixes.length > 0 : "validatePrefixes prefixes issue";
        for (String prefix : prefixes) {
            if (!arguments.contains(prefix)) {
                logger.log(Level.WARNING, "validatePrefixes failed: prefix \"{0}\" not found in \"{1}\"");
                throw missingErrorFor(prefix);
            }
            if (arguments.indexOf(prefix) != arguments.lastIndexOf(prefix)) {
                logger.log(Level.WARNING, "validatePrefixes failed: prefix \"{0}\" appears more than once");
                throw new FlashException(ErrorType.DUPLICATE_PREFIX);
            }
        }
    }

    /**
     * Validates that the given prefixes appear in strict left-to-right order.
     *
     * @param arguments Raw argument string to inspect.
     * @param prefixes Prefix tokens in the expected left-to-right order.
     * @throws FlashException If the prefixes do not appear in the specified order.
     */
    private static void validatePrefixOrder(String arguments, String... prefixes) throws FlashException {
        assert arguments != null : "validatePrefixOrder arguments issue";
        int last = -1;
        for (String prefix : prefixes) {
            int index = arguments.indexOf(prefix);
            if (index <= last) {
                logger.log(Level.WARNING, "validatePrefixOrder failed: prefix \"{0}\" out of order in \"{1}\"");
                throw new FlashException(ErrorType.INVALID_ARGUMENTS);
            }
            last = index;
        }
    }

    /**
     * Throws a FlashException if the given value is null or blank.
     *
     * @param value The extracted string value to check.
     * @param errorType The ErrorType to use if the value is blank.
     * @throws FlashException If value is null or empty.
     */
    private static void validateNonEmpty(String value, ErrorType errorType) throws FlashException {
        if (value == null || value.isEmpty()) {
            logger.log(Level.WARNING, "validateNonEmpty failed for ErrorType: {0}", errorType);
            throw new FlashException(errorType);
        }
    }

    /**
     * Returns a FlashException mapped to the appropriate ErrorType
     */
    private static FlashException missingErrorFor(String prefix) {
        assert prefix != null : "missingErrorFor prefix issue";
        switch (prefix) {
        case DECK_PREFIX:     return new FlashException(ErrorType.MISSING_DECK);
        case QUESTION_PREFIX: return new FlashException(ErrorType.MISSING_QUESTION);
        case ANSWER_PREFIX:   return new FlashException(ErrorType.MISSING_ANSWER);
        case INDEX_PREFIX:    return new FlashException(ErrorType.MISSING_INDEX);
        default:              return new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
    }

    /**
     * Extracts and trims the substring between startPrefix and endPrefix.
     */
    private static String extractBetween(String arguments, String startPrefix, String endPrefix) {
        assert arguments.contains(startPrefix) : "extractBetween startPrefix issue";
        assert arguments.contains(endPrefix) : "extractBetween endPrefix issue";
        assert arguments.indexOf(startPrefix) + PREFIX_LEN
                <= arguments.indexOf(endPrefix) : "extractBetween index issue";
        int start = arguments.indexOf(startPrefix) + PREFIX_LEN;
        int end   = arguments.indexOf(endPrefix);
        return arguments.substring(start, end).trim();
    }

    /**
     * Extracts and trims the substring from endPrefix till the end of the string.
     */
    private static String extractAfter(String arguments, String prefix) {
        assert arguments.contains(prefix) : "extractAfter prefix issue";
        int start = arguments.indexOf(prefix) + PREFIX_LEN;
        return arguments.substring(start).trim();
    }
}