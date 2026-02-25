package domain;

import exception.ErrorMessages;
import exception.LoanNotFoundException;

import java.time.LocalDate;

public class Loan {

    private final long bookId;
    private final long memberId;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private boolean returned;

    public Loan(long bookId, long memberId, LocalDate loanDate, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public Loan(long bookId, long memberId, LocalDate loanDate, LocalDate dueDate, boolean returned) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returned = returned;
    }

    public Loan(Loan loan) {
        this.bookId = loan.getBookId();
        this.memberId = loan.getMemberId();
        this.loanDate = loan.getLoanDate();
        this.dueDate = loan.getDueDate();
        this.returned = loan.isReturned();
    }

    public long getBookId() {
        return bookId;
    }

    public long getMemberId() {
        return memberId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void returnBook() {
        if (this.returned) {
            throw new LoanNotFoundException(String.format(ErrorMessages.LOAN_NOT_FOUND, this.bookId));
        }
        this.returned = true;
    }

    @Override
    public String toString() {
        return String.format(
                "loan - bookId: %d, memberId: %d, %s ~ %s, returned: %b",
                bookId, memberId, loanDate.toString(), dueDate.toString(), returned
        );
    }
}
