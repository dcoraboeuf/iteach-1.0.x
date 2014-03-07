package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Qualifier("password")
@Transactional
public class PasswordAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordAuthenticationProvider(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String rawPassword = (String) authentication.getCredentials();
        boolean ok = accountRepository.checkPassword(
                ((AccountAuthentication) userDetails).getId(),
                encodedPassword -> passwordEncoder.matches(rawPassword, encodedPassword)
        );
        if (!ok) {
            throw new BadCredentialsException("Incorrect password");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        TAccount t = accountRepository.findUserByUsernameForPasswordMode(username);
        if (t != null) {
            return new AccountAuthenticationDetails(
                    t.getId(),
                    t.getName(),
                    t.getEmail(),
                    t.isAdministrator(),
                    t.getAuthenticationMode());
        } else {
            throw new UsernameNotFoundException(String.format("User %s cannot be found", username));
        }
    }
}
