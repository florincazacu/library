package com.example.library.controllers;

import com.example.library.model.Book;
import com.example.library.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> loanBook(@PathVariable Long id) {
        loanService.loanBook(id, getLoggedUser());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Book> returnBook(@PathVariable Long id) {
        loanService.returnBook(id);

        return new ResponseEntity<>(new Book(), HttpStatus.OK);
    }

    private String getLoggedUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getUsername();
    }
}
