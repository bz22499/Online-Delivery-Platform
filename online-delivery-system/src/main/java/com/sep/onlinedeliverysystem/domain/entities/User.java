package com.sep.onlinedeliverysystem.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.Serializable;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
    @Id
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming all vendors have the same role for simplicity.
        return Collections.singletonList(new SimpleGrantedAuthority("VENDOR"));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
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
