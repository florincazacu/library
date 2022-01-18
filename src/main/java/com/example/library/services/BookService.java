package com.example.library.services;


import com.example.library.model.Author;
import com.example.library.model.Book;

import java.util.Set;


public interface BookService extends CrudService<Book, Long> {

    Set<Book> findByTitleAndAuthor(String title, Author author);

    Set<Book> findByAuthor(Author author);

    Set<Book> findByTitle(String title);

    void update(Long id, Book book);


    void updatePartial(Long id, Book book);

    void deleteAll();
}
