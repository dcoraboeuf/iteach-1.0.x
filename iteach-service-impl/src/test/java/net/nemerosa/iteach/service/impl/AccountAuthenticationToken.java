package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.AccountAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class AccountAuthenticationToken implements Authentication {

    private final AccountAuthentication account;

    @Autowired
    public AccountAuthenticationToken(AccountAuthentication account) {
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptySet();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        // TODO Name
        return account.getEmail();
    }
}
