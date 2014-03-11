package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.ui.support.ValidationTokenTypeNotManagedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UIValidateController {

    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET, headers = "Accept=text/html")
    public RedirectView guiValidate(@PathVariable TokenType tokenType, @PathVariable String token) {
        switch (tokenType) {
            case REGISTRATION:
                return new RedirectView(
                        String.format("/index.html#/registrationOk/%s", tokenType, token),
                        true
                );
            default:
                throw new ValidationTokenTypeNotManagedException(tokenType);
        }
    }

}
