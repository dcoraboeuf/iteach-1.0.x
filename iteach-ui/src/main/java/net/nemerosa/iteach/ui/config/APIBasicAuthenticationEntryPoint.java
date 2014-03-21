package net.nemerosa.iteach.ui.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("APIBasicAuthenticationEntryPoint")
public class APIBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    public APIBasicAuthenticationEntryPoint() {
        setRealmName("iteach");
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Does nothing...
        response.sendError(HttpServletResponse.SC_FORBIDDEN, authException.getMessage());
    }
}
