package com.example.library.services;

import com.example.library.model.Author;

public interface AuthorService extends CrudService<Author, Long> {

    Author find(String firstName, String lastName);
}
