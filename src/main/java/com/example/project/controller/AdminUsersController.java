package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;
import java.util.Optional;

/**Rest Controller for users*/
@RestController
@Secured("hasRole('SUPER_USER')")
public class AdminUsersController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/admin/users/{id}")
    public String getUser(@PathVariable long id) {
        return userRepo.findById(id).toString();
    }

    @GetMapping("/admin/users/}")
    public String getAllUser() {
        Iterator<User> iter = userRepo.findAll().iterator();
        StringBuilder builder = new StringBuilder();
        while (iter.hasNext()) {
            builder.append(iter.next());
            builder.append('\n');
        }
        return builder.toString();
    }

    @PostMapping("admin/users/")
    public String createUser(@ModelAttribute User user) {
        long id = userRepo.save(user).getId();
        //encode raw password
        user.setPassword(encoder.encode(user.getPassword()));
        return "UserCreated with id = " + id;
    }

    @PutMapping("admin/users/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute User user) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) return "No user with id = " + id;
        user.setId(userOpt.get().getId());
        //encode raw password
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return user.toString();
    }

    @DeleteMapping("admin/users/{id}")
    public String deleteUser(@PathVariable long id) {
        userRepo.deleteById(id);
        return "User deleted";
    }
}
