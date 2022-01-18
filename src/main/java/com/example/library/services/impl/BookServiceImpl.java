package com.example.library.services.impl;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.repositories.BookRepository;
import com.example.library.services.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Set<Book> findAll() {
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);
        return books;
    }

    @Override
    public Book findById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.orElse(null);
    }

    @Override
    public Set<Book> findByTitleAndAuthor(String title, Author author) {
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);

        Set<Book> foundBooks = new HashSet<>();

        for (Book book : books) {
            if (book.getTitle().equals(title)
            && book.getAuthors().contains(author)) {
                foundBooks.add(book);
            }
        }

        return foundBooks;
    }

    @Override
    public Set<Book> findByAuthor(Author author) {
        Set<Book> books = new HashSet<>();
        bookRepository.findAll().forEach(books::add);

        Set<Book> foundBooks = new HashSet<>();

        for (Book book : books) {
            if (book.getAuthors().contains(author)) {
                foundBooks.add(book);
            }
        }

        return foundBooks;
    }

    @Override
    public Set<Book> findByTitle(String title) {
        return bookRepository.searchByTitleLike(title);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void update(Long id, Book bookDto) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        bookOptional.ifPresentOrElse(book -> {
            book.setTitle(bookDto.getTitle());
            book.setCopies(bookDto.getCopies());
            book.setAuthors(bookDto.getAuthors());
            bookRepository.save(book);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book with id: %s not found", id));
        });
    }

    @Override
    public void updatePartial(Long id, Book bookDto) {
        Optional<Book> bookOptional = bookRepository.findById(id);

        bookOptional.ifPresentOrElse(book -> {

            if (bookDto.getTitle() != null) {
                book.setTitle(bookDto.getTitle());
            }

            if (bookDto.getCopies() != null) {
                book.setCopies(bookDto.getCopies());
            }

            if (bookDto.getAuthors() != null) {
                book.setAuthors(bookDto.getAuthors());
            }

            bookRepository.save(book);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book with id: %s not found", id));
        });
    }

    @Override
    public void delete(Book entity) {
        bookRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {

        Set<Book> books = findAll();

        for (Book book : books) {
            if (book.getLoan() == null) {
                bookRepository.delete(book);
            }
        }
    }
}
