package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class OpenIdAuthenticationUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    private final AccountRepository accountRepository;

    @Autowired
    public OpenIdAuthenticationUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        TAccount t = accountRepository.findUserByUsernameForOpenIDMode(token.getIdentityUrl());
        if (t != null) {
            if (t.isVerified() && !t.isDisabled()) {
                return new AccountAuthenticationDetails(
                        t.getId(),
                        t.getName(),
                        t.getEmail(),
                        t.isAdministrator(),
                        t.getAuthenticationMode()
                );
            } else {
                throw new AccountNonVerifiedOrDisabledException();
            }
        } else {
           throw new AccountOpenIDNotFoundException(token.getIdentityUrl());
        }
    }

}
