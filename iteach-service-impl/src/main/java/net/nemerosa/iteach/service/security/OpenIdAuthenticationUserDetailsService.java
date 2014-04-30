package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.service.AccountNonVerifiedOrDisabledException;
import net.nemerosa.iteach.service.AccountOpenIDNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OpenIdAuthenticationUserDetailsService implements AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    private final AccountRepository accountRepository;

    @Autowired
    public OpenIdAuthenticationUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        String identityUrl = token.getIdentityUrl();
        TAccount t = accountRepository.findUserByUsernameForOpenIDMode(identityUrl);
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
                throw new AccountNonVerifiedOrDisabledException(identityUrl);
            }
        } else {
            // Needs to create a new account
            String name = null;
            String firstName = null;
            String lastName = null;
            String email = null;
            for (OpenIDAttribute attribute : token.getAttributes()) {
                String type = attribute.getType();
                switch (type) {
                    case "http://axschema.org/contact/email":
                    case "http://schema.openid.net/contact/email":
                        email = toSimpleValue(attribute.getValues());
                        break;
                    case "http://axschema.org/namePerson/first":
                        firstName = toSimpleValue(attribute.getValues());
                        break;
                    case "http://axschema.org/namePerson/last":
                        lastName = toSimpleValue(attribute.getValues());
                        break;
                    case "http://schema.openid.net/namePerson":
                        name = toSimpleValue(attribute.getValues());
                        break;
                }
            }
            // Checks name
            if (StringUtils.isBlank(name)) {
                name = "";
                if (StringUtils.isNotBlank(firstName)) {
                    name += firstName;
                }
                if (StringUtils.isNotBlank(lastName)) {
                    name += " " + lastName;
                }
                name = name.trim();
            }
            if (StringUtils.isBlank(name)) {
                throw new AccountOpenIDNotFoundException(identityUrl);
            }
            // Checks name & email
            if (StringUtils.isBlank(name) || StringUtils.isBlank(email)) {
                // TODO Unknown attributes - need for a custom registration (not planned yet)
                // Use a OpenIDAuthenticationFailureHandler
                // Intercept the AccountOpenIDNotFoundException
                // Redirect to a page that allows the registration
                throw new AccountOpenIDNotFoundException(identityUrl);
            }
            // Creates an account
            int accountId = accountRepository.createAccount(
                    AuthenticationMode.OPEN_ID,
                    identityUrl,
                    email,
                    name,
                    ""
            );
            // Auto verified because of OpenID
            accountRepository.accountVerified(accountId);
            // OK
            return new AccountAuthenticationDetails(
                    accountId,
                    name,
                    email,
                    false,
                    AuthenticationMode.OPEN_ID
            );
        }
    }

    private String toSimpleValue(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        } else {
            return values.get(0);
        }
    }

}
