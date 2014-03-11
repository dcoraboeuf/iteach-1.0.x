package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.ui.model.UIAccountAPI;
import net.nemerosa.iteach.ui.support.ErrorHandler;
import net.nemerosa.iteach.ui.support.ValidationTokenTypeNotManagedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class UIValidateController {

    private final UIAccountAPI accountAPI;
    private final ErrorHandler errorHandler;

    @Autowired
    public UIValidateController(UIAccountAPI accountAPI, ErrorHandler errorHandler) {
        this.accountAPI = accountAPI;
        this.errorHandler = errorHandler;
    }

    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET, headers = "Accept=text/html")
    public RedirectView guiValidate(Locale locale, @PathVariable TokenType tokenType, @PathVariable String token, HttpServletRequest request) {
        boolean success;
        try {
            success = accountAPI.validate(locale, tokenType, token).isSuccess();
        } catch (Exception ex) {
            errorHandler.handleError(locale, request, ex);
            success = false;
        }
        switch (tokenType) {
            case REGISTRATION:
                return new RedirectView(
                        String.format("/index.html#/registration/%s", success),
                        true
                );
            default:
                throw new ValidationTokenTypeNotManagedException(tokenType);
        }
    }

}
