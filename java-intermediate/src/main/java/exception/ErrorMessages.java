package exception;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String INVALID_PARAMETER = "INVALID_PARAMETER: parameter=%s";
    public static final String ALREADY_LOANED = "ALREADY_LOANED: bookId=%s";
    public static final String BOOK_NOT_FOUND = "BOOK_NOT_FOUND: bookId=%s";
    public static final String MEMBER_NOT_FOUND = "MEMBER_NOT_FOUND: memberId=%s";
    public static final String LOAN_NOT_FOUND = "LOAN_NOT_FOUND: bookId=%s";
    public static final String DUPLICATE_BOOK = "DUPLICATE_BOOK: bookId=%s";
    public static final String DUPLICATE_MEMBER = "DUPLICATE_MEMBER: memberId=%s";
    public static final String LOAN_LIMIT_EXCEED = "LOAN_LIMIT_EXCEED: memberId=%s";
    public static final String PARSE_ERROR = "PARSE_ERROR: %s";

}
