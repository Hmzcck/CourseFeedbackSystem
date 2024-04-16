package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor-based injection (recommended by Spring)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public UserModel findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public UserModel findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    System.out.println("Failed to find user with ID: " + id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });
    }

}
