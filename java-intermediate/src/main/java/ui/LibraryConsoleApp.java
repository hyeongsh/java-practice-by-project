package ui;

import domain.Book;
import domain.Loan;
import domain.Member;
import service.LibraryService;

import java.io.*;
import java.util.List;

public class LibraryConsoleApp {

    private final LibraryService libraryService;
    private final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public LibraryConsoleApp() {
        this.libraryService = new LibraryService();
    }

    public void run() {
        boolean endFlag = false;
        while (true) {
            if (endFlag) {
                break ;
            }
            int number = printMenu();
            switch (number) {
                case 1:
                    registerBook();
                    break ;
                case 2:
                    registerMember();
                    break ;
                case 3:
                    loanBook();
                    break ;
                case 4:
                    returnBook();
                    break ;
                case 5:
                    getBookList();
                    break ;
                case 6:
                    getMemberList();
                    break ;
                case 7:
                    getLoanedList();
                    break ;
                case 8:
                    getLoanListByMember();
                    break ;
                case 9:
                    save();
                    break ;
                case 0:
                    save();
                    endFlag = true;
                    break ;
            }
        }
    }

    private int printMenu() {
        System.out.println("""
                1. 도서 등록
                2. 회원 등록
                3. 도서 대출
                4. 도서 반납
                5. 도서 목록 조회
                6. 회원 목록 조회
                7. 현재 대출 목록 조회
                8. 회원별 대출 조회
                9. 저장
                0. 종료
                """);
        try {
            String number = br.readLine();
            return Integer.parseInt(number);
        } catch (IOException ex) {
            System.err.println("Input error. please retry.");
        } catch (Exception ex) {
            System.err.println("Wrong answer. please retry." + ex.getMessage());
        }
        return -1;
    }

    private void registerBook() {
        try {
            System.out.println("id: ");
            long id = Long.parseLong(br.readLine());
            System.out.println("title: ");
            String title = br.readLine();
            System.out.println("author: ");
            String author = br.readLine();
            Book book = new Book(id, title, author);
            libraryService.registerBook(book);
        } catch (IOException e) {
            System.err.println("Input error. please retry.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void registerMember() {
        try {
            System.out.println("id: ");
            long id = Long.parseLong(br.readLine());
            System.out.println("name: ");
            String name = br.readLine();
            Member member = new Member(id, name);
            libraryService.registerMember(member);
        } catch (IOException e) {
            System.err.println("Input error. please retry.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void loanBook() {
        try {
            System.out.println("bookId: ");
            long bookId = Long.parseLong(br.readLine());
            System.out.println("memberId: ");
            long memberId = Long.parseLong(br.readLine());
            libraryService.loanBook(bookId, memberId);
        } catch (IOException e) {
            System.err.println("Input error. please retry.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void returnBook() {
        try {
            System.out.println("bookId: ");
            long bookId = Long.parseLong(br.readLine());
            System.out.println("memberId: ");
            long memberId = Long.parseLong(br.readLine());
            libraryService.returnBook(bookId, memberId);
        } catch (IOException e) {
            System.err.println("Input error. please retry.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getBookList() {
        try {
            List<Book> allBook = libraryService.getAllBook();
            for (Book book : allBook) {
                System.out.println(book.toString() + "\n");
            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getMemberList() {
        try {
            List<Member> allMember = libraryService.getAllMember();
            for (Member member : allMember) {
                System.out.println(member.toString() + "\n");
            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getLoanedList() {
        try {
            List<Loan> allLoan = libraryService.getLoanedList();
            for (Loan loan : allLoan) {
                System.out.println(loan.toString() + "\n");
            }
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void getLoanListByMember() {
        try {
            System.out.println("memberId: ");
            long memberId = Long.parseLong(br.readLine());
            List<Loan> allLoan = libraryService.getLoanListByMemberId(memberId);
            for (Loan loan : allLoan) {
                System.out.println(loan.toString() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Input error. please retry.");
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void save() {
        try {
            libraryService.save();
        } catch (RuntimeException ex) {
            System.err.println(ex.getMessage());
        }
    }

}
