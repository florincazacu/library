package com.example.library.repositories;

import com.example.library.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface BookRepository  extends CrudRepository<Book, Long> {

	Set<Book> searchByTitleLike(String title);

}
