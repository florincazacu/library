package com.example.library.services.security;

import com.example.library.model.security.User;
import com.example.library.model.security.UserDto;
import com.example.library.repositories.security.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.BadRequestException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByUsername(username);

        if (existingUser == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }

        return existingUser;
    }

    public void register(UserDto userDto) {
        User existingUser = userRepository.findByUsername(userDto.getUsername());

        if (existingUser != null) {
            throw new BadRequestException(String.format("User %s already exists", userDto.getUsername()));
        }

        User user = new User(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());

        userRepository.save(user);
    }
}
