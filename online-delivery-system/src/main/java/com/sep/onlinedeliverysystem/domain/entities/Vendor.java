package com.sep.onlinedeliverysystem.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vendors")
public class Vendor implements UserDetails {
    @Id
    private String email;
    private String name;
    private String password;
    private String description;
    private float rating;
    private String imageUrl;

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