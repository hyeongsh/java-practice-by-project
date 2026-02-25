package repository;

import domain.Book;
import domain.Loan;
import domain.Member;
import exception.ErrorMessages;
import exception.ParseException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryRepository {

    public Map<Long, Book> loadBooks(Path path) throws IOException {
        Map<Long, Book> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                if (info.length != 4) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "books"));
                }
                try {
                    long id = Long.parseLong(info[0]);
                    String title = info[1];
                    String author = info[2];
                    boolean loaned = Boolean.parseBoolean(info[3]);
                    Book book = new Book(id, title, author, loaned);
                    map.put(id, book);
                } catch (IllegalArgumentException ex) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "books"));
                }
            }
            return map;
        }
    }

    public Map<Long, Member> loadMembers(Path path) throws IOException {
        Map<Long, Member> map = new HashMap<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                if (info.length != 3) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "members"));
                }
                try {
                    long id = Long.parseLong(info[0]);
                    String name = info[1];
                    int maxLoanCount = Integer.parseInt(info[2]);
                    Member member = new Member(id, name, maxLoanCount);
                    map.put(id, member);
                } catch (IllegalArgumentException ex) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "members"));
                }
            }
            return map;
        }
    }

    public List<Loan> loadLoans(Path path) throws IOException {
        List<Loan> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                if (info.length != 5) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "loans"));
                }
                try {
                    long bookId = Long.parseLong(info[0]);
                    long memberId = Long.parseLong(info[1]);
                    LocalDate loanDate = LocalDate.parse(info[2]);
                    LocalDate dueDate = LocalDate.parse(info[3]);
                    boolean returned = Boolean.parseBoolean(info[4]);
                    Loan loan = new Loan(bookId, memberId, loanDate, dueDate, returned);
                    list.add(loan);
                } catch (IllegalArgumentException ex) {
                    throw new ParseException(String.format(ErrorMessages.PARSE_ERROR, "loans"));
                }
            }
            return list;
        }
    }

    public void saveBook(Path path, Map<Long, Book> bookMap) {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Book book : bookMap.values()) {
                String line = String.format("%d,%s,%s,%b", book.getId(), book.getTitle(), book.getAuthor(), book.isLoaned());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("save failed");
        }
    }

    public void saveMember(Path path, Map<Long, Member> memberMap) {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Member member : memberMap.values()) {
                String line = String.format("%d,%s,%d", member.getId(), member.getName(), member.getMaxLoanCount());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void saveLoan(Path path, List<Loan> loanList) {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (Loan loan : loanList) {
                String line = String.format("%d,%d,%s,%s,%b", loan.getBookId(), loan.getMemberId(), loan.getLoanDate(), loan.getDueDate(), loan.isReturned());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
