package seedu.flashcli.parser;

import seedu.flashcli.exception.ErrorType;
import seedu.flashcli.exception.FlashException;

public class ArgumentExtractor {

    static final String DECK_PREFIX = "d/";
    static final String QUESTION_PREFIX = "q/";
    static final String ANSWER_PREFIX = "a/";
    static final String INDEX_PREFIX = "i/";
    private static final int PREFIX_LEN = 2;

    private ArgumentExtractor() {}

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

    private static FlashException missingErrorFor(String prefix) {
        switch (prefix) {
        case DECK_PREFIX:     return new FlashException(ErrorType.MISSING_DECK);
        case QUESTION_PREFIX: return new FlashException(ErrorType.MISSING_QUESTION);
        case ANSWER_PREFIX:   return new FlashException(ErrorType.MISSING_ANSWER);
        case INDEX_PREFIX:    return new FlashException(ErrorType.MISSING_INDEX);
        default:              return new FlashException(ErrorType.INVALID_ARGUMENTS);
        }
    }
}
