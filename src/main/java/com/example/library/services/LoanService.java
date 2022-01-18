package com.example.library.services;

import com.example.library.model.Loan;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public interface LoanService extends CrudService<Loan, Long> {

    Set<Loan> findAll();

    void loanBook(Long id, String username);

    void returnBook(Long id);

    void deleteAll();

    void saveAll(Set<Loan> loans);
}
