package net.nemerosa.iteach.gui;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice(basePackages = {"net.nemerosa.iteach.gui"})
public class GUIErrorHandler {

    @ExceptionHandler(Exception.class)
    public RedirectView onError(Exception ex) {
        return new RedirectView(
                String.format("/index.html#/error"),
                true
        );
    }

}
