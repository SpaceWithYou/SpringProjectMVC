package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;
import java.util.Optional;

/**Rest Controller for users*/
@RestController
@Secured("hasRole('SUPER_USER')")
public class AdminUsersController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    //Path const
    private static final String path = "/admin/users/";

    @GetMapping(path + "{id}")
    public String getUser(@PathVariable long id) {
        Optional<User> opt = userRepo.findById(id);
        if(opt.isPresent()) {
            return opt.get().toString();
        }
        return "null";
    }

    @GetMapping(path)
    public String getAllUser() {
        Iterator<User> iter = userRepo.findAll().iterator();
        StringBuilder builder = new StringBuilder();
        while (iter.hasNext()) {
            builder.append(iter.next());
            builder.append('\n');
        }
        return builder.toString();
    }

    @PostMapping(path)
    public String createUser(@RequestBody User user) {
        //encode raw password
        user.setPassword(encoder.encode(user.getPassword()));
        long id = userRepo.save(user).getId();
        return "UserCreated with id = " + id;
    }

    @PutMapping(path + "{id}")
    public String updateUser(@PathVariable long id, @RequestBody User user) {
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isEmpty()) return "No user with id = " + id;
        user.setId(userOpt.get().getId());
        //encode raw password
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
        return user.toString();
    }

    @DeleteMapping(path + "{id}")
    public String deleteUser(@PathVariable long id, Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        //Forbid deleting self
        if(details.getUser().getId() == id) return "Cannot delete this user";
        userRepo.deleteById(id);
        return "User deleted";
    }
}
