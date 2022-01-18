package com.example.library.controllers;

import com.example.library.repositories.security.UserRepository;
import com.example.library.model.security.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RolesAllowed("ADMIN")
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Set<User>> getAll() {
        Set<User> users = new HashSet<>();

        userRepository.findAll().forEach(users::add);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        return userOptional.map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/find")
    public ResponseEntity<User> findUser(@RequestParam(value = "firstName") String firstName,
                                         @RequestParam(value = "lastName") String lastName) {
        User user = userRepository.findByFirstNameAndLastName(firstName, lastName);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}
