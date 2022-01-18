package com.example.library.controllers;

import com.example.library.BaseTest;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.services.BookService;
import com.example.library.services.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoanControllerTest extends BaseTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private BookService bookService;

    @Test
    void loanBookNotAuth() throws Exception {
        mockMvc.perform(post("/loans/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @ParameterizedTest(name = "#{index} with [{arguments}]")
    @MethodSource("com.example.library.controllers.BookControllerTest#getStreamNotAdmin")
    void loanBookAuth(String user, String password) throws Exception {
        Book book = bookService.findAll().stream().findFirst().get();
        mockMvc.perform(post("/loans/" + book.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void returnBookNotAuth() throws Exception {
        mockMvc.perform(put("/loans/return/" + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void returnBookAuth() throws Exception {
        String user = "user1";
        String password = "password";

        // find a book
        Book book = bookService.findAll().stream().findFirst().get();

        // ensure book is not already loaned by user
        Set<Loan> loans = loanService.findAll();

        for (Loan loan : loans) {
            assertNotEquals(loan.getUser().getUsername(), user);
        }

        // loan book
        mockMvc.perform(post("/loans/" + book.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // verify that user has loaned book
        loans = loanService.findAll();
        Loan loan = loans.stream().filter(loan1 ->
                loan1.getUser().getUsername().equals(user)).findFirst().orElse(null);

        assertNotNull(loan);

        // return the book

        mockMvc.perform(put("/loans/return/" + book.getId())
                        .with(httpBasic(user, password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // ensure book is not loaned anymore
        loans = loanService.findAll();

        loan = loans.stream().filter(loan1 ->
                loan1.getUser().getUsername().equals(user)).findFirst().orElse(null);

        assertNotNull(loan);

        assertNull(loan.getBook());
    }
}