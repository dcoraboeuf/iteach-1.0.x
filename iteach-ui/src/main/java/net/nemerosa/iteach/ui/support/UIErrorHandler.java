package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.InputException;
import net.nemerosa.iteach.common.NotFoundException;
import net.nemerosa.iteach.service.SecurityUtils;
import net.sf.jstring.Strings;
import net.sf.jstring.support.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.UUID;

@ControllerAdvice(annotations = RestController.class)
public class UIErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(UIErrorHandler.class);

    private final SecurityUtils securityUtils;
    private final Strings strings;

    @Autowired
    public UIErrorHandler(SecurityUtils securityUtils, Strings strings) {
        this.securityUtils = securityUtils;
        this.strings = strings;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> onAccessDeniedException(Locale locale, AccessDeniedException ex) {
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> onNotFoundException(Locale locale, NotFoundException ex) {
        // Returns a message to display to the user
        String message = ex.getLocalizedMessage(strings, locale);
        // OK
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InputException.class)
    public ResponseEntity<String> onInputException(Locale locale, InputException ex) {
        // Returns a message to display to the user
        String message = ex.getLocalizedMessage(strings, locale);
        // OK
        return getMessageResponse(message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> onException(Locale locale, HttpServletRequest request, Exception ex) throws Exception {
        // Ignores access errors
        if (ex instanceof AccessDeniedException) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        // Error message
        ErrorMessage error = handleError(locale, request, ex);
        // Returns a message to display to the user
        String message = strings.get(locale, "general.error.full", error.getMessage(), error.getUuid());
        // Ok
        return getMessageResponse(message);
    }

    protected ResponseEntity<String> getMessageResponse(String message) {
        // Header
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        // OK
        return new ResponseEntity<>(message, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ErrorMessage handleError(Locale locale, HttpServletRequest request, Exception ex) {
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
                displayMessage = strings.get(locale, "general.error.technical");
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
