package net.nemerosa.iteach.gui;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.service.TokenService;
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
    private final TokenService tokenService;

    @Autowired
    public GUIValidateController(UIAccountAPI accountAPI, TokenService tokenService) {
        this.accountAPI = accountAPI;
        this.tokenService = tokenService;
    }

    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET, headers = "Accept=text/html")
    public RedirectView guiValidate(Locale locale, @PathVariable TokenType tokenType, @PathVariable String token) {
        if (tokenType == TokenType.REGISTRATION) {
            boolean success = accountAPI.validate(locale, tokenType, token).isSuccess();
            return new RedirectView(
                    String.format("/index.html#/registration/%s", success),
                    true
            );
        } else if (tokenType == TokenType.PASSWORD_CHANGE) {
            // Checks the token before hand
            tokenService.checkToken(token, tokenType);
            // Redirects to the form
            return new RedirectView(
                    String.format("/index.html#/passwordChangeRequest/%s", token),
                    true
            );
        } else {
            throw new ValidationTokenTypeNotManagedException(tokenType);
        }
    }

}
