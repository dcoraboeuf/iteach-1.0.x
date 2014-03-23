package net.nemerosa.iteach.ui.config;

import net.nemerosa.iteach.service.AccountNonVerifiedOrDisabledException;
import net.nemerosa.iteach.service.AccountOpenIDNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// FIXME Moves to the GUI layer
@Component
@Qualifier("openid")
public class OpenIdAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        if (exception instanceof AccountOpenIDNotFoundException) {
            // TODO Registration page when AccountOpenIDNotFoundException
            // TODO Adds known information in the request (OpenID identifier)
            // Goes to the registration page
            redirectStrategy.sendRedirect(request, response, "/index.html#/register_openid");
        } else if (exception instanceof AccountNonVerifiedOrDisabledException) {
            // Goes back to the login page with an error message
            redirectStrategy.sendRedirect(request, response, "/index.html#/login?error=openid_registration_non_valid");
        } else {
            // Goes back to the login page with an error message
            redirectStrategy.sendRedirect(request, response, "/index.html#/login?error=openid_failed");
        }
    }
}
