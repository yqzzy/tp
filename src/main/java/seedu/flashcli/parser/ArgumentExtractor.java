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

    static void validatePrefixes(String arguments, String... prefixes) throws FlashException {
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
