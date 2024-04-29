package com.example.project;

import com.example.project.config.SecurityConfig;
import com.example.project.controller.AuthController;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(properties = {"spring.config.location=classpath:application-test.properties"}, value = AuthController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class AuthControllerSecurityTests {
    @Autowired
    private MockMvc mvc;

    //For Import
    @MockBean
    UserAuthService service;
    @MockBean
    UserRepository repo;

    @Test
    @WithAnonymousUser
    void checkUserPageWithAnon() throws Exception {
        mvc.perform(get("/user")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void checkUserPageWithUser() throws Exception {
        mvc.perform(get("/user")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_USER")
    void checkUserPageWithAdmin() throws Exception {
        mvc.perform(get("/user")).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void checkAdminPageWithAnon() throws Exception {
        mvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(roles = "USER")
    void checkAdminPageWithUser() throws Exception {
        mvc.perform(get("/admin")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_USER")
    void checkAdminPageWithAdmin() throws Exception {
        mvc.perform(get("/admin")).andExpect(status().isOk());
    }

}
