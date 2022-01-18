package com.example.library.repositories.security;

import com.example.library.model.security.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    User findByFirstNameAndLastName(String firstName, String lastName);
}
