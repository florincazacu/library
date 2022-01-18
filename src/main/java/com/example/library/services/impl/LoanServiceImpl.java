package com.example.library.services.impl;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.security.User;
import com.example.library.repositories.BookRepository;
import com.example.library.repositories.LoanRepository;
import com.example.library.repositories.security.UserRepository;
import com.example.library.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.BadRequestException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Set<Loan> findAll() {
        Set<Loan> loans = new HashSet<>();
        loanRepository.findAll().forEach(loans::add);
        return loans;
    }

    @Override
    public Loan findById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public Loan save(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public void saveAll(Set<Loan> loans) {
        loanRepository.saveAll(loans);
    }

    @Override
    public void delete(Loan loan) {
        loanRepository.delete(loan);
    }

    @Override
    public void deleteById(Long id) {
        loanRepository.deleteById(id);
    }

    @Override
    public void updatePartial(Long aLong, Loan entity) {

    }

    @Override
    public void update(Long id, Loan loanDto) {
        Optional<Loan> loanOptional = loanRepository.findById(id);

        loanOptional.ifPresentOrElse(loan -> {
            loan.setLoanDate(loanDto.getLoanDate());
            loan.setReturnDate(loanDto.getReturnDate());
            loan.setUser(loanDto.getUser());
            loan.setBook(loanDto.getBook());
            loanRepository.save(loan);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book with id: %s not found", id));
        });

    }

    @Override
    public void loanBook(Long id, String username) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book not found");
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new BadRequestException("User not found");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(computeCurrentDate());

        loanRepository.save(loan);

        book.decreaseStock();
        book.setLoan(loan);

        bookRepository.save(book);
    }

    @Override
    public void returnBook(Long id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            throw new BadRequestException("Book not found");
        }

        Loan loan = loanRepository.findById(book.getLoan().getId()).orElse(null);

        if (loan == null) {
            throw new BadRequestException("Book is not loaned");
        }

        book.increaseStock();
        book.setLoan(null);

        loan.setReturnDate(computeCurrentDate());
        loan.setBook(null);

        bookRepository.save(book);

        loanRepository.save(loan);

    }

    @Override
    public void deleteAll() {
        Set<Loan> loans = new HashSet<>();
        loanRepository.findAll().forEach(loans::add);

        for (Loan loan : loans) {
            if (loan != null && loan.getBook() != null) {
                Book book = loan.getBook();
                book.setLoan(null);
                bookRepository.save(book);
                loanRepository.delete(loan);
            }
        }

    }

    private String computeCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy");
        return LocalDate.now().format(formatter);
    }
}
