package com.example.library.services.impl;

import com.example.library.model.Author;
import com.example.library.repositories.AuthorRepository;
import com.example.library.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author find(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Set<Author> findAll() {
        Set<Author> authors = new HashSet<>();
        authorRepository.findAll().forEach(authors::add);

        return authors;
    }

    @Override
    public Author findById(Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        return authorOptional.orElse(null);
    }

    @Override
    public Author save(Author entity) {
        return authorRepository.save(entity);
    }

    @Override
    public void update(Long id, Author authorDto) {
        Optional<Author> authorOptional = authorRepository.findById(id);

        authorOptional.ifPresentOrElse(author -> {
            author.setFirstName(authorDto.getFirstName());
            author.setLastName(authorDto.getLastName());
            authorRepository.save(author);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Author with id: %s not found", id));
        });
    }

    @Override
    public void updatePartial(Long id, Author authorDto) {
        Optional<Author> authorOptional = authorRepository.findById(id);

        authorOptional.ifPresentOrElse(author -> {
            if (authorDto.getFirstName() != null) {
                author.setFirstName(authorDto.getFirstName());
            }
            if (authorDto.getLastName() != null) {
                author.setLastName(authorDto.getLastName());
            }
            authorRepository.save(author);
        }, () -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Author with id: %s not found", id));
        });
    }

    @Override
    public void delete(Author entity) {
        authorRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}
