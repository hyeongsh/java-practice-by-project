package exception;

public class AlreadyLoanedException extends RuntimeException {
    public AlreadyLoanedException(String message) {
        super(message);
    }
}
