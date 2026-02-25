package domain;

import exception.AlreadyLoanedException;
import exception.ErrorMessages;
import exception.LoanNotFoundException;

public class Book {

    private final long id;
    private final String title;
    private final String author;
    private boolean loaned;

    public Book(long id, String title, String author) {
        if (title == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "title"));
        } else if (author == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "author"));
        }
        this.id = id;
        this.title = title;
        this.author = author;
        this.loaned = false;
    }

    public Book(long id, String title, String author, boolean loaned) {
        if (title == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "title"));
        } else if (author == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "author"));
        }
        this.id = id;
        this.title = title;
        this.author = author;
        this.loaned = loaned;
    }

    public Book(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.loaned = book.isLoaned();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isLoaned() {
        return loaned;
    }

    public void loan() {
        if (this.loaned) {
            throw new AlreadyLoanedException(String.format(ErrorMessages.ALREADY_LOANED, this.id));
        }
        this.loaned = true;
    }

    public void returnBook() {
        if (!this.loaned) {
            throw new LoanNotFoundException(String.format(ErrorMessages.LOAN_NOT_FOUND, this.id));
        }
        this.loaned = false;
    }

    @Override
    public String toString() {
        return String.format(
                "book - id: %d, title: %s, author: %s, loaned: %b",
                id, title, author, loaned
        );
    }
}
