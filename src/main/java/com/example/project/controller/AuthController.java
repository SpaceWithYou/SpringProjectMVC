package com.example.project.controller;

import com.example.project.service.UserAuthService;
import com.example.project.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @Autowired
    UserAuthService service;

    @PostMapping("login/auth")
    public ModelAndView auth(@RequestParam("username") String username, @RequestParam("password") String password) {
        ModelAndView mv = new ModelAndView("login");
        try {
            UserDetails details = (UserDetails) service.loadUserByUsername(username);
            if(details.isSuperUser()) {
                return new ModelAndView("subSuperUserPage");
            } else {
                return new ModelAndView("subUserPage");
            }
        } catch (UsernameNotFoundException e) {
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/user")
    public ModelAndView userPage() {
        return new ModelAndView("userPage");
    }

    @GetMapping("/admin")
    public ModelAndView superUserPage() {
        return new ModelAndView("superUserPage");
    }
}
