package service;

import domain.Book;
import domain.Loan;
import domain.Member;
import exception.*;
import repository.LibraryRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryService {

    private final Path BOOK_PATH = Path.of("java-intermediate/src/main/java/data/books.csv");
    private final Path MEMBER_PATH = Path.of("java-intermediate/src/main/java/data/members.csv");
    private final Path LOAN_PATH = Path.of("java-intermediate/src/main/java/data/loans.csv");

    private final LibraryRepository libraryRepository;

    // id 기준으로 바로 찾을 수 있도록 저장
    private final Map<Long, Book> bookMap;
    private final Map<Long, Member> memberMap;

    // BookId로 찾을 수도 있고, MemberId로 찾을 수도 있어 기준이 모호함. 탐색효율이 비슷하다면 저장을 쉽게 하는 것이 더 효율적이라 판단
    private final List<Loan> loanList;

    public LibraryService() {
        libraryRepository = new LibraryRepository();
        Map<Long, Book> tmpBookMap = null;
        Map<Long, Member> tmpMemberMap = null;
        List<Loan> tmpLoanList = null;
        try {
            tmpBookMap = libraryRepository.loadBooks(BOOK_PATH);
            tmpMemberMap = libraryRepository.loadMembers(MEMBER_PATH);
            tmpLoanList = libraryRepository.loadLoans(LOAN_PATH);
        } catch (Exception ex) {
            tmpBookMap = new HashMap<>();
            tmpMemberMap = new HashMap<>();
            tmpLoanList = new ArrayList<>();
            System.out.println("load fail");
        } finally {
            this.bookMap = tmpBookMap;
            this.memberMap = tmpMemberMap;
            this.loanList = tmpLoanList;
        }
    }

    public void save() {
        libraryRepository.saveBook(BOOK_PATH, bookMap);
        libraryRepository.saveMember(MEMBER_PATH, memberMap);
        libraryRepository.saveLoan(LOAN_PATH, loanList);
    }

    public void registerBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "book"));
        }
        book = new Book(book);
        Book prev = bookMap.putIfAbsent(book.getId(), book);
        if (prev != null) {
            throw new DuplicateBookException(String.format(ErrorMessages.DUPLICATE_BOOK, book.getId()));
        }
    }

    public List<Book> getAllBook() {
        return bookMap.values().stream().map(Book::new).toList();
    }

    public Book getBookById(long bookId) {
        Book book = bookMap.get(bookId);
        if (book == null) {
            throw new BookNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND, bookId));
        }
        return new Book(bookMap.get(bookId));
    }

    public void registerMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException(String.format(ErrorMessages.INVALID_PARAMETER, "member"));
        }
        member = new Member(member);
        Member prev = memberMap.putIfAbsent(member.getId(), member);
        if (prev != null) {
            throw new DuplicateMemberException(String.format(ErrorMessages.DUPLICATE_MEMBER, member.getId()));
        }
    }

    public List<Member> getAllMember() {
        return memberMap.values().stream().map(Member::new).toList();
    }

    public Member getMemberById(long memberId) {
        Member member = memberMap.get(memberId);
        if (member == null) {
            throw new MemberNotFoundException(String.format(ErrorMessages.MEMBER_NOT_FOUND, memberId));
        }
        return new Member(memberMap.get(memberId));
    }

    public void loanBook(long bookId, long memberId) {
        Book book = bookMap.get(bookId);
        Member member = memberMap.get(memberId);
        if (book == null) {
            throw new BookNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND, bookId));
        } else if (member == null) {
            throw new MemberNotFoundException(String.format(ErrorMessages.MEMBER_NOT_FOUND, memberId));
        }
        if (!book.isLoaned() && member.getMaxLoanCount() > 0) {
            book.loan();
            member.loanBook();
            loanList.add(new Loan(bookId, memberId, LocalDate.now(), LocalDate.now().plusWeeks(2)));
        } else if (book.isLoaned()) {
            throw new AlreadyLoanedException(String.format(ErrorMessages.ALREADY_LOANED, bookId));
        } else {
            throw new LoanLimitExceededException(String.format(ErrorMessages.LOAN_LIMIT_EXCEED, memberId));
        }
    }

    public void returnBook(long bookId, long memberId) {
        Book book = bookMap.get(bookId);
        Member member = memberMap.get(memberId);
        if (book == null) {
            throw new BookNotFoundException(String.format(ErrorMessages.BOOK_NOT_FOUND, bookId));
        } else if (member == null) {
            throw new MemberNotFoundException(String.format(ErrorMessages.MEMBER_NOT_FOUND, memberId));
        }
        Loan loan = null;
        for (Loan cur : this.loanList) {
            if (cur.getBookId() == bookId && cur.getMemberId() == memberId && !cur.isReturned()) {
                loan = cur;
                break ;
            }
        }
        if (loan == null) {
            throw new LoanNotFoundException(String.format(ErrorMessages.LOAN_NOT_FOUND, bookId));
        }
        loan.returnBook();
        book.returnBook();
        member.returnBook();
    }

    public List<Loan> getLoanedList() {
        return loanList.stream().filter(l -> !l.isReturned()).map(Loan::new).toList();
    }

    public List<Loan> getLoanListByMemberId(long id) {
        return loanList.stream().filter(l -> l.getMemberId() == id).map(Loan::new).toList();
    }
}
