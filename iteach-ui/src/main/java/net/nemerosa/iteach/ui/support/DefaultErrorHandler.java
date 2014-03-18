package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.InputException;
import net.nemerosa.iteach.service.SecurityUtils;
import net.sf.jstring.Strings;
import net.sf.jstring.support.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

@Component
public class DefaultErrorHandler implements ErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private final Strings strings;
    private final SecurityUtils securityUtils;

    @Autowired
    public DefaultErrorHandler(Strings strings, SecurityUtils securityUtils) {
        this.strings = strings;
        this.securityUtils = securityUtils;
    }

    @Override
    public ErrorMessage handleError(Locale locale, HttpServletRequest request, Exception ex) {
        // Generates a UUID
        String uuid = UUID.randomUUID().toString();
        // Error message
        String displayMessage;
        String loggedMessage;
        boolean stackTrace;
        if (ex instanceof CoreException) {
            loggedMessage = ((CoreException) ex).getLocalizedMessage(strings, Locale.ENGLISH);
            stackTrace = !(ex instanceof InputException);
            displayMessage = ((CoreException) ex).getLocalizedMessage(strings, locale);
        } else {
            loggedMessage = ex.getMessage();
            stackTrace = true;
            // Gets a display message for this exception class
            String messageKey = ex.getClass().getName();
            if (strings.isDefined(locale, messageKey)) {
                displayMessage = strings.get(locale, messageKey, false);
            } else {
                displayMessage = strings.get(locale, "ui.error");
            }
        }
        // Traces the error
        // Adds request information
        String pathInfo = request.getPathInfo();
        // Adds authentication information
        String securityInfo = securityUtils.getCurrentAccountName();
        // Message to display in the log
        String formattedLoggedMessage = String.format("[%s] user=%s,path=%s - %s",
                uuid,
                securityInfo,
                pathInfo,
                loggedMessage
        );
        if (stackTrace) {
            logger.error(formattedLoggedMessage, ex);
        } else {
            logger.error(formattedLoggedMessage);
        }
        // OK
        return new ErrorMessage(uuid, displayMessage);
    }
}
