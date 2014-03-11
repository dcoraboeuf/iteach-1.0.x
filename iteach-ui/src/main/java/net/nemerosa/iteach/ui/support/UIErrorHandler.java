package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.InputException;
import net.nemerosa.iteach.common.NotFoundException;
import net.sf.jstring.Strings;
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

@ControllerAdvice(annotations = RestController.class)
public class UIErrorHandler {

    private final ErrorHandler errorHandler;
    private final Strings strings;

    @Autowired
    public UIErrorHandler(ErrorHandler errorHandler, Strings strings) {
        this.errorHandler = errorHandler;
        this.strings = strings;
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
        // Ignoring security exceptions
        if (ex instanceof AccessDeniedException) {
            throw ex;
        }
        // Error message
        ErrorMessage error = errorHandler.handleError(locale, request, ex);
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

}
