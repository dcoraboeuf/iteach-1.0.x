package net.nemerosa.iteach.ui.config;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * CSRF request matcher for the application. Disabled for some elements.
 */
public class CSRFRequestMatcher implements RequestMatcher {

    private final Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private final Collection<RegexRequestMatcher> excludedRequests =
            Arrays.asList(
                    new RegexRequestMatcher("/api/localization/.*", null),
                    new RegexRequestMatcher("/api/account/register", null),
                    new RegexRequestMatcher("/api/account/validate", null)
            );

    @Override
    public boolean matches(HttpServletRequest request) {

        // No CSRF due to allowedMethod
        if (allowedMethods.matcher(request.getMethod()).matches()) {
            return false;
        }

        // No CSRF for excluded requests
        if (excludedRequests.stream().anyMatch(matcher -> matcher.matches(request))) {
            return false;
        }

        // CSRF for everything else that is not allowed
        return true;
    }

}
