package exception;

public final class ErrorMessages {

    private ErrorMessages() {}

    public static final String DUPLICATE_ORDER = "DUPLICATE_ORDER: orderId=%s";
    public static final String ORDER_NOT_FOUND = "ORDER_NOT_FOUND: orderId=%s";
    public static final String PARSE_ERROR = "PARSE_ERROR: line=%d";
    public static final String INVALID_INPUT = "INVALID_INPUT: field=%s";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER: parameter=%s";

}
