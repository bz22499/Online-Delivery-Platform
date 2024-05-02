package com.sep.onlinedeliverysystem.domain.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsEntity implements UserDetails {

    private final User user;

    public UserDetailsEntity(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming all users have the same role for simplicity.
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // You can implement your own logic here if needed.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // You can implement your own logic here if needed.
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // You can implement your own logic here if needed.
        return true;
    }

    @Override
    public boolean isEnabled() {
        // You can implement your own logic here if needed.
        return true;
    }
}