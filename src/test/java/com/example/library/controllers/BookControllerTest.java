package com.example.library.controllers;

import com.example.library.BaseTest;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.services.BookService;
import com.example.library.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookControllerTest extends BaseTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private LoanService loanService;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests {

        @Test
        void deleteBookNotAuth() throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            mockMvc.perform(delete("/books/delete/" + savedBook.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamNotAdmin")
        @Transient
        void deleteBookUserAuth(String user, String pwd) throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            mockMvc.perform(delete("/books/delete/" + savedBook.getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAdmin")
        @Transient
        void deleteBookAdminAuth(String user, String pwd) throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            mockMvc.perform(delete("/books/delete/" + savedBook.getId())
                            .with(httpBasic(user, pwd)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Tests")
    @Nested
    class GetTests {
        @Test
        void getAllBooksNotAuth() throws Exception {
            mockMvc.perform(get("/books"))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        void getAllBooksAuth(String user, String password) throws Exception {
            mockMvc.perform(get("/books").with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        @Transient
        void getAllBooksAuthNotFound(String user, String password) throws Exception {
            Set<Loan> loans = new HashSet<>(loanService.findAll());

            for (Loan loan : loans) {
                loanService.delete(loan);
            }

            bookService.deleteAll();
            mockMvc.perform(get("/books").with(httpBasic(user, password)))
                    .andExpect(status().isNotFound());

        }

        @Test
        void findBookByIdNotAuth() throws Exception {
            Book book = bookService.findAll().iterator().next();

            mockMvc.perform(get("/books/find/" + book.getId()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        void findBookByIdAuth(String user, String password) throws Exception {
            Book book = bookService.findAll().iterator().next();

            mockMvc.perform(get("/books/find/" + book.getId())
                            .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }

        @Test
        void findBookByTitle() throws Exception {
            Book book = bookService.findAll().iterator().next();

            mockMvc.perform(get("/books/find?title=" + book.getTitle()))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        void findBookByTitleAuth(String user, String password) throws Exception {
            Book book = bookService.findAll().iterator().next();

            mockMvc.perform(get("/books/find?title=" + book.getTitle())
                            .with(httpBasic(user, password)))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        void findBookByIdNotFound(String user, String password) throws Exception {

            mockMvc.perform(get("/books/find/" + -1)
                            .with(httpBasic(user, password)))
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAllUsers")
        void findBookByTitleNotFound(String user, String password) throws Exception {
            mockMvc.perform(get("/books/find?title=" + "dummyTitle")
                            .with(httpBasic(user, password)))
                    .andExpect(status().isNotFound());
        }
    }

    @DisplayName("Post Tests")
    @Nested
    class PostTests {
        @Test
        void addBookNotAuth() throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);
            String json = new ObjectMapper().writeValueAsString(book);

            mockMvc.perform(post("/books/add").contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAdmin")
        void addBookAuth(String user, String password) throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);
            String json = new ObjectMapper().writeValueAsString(book);

            mockMvc.perform(post("/books/add")
                            .with(httpBasic(user, password))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }
    }

    @DisplayName("Put Tests")
    @Nested
    class PutTests {
        @Test
        void updateBookNotAuth() throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            book.setTitle("Updated");
            String json = new ObjectMapper().writeValueAsString(book);

            mockMvc.perform(put("/books/" + savedBook.getId()).contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAdmin")
        void updateBookAuth(String user, String password) throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            book = new Book("Updated", 4);
            String json = new ObjectMapper().writeValueAsString(book);

            mockMvc.perform(put("/books/update/" + savedBook.getId())
                            .with(httpBasic(user, password))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            Book updatedBook = bookService.findById(savedBook.getId());

            assertNotNull(updatedBook);
            assertNotEquals(updatedBook, savedBook);
            assertEquals("Updated", updatedBook.getTitle());
        }
    }

    @DisplayName("Put Tests")
    @Nested
    class PatchTests {
        @Test
        void updateBookNotAuth() throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            String json = new ObjectMapper().writeValueAsString(book);

            book.setTitle("Updated");

            mockMvc.perform(patch("/books/" + savedBook.getId()).contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("com.example.library.controllers.BookControllerTest#getStreamAdmin")
        void updateBookAuth(String user, String password) throws Exception {
            Book book = new Book("The Pragmatic Programmer", 4);

            Book savedBook = bookService.save(book);

            String json = new ObjectMapper().writeValueAsString(book);

            book.setTitle("Updated");

            mockMvc.perform(patch("/books/update/" + savedBook.getId())
                            .with(httpBasic(user, password))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }
    }

}