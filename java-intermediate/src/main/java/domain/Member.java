package domain;

import exception.ErrorMessages;
import exception.LoanLimitExceededException;

public class Member {

    private final long id;
    private String name;
    private int maxLoanCount = 5;

    public Member(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Member(long id, String name, int maxLoanCount) {
        this.id = id;
        this.name = name;
        this.maxLoanCount = maxLoanCount;
    }

    public Member(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.maxLoanCount = member.getMaxLoanCount();
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxLoanCount() {
        return maxLoanCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void loanBook() {
        if (this.maxLoanCount == 0) {
            throw new LoanLimitExceededException(String.format(ErrorMessages.LOAN_LIMIT_EXCEED, id));
        }
        this.maxLoanCount--;
    }

    public void returnBook() {
        this.maxLoanCount++;
    }

    @Override
    public String toString() {
        return String.format(
                "memeber - id: %d, name: %s, maxLoanCount: %d",
                id, name, maxLoanCount
        );
    }
}
