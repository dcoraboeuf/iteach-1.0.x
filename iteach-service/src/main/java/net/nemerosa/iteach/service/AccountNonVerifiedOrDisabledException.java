package net.nemerosa.iteach.service;

import org.springframework.security.core.AuthenticationException;

public class AccountNonVerifiedOrDisabledException extends AuthenticationException {

    public AccountNonVerifiedOrDisabledException(String msg) {
        super(msg);
    }

}
