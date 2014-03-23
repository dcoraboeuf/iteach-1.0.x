package net.nemerosa.iteach.service;

import org.springframework.security.authentication.AccountStatusException;

public class AccountOpenIDNotFoundException extends AccountStatusException {
    private final String identityUrl;

    public AccountOpenIDNotFoundException(String identityUrl) {
        super(identityUrl + " is not registered.");
        this.identityUrl = identityUrl;
    }

    public String getIdentityUrl() {
        return identityUrl;
    }
}
