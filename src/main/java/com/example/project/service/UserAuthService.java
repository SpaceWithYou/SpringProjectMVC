package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAuthService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = repo.findByName(username);
        if(userOpt.isEmpty()) throw new UsernameNotFoundException("User not found");
        return new com.example.project.util.UserDetails(userOpt.get());
    }

    public boolean checkPassword(String userName, String password) throws UsernameNotFoundException {
        UserDetails details = loadUserByUsername(userName);
        return password.equals(details.getPassword());
    }
}
