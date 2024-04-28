package com.example.project;

import com.example.project.config.SecurityConfig;
import com.example.project.controller.HomeController;
import com.example.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(properties = {"spring.config.location=classpath:application-test.properties"}, value = HomeController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class HomeControllerSecurityTests {

    @Autowired
    private MockMvc mvc;

    //For Import
    @MockBean
    UserRepository repo;

    @Test
    @WithAnonymousUser
    void checkIndexPage() throws Exception {
        mvc.perform(get("/index")).andExpect(status().isOk());
        mvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void checkLoginPage() throws Exception {
        mvc.perform(get("/login")).andExpect(status().isOk());
    }

}
