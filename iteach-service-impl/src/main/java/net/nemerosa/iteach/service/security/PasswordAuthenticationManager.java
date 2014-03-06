package net.nemerosa.iteach.service.security;

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
public class PasswordAuthenticationManager extends AbstractUserDetailsAuthenticationProvider {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordAuthenticationManager(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        String encodedPassword = passwordEncoder.encode((String) authentication.getCredentials());
        boolean passwordOk = accountRepository.checkPassword(userDetails.getUsername(), encodedPassword);
        if (!passwordOk) {
            throw new BadCredentialsException("Incorrect password");
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        TAccount t = accountRepository.findUserByUsernameForPasswordMode(username);
        if (t != null) {
            return new AccountAuthenticationDetails(
                    t.getId(),
                    t.isAdministrator(),
                    t.getEmail()
            );
        } else {
            throw new UsernameNotFoundException(String.format("User %s cannot be found", username));
        }
    }
}
