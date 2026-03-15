package seedu.flashcli.parser;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

public class ArgumentExtractor {

    static final String DECK_PREFIX = "d/";
    static final String QUESTION_PREFIX = "q/";
    static final String ANSWER_PREFIX = "a/";
    static final String INDEX_PREFIX = "i/";
    private static final int PREFIX_LEN = 2;

    private ArgumentExtractor() {

    }

    public static DeckArgs parseDeckArgs(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX);
        String deckName = extractAfter(arguments, DECK_PREFIX);
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        return new DeckArgs(deckName);
    }

    public static AddCardArgs parseAddCardArgs(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX, QUESTION_PREFIX, ANSWER_PREFIX);
        validatePrefixOrder(arguments, DECK_PREFIX, QUESTION_PREFIX, ANSWER_PREFIX);
        String deckName = extractBetween(arguments, DECK_PREFIX, QUESTION_PREFIX);
        String question = extractBetween(arguments, QUESTION_PREFIX, ANSWER_PREFIX);
        String answer = extractAfter(arguments, ANSWER_PREFIX);
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        validateNonEmpty(question, ErrorType.MISSING_QUESTION);
        validateNonEmpty(answer,   ErrorType.MISSING_ANSWER);
        return new AddCardArgs(deckName, question, answer);
    }

    public static DeleteCardArgs parseDeleteCardArgs(String arguments) throws FlashException {
        validatePrefixes(arguments, DECK_PREFIX, INDEX_PREFIX);
        validatePrefixOrder(arguments, DECK_PREFIX, INDEX_PREFIX);
        String deckName = extractBetween(arguments, DECK_PREFIX, INDEX_PREFIX);
        String indexStr = extractAfter(arguments, INDEX_PREFIX);
        validateNonEmpty(deckName, ErrorType.MISSING_DECK);
        validateNonEmpty(indexStr, ErrorType.MISSING_INDEX);
        return new DeleteCardArgs(deckName, parseIndex(indexStr));
    }

    private static int parseIndex(String indexStr) throws FlashException {
        try {
            return Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new FlashException(ErrorType.INVALID_INDEX);
        }
    }

    // Ensures each prefix occurs, and only occurs once
    static void validatePrefixes(String arguments, String... prefixes) throws FlashException {
        if (arguments == null) {
            throw new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
        for (String prefix : prefixes) {
            if (!arguments.contains(prefix)) {
                throw missingErrorFor(prefix);
            }
            if (arguments.indexOf(prefix) != arguments.lastIndexOf(prefix)) {
                throw new FlashException(ErrorType.DUPLICATE_PREFIX);
            }
        }
    }

    static void validatePrefixOrder(String arguments, String... prefixes) throws FlashException {
        int last = -1;
        for (String prefix : prefixes) {
            int index = arguments.indexOf(prefix);
            if (index <= last) {
                throw new FlashException(ErrorType.INVALID_ARGUMENTS);
            }
            last = index;
        }
    }

    private static void validateNonEmpty(String value, ErrorType errorType) throws FlashException {
        if (value == null || value.isEmpty()) {
            throw new FlashException(errorType);
        }
    }

    private static FlashException missingErrorFor(String prefix) {
        switch (prefix) {
        case DECK_PREFIX:     return new FlashException(ErrorType.MISSING_DECK);
        case QUESTION_PREFIX: return new FlashException(ErrorType.MISSING_QUESTION);
        case ANSWER_PREFIX:   return new FlashException(ErrorType.MISSING_ANSWER);
        case INDEX_PREFIX:    return new FlashException(ErrorType.MISSING_INDEX);
        default:              return new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
    }

    private static String extractBetween(String arguments, String startPrefix, String endPrefix) {
        int start = arguments.indexOf(startPrefix) + PREFIX_LEN;
        int end   = arguments.indexOf(endPrefix);
        return arguments.substring(start, end).trim();
    }

    private static String extractAfter(String arguments, String prefix) {
        int start = arguments.indexOf(prefix) + PREFIX_LEN;
        return arguments.substring(start).trim();
    }
}

