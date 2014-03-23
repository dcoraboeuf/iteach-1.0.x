package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class OpenIdAuthenticationUserDetailsServiceTest {

    public static final String IDENTITY_URL = "myIdentity";
    public static final String ACCOUNT_EMAIL = "myname@test.com";
    public static final String ACCOUNT_NAME = "My Name";
    private AccountRepository accountRepository;
    private OpenIdAuthenticationUserDetailsService service;

    @Before
    public void before() {
        accountRepository = mock(AccountRepository.class);
        service = new OpenIdAuthenticationUserDetailsService(accountRepository);
    }

    @Test(expected = AccountNonVerifiedOrDisabledException.class)
    public void not_verified() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                Collections.emptyList()
        );
        when(accountRepository.findUserByUsernameForOpenIDMode(IDENTITY_URL)).thenReturn(
                new TAccount(
                        1,
                        ACCOUNT_NAME,
                        ACCOUNT_EMAIL,
                        false,
                        AuthenticationMode.OPEN_ID,
                        false,
                        false
                )
        );
        service.loadUserDetails(token);
    }

    @Test(expected = AccountNonVerifiedOrDisabledException.class)
    public void disabled() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                Collections.emptyList()
        );
        when(accountRepository.findUserByUsernameForOpenIDMode(IDENTITY_URL)).thenReturn(
                new TAccount(
                        1,
                        ACCOUNT_NAME,
                        ACCOUNT_EMAIL,
                        false,
                        AuthenticationMode.OPEN_ID,
                        true,
                        true
                )
        );
        service.loadUserDetails(token);
    }

    @Test
    public void already_registered() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                Collections.emptyList()
        );
        when(accountRepository.findUserByUsernameForOpenIDMode(IDENTITY_URL)).thenReturn(
                new TAccount(
                        1,
                        ACCOUNT_NAME,
                        ACCOUNT_EMAIL,
                        false,
                        AuthenticationMode.OPEN_ID,
                        true,
                        false
                )
        );
        UserDetails userDetails = service.loadUserDetails(token);
        assertTrue(userDetails instanceof AccountAuthenticationDetails);
        AccountAuthenticationDetails details = (AccountAuthenticationDetails) userDetails;
        assertEquals(1, details.getId());
        assertEquals(ACCOUNT_NAME, details.getName());
        assertEquals(ACCOUNT_EMAIL, details.getEmail());
        assertEquals(AuthenticationMode.OPEN_ID, details.getAuthenticationMode());
    }

    @Test
    public void auto_registration_email_and_name() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                asList(
                        new OpenIDAttribute("email", "http://schema.openid.net/contact/email", asList(ACCOUNT_EMAIL)),
                        new OpenIDAttribute("name", "http://schema.openid.net/namePerson", asList(ACCOUNT_NAME))
                )
        );
        when(accountRepository.createAccount(AuthenticationMode.OPEN_ID, IDENTITY_URL, ACCOUNT_EMAIL, ACCOUNT_NAME, "")).thenReturn(10);
        UserDetails userDetails = service.loadUserDetails(token);
        assertTrue(userDetails instanceof AccountAuthenticationDetails);
        AccountAuthenticationDetails details = (AccountAuthenticationDetails) userDetails;
        assertEquals(10, details.getId());
        assertEquals(ACCOUNT_NAME, details.getName());
        assertEquals(ACCOUNT_EMAIL, details.getEmail());
        assertEquals(AuthenticationMode.OPEN_ID, details.getAuthenticationMode());
        verify(accountRepository, times(1)).accountVerified(10);
    }

    @Test
    public void auto_registration_email_and_first_and_last_name() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                asList(
                        new OpenIDAttribute("email", "http://axschema.org/contact/email", asList(ACCOUNT_EMAIL)),
                        new OpenIDAttribute("name", "http://axschema.org/namePerson/first", asList("My")),
                        new OpenIDAttribute("name", "http://axschema.org/namePerson/last", asList("Name"))
                )
        );
        when(accountRepository.createAccount(AuthenticationMode.OPEN_ID, IDENTITY_URL, ACCOUNT_EMAIL, ACCOUNT_NAME, "")).thenReturn(10);
        UserDetails userDetails = service.loadUserDetails(token);
        assertTrue(userDetails instanceof AccountAuthenticationDetails);
        AccountAuthenticationDetails details = (AccountAuthenticationDetails) userDetails;
        assertEquals(10, details.getId());
        assertEquals(ACCOUNT_NAME, details.getName());
        assertEquals(ACCOUNT_EMAIL, details.getEmail());
        assertEquals(AuthenticationMode.OPEN_ID, details.getAuthenticationMode());
        verify(accountRepository, times(1)).accountVerified(10);
    }

    @Test
    public void auto_registration_email_and_last_name_only() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                asList(
                        new OpenIDAttribute("email", "http://axschema.org/contact/email", asList(ACCOUNT_EMAIL)),
                        new OpenIDAttribute("name", "http://axschema.org/namePerson/last", asList("My Name"))
                )
        );
        when(accountRepository.createAccount(AuthenticationMode.OPEN_ID, IDENTITY_URL, ACCOUNT_EMAIL, ACCOUNT_NAME, "")).thenReturn(10);
        UserDetails userDetails = service.loadUserDetails(token);
        assertTrue(userDetails instanceof AccountAuthenticationDetails);
        AccountAuthenticationDetails details = (AccountAuthenticationDetails) userDetails;
        assertEquals(10, details.getId());
        assertEquals(ACCOUNT_NAME, details.getName());
        assertEquals(ACCOUNT_EMAIL, details.getEmail());
        assertEquals(AuthenticationMode.OPEN_ID, details.getAuthenticationMode());
        verify(accountRepository, times(1)).accountVerified(10);
    }

    @Test(expected = AccountOpenIDNotFoundException.class)
    public void auto_registration_missing_name() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                asList(
                        new OpenIDAttribute("email", "http://axschema.org/contact/email", asList(ACCOUNT_EMAIL))
                )
        );
        service.loadUserDetails(token);
    }

    @Test(expected = AccountOpenIDNotFoundException.class)
    public void auto_registration_missing_email() {
        OpenIDAuthenticationToken token = new OpenIDAuthenticationToken(
                OpenIDAuthenticationStatus.SUCCESS,
                IDENTITY_URL,
                "Some message",
                asList(
                        new OpenIDAttribute("name", "http://axschema.org/contact/namePerson/first", asList(ACCOUNT_NAME))
                )
        );
        service.loadUserDetails(token);
    }

}
