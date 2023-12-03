package com.sep.onlinedeliverysystem.domain.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class VendorDetailsEntity implements UserDetails {

    private final Vendor vendor;

    public VendorDetailsEntity(Vendor vendor) {
        this.vendor = vendor;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Assuming all vendors have the same role for simplicity.
        return Collections.singletonList(new SimpleGrantedAuthority("VENDOR"));
    }

    @Override
    public String getPassword() {
        return this.vendor.getPassword();
    }

    @Override
    public String getUsername() {
        return this.vendor.getEmail();
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
