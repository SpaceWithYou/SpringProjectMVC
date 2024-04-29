package com.example.project.config;

import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserAuthService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.ChangeSessionIdAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService detailsService() {
        return new UserAuthService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(detailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    /*url mappings
    * TODO possible to use preauthorize in method*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(custom -> custom
                                .requestMatchers("/index", "/", "/login", "/login/auth").permitAll()
                                .requestMatchers("/admin").hasRole("SUPER_USER")
                                .requestMatchers("/user").hasRole("USER")
                                .anyRequest().authenticated()
        )
                                .formLogin(login -> login
                                        .loginPage("/login")
                                )
                                .logout(logout -> logout
                                        .logoutSuccessUrl("/index")
                                )
                                .csrf(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable);
        http.setSharedObject(SessionAuthenticationStrategy.class, new ChangeSessionIdAuthenticationStrategy());
        return http.build();
    }

}
