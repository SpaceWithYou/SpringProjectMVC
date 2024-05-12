package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = repo.findByName(username);
        if(userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");
        return new com.example.project.util.UserDetails(userOpt.get());
    }

    /**Adds one admin with hashed password*/
    @PostConstruct
    private void addAdmin() {
        if(repo.findByName("Admin").isEmpty()) {
            String rawPassword = UUID.randomUUID().toString();
            System.out.println("Raw password:");
            System.out.println(rawPassword);
            System.out.println();
            repo.save(new User("Admin", encoder.encode(rawPassword), true));
        };
    }
}
