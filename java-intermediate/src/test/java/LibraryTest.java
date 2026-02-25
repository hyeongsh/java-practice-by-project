import domain.Book;
import domain.Member;
import exception.*;
import org.junit.jupiter.api.Test;
import service.LibraryService;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {

    LibraryService libraryService = new LibraryService();

    @Test
    public void registerBookAndGetByIdAndAll() {
        Book book1 = new Book(1, "abc", "123");
        Book book2 = new Book(2, "bcd", "123");
        Book book3 = new Book(3, "cde", "123");
        libraryService.registerBook(book1);
        libraryService.registerBook(book2);
        libraryService.registerBook(book3);

        assertEquals("abc", libraryService.getBookById(1).getTitle());
        assertEquals(3, libraryService.getAllBook().size());
    }

    @Test
    public void registerBookDuplicate() {
        Book book1 = new Book(1, "abc", "123");
        Book book2 = new Book(1, "bcd", "123");
        libraryService.registerBook(book1);
        assertThrows(DuplicateBookException.class, () -> libraryService.registerBook(book2));
    }

    @Test
    public void registerMemberAndGetByIdAndAll() {
        Member member1 = new Member(1, "qwe");
        Member member2 = new Member(2, "wer");
        Member member3 = new Member(3, "ert");
        libraryService.registerMember(member1);
        libraryService.registerMember(member2);
        libraryService.registerMember(member3);

        assertEquals("qwe", libraryService.getMemberById(1).getName());
        assertEquals(3, libraryService.getAllMember().size());
    }

    @Test
    public void registerMemberDuplicate() {
        Member member1 = new Member(1, "qwe");
        Member member2 = new Member(1, "wer");
        libraryService.registerMember(member1);
        assertThrows(DuplicateMemberException.class, () -> libraryService.registerMember(member2));
    }

    @Test
    public void loanAndReturn() {
        Book book1 = new Book(1, "abc", "123");
        Book book2 = new Book(2, "bcd", "123");
        Book book3 = new Book(3, "cde", "123");
        libraryService.registerBook(book1);
        libraryService.registerBook(book2);
        libraryService.registerBook(book3);
        Member member1 = new Member(1, "qwe");
        Member member2 = new Member(2, "wer");
        Member member3 = new Member(3, "ert");
        libraryService.registerMember(member1);
        libraryService.registerMember(member2);
        libraryService.registerMember(member3);

        libraryService.loanBook(1, 1);
        assertThrows(BookNotFoundException.class, () -> libraryService.loanBook(4, 2));
        assertThrows(AlreadyLoanedException.class, () -> libraryService.loanBook(1, 2));
        assertTrue(libraryService.getBookById(1).isLoaned());
        assertEquals(4, libraryService.getMemberById(1).getMaxLoanCount());

        assertThrows(LoanNotFoundException.class, () -> libraryService.returnBook(1, 2));
        libraryService.returnBook(1, 1);
        assertFalse(libraryService.getBookById(1).isLoaned());
        assertEquals(5, libraryService.getMemberById(1).getMaxLoanCount());

    }

}
