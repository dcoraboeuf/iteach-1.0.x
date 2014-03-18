package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.InputException;
import net.nemerosa.iteach.common.NotFoundException;
import net.sf.jstring.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@ControllerAdvice(annotations = RestController.class)
public class UIErrorHandler {

    private final ErrorHandler errorHandler;
    private final Strings strings;
    private final MessageSource messageSource;

    @Autowired
    public UIErrorHandler(ErrorHandler errorHandler, Strings strings, MessageSource messageSource) {
        this.errorHandler = errorHandler;
        this.strings = strings;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> onNotFoundException(Locale locale, NotFoundException ex) {
        // Returns a message to display to the user
        String message = ex.getLocalizedMessage(strings, locale);
        // OK
        return getMessageResponse(HttpStatus.NOT_FOUND, message);
    }

    @ExceptionHandler(InputException.class)
    public ResponseEntity<String> onInputException(Locale locale, InputException ex) {
        // Returns a message to display to the user
        String message = ex.getLocalizedMessage(strings, locale);
        // OK
        return getMessageResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> onValidationException(Locale locale, MethodArgumentNotValidException ex) {
        List<String> messages = new ArrayList<>();
        // Field errors
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            messages.add(messageSource.getMessage(fieldError, locale));
        }
        // TODO Global errors?
        // Returned message
        String message;
        if (messages.size() > 1) {
            message = StringUtils.join(
                    messages.stream().map(s -> "* " + s + "\n").collect(Collectors.toList())
            );
        } else {
            message = messages.get(0);
        }
        // OK
        return getMessageResponse(
                HttpStatus.BAD_REQUEST,
                message
        );
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
        String message = String.format("[%s] %s", error.getUuid(), error.getMessage());
        // Ok
        return getMessageResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    protected ResponseEntity<String> getMessageResponse(HttpStatus status, String message) {
        // Header
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "text/plain; charset=utf-8");
        // OK
        return new ResponseEntity<>(message, responseHeaders, status);
    }

}
