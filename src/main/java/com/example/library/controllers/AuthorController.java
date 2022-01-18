package com.example.library.controllers;

import com.example.library.model.Author;
import com.example.library.services.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<Set<Author>> getAllAuthors() {
        Set<Author> authors = authorService.findAll();

        if (CollectionUtils.isEmpty(authors)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Author> findAuthorById(@PathVariable Long id) {
        Author author = authorService.findById(id);

        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<Author> findAuthorByTitle(@RequestParam(value = "firstName") String firstName,
                                                        @RequestParam(value = "lastName") String lastName) {
        Author author = authorService.find(firstName, lastName);

        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAuthor(Author author) {
        authorService.save(author);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        authorService.update(id, author);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> patchAuthor(@PathVariable Long id, @RequestBody Author author) {
        authorService.updatePartial(id, author);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/update/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        authorService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
