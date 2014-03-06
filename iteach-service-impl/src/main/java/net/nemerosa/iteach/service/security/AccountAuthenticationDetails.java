package net.nemerosa.iteach.service.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AccountAuthenticationDetails implements UserDetails, AccountAuthentication {

    private final int id;
    private final boolean administrator;
    private final String email;

    public AccountAuthenticationDetails(int id, boolean administrator, String email) {
        this.id = id;
        this.administrator = administrator;
        this.email = email;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isAdministrator() {
        return administrator;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

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
