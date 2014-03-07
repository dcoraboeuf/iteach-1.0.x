package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.common.AuthenticationMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class AccountAuthenticationDetails implements UserDetails, AccountAuthentication {

    private final int id;
    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;

    public AccountAuthenticationDetails(int id, String name, String email, boolean administrator, AuthenticationMode authenticationMode) {
        this.id = id;
        this.name = name;
        this.administrator = administrator;
        this.email = email;
        this.authenticationMode = authenticationMode;
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
    public String getName() {
        return name;
    }

    @Override
    public AuthenticationMode getAuthenticationMode() {
        return authenticationMode;
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
