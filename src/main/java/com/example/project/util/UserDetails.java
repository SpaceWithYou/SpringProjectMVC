package com.example.project.util;

import com.example.project.entity.User;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.LinkedList;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        LinkedList<GrantedAuthority> res = new LinkedList<>();
        if(user.isSuperUser()) {
            res.add(new SimpleGrantedAuthority("SUPER_USER"));
        } else {
            res.add(new SimpleGrantedAuthority("USER"));
        }
        return res;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    public boolean isSuperUser() {
        return user.isSuperUser();
    }

    //TODO: Misc, hardcoded for new features
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
