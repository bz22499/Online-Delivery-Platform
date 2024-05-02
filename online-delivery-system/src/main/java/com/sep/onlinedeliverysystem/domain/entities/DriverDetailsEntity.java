package com.sep.onlinedeliverysystem.domain.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class DriverDetailsEntity implements UserDetails {

    private final Driver driver;

    public DriverDetailsEntity(Driver driver) {
        this.driver = driver;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming all vendors have the same role for simplicity.
        return Collections.singletonList(new SimpleGrantedAuthority("DRIVER"));
    }

    @Override
    public String getPassword() {
        return this.driver.getPassword();
    }

    @Override
    public String getUsername() {
        return this.driver.getEmail();
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
