package net.nemerosa.iteach.gui;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.service.ValidationTokenTypeNotManagedException;
import net.nemerosa.iteach.ui.UIAccountAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Locale;

@Controller
public class GUIValidateController {

    private final UIAccountAPI accountAPI;

    @Autowired
    public GUIValidateController(UIAccountAPI accountAPI) {
        this.accountAPI = accountAPI;
    }

    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET, headers = "Accept=text/html")
    public RedirectView guiValidate(Locale locale, @PathVariable TokenType tokenType, @PathVariable String token) {
        if (tokenType == TokenType.REGISTRATION) {
            boolean success;
            try {
                success = accountAPI.validate(locale, tokenType, token).isSuccess();
            } catch (Exception ex) {
                // FIXME Redirects to an error page
                success = false;
            }
            return new RedirectView(
                    String.format("/index.html#/registration/%s", success),
                    true
            );
        } else if (tokenType == TokenType.PASSWORD_CHANGE) {
            // FIXME Checks the token before hand
            return new RedirectView(
                    String.format("/index.html#/passwordChangeRequest/%s", token),
                    true
            );
        } else {
            // FIXME Redirect to an error page
            throw new ValidationTokenTypeNotManagedException(tokenType);
        }
    }

}
