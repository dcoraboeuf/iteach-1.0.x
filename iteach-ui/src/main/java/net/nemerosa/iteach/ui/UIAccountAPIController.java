package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.ui.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class UIAccountAPIController implements UIAccountAPI {

    private final AccountService accountService;
    private final SecurityUtils securityUtils;
    private final Function<AccountAuthentication, UITeacher> teacherFromAuthentication = authentication ->
            new UITeacher(
                    authentication.getId(),
                    authentication.getName(),
                    authentication.getEmail(),
                    authentication.isAdministrator(),
                    authentication.getAuthenticationMode()
            );

    @Autowired
    public UIAccountAPIController(AccountService accountService, SecurityUtils securityUtils) {
        this.accountService = accountService;
        this.securityUtils = securityUtils;
    }

    @Override
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public UIState state(Locale locale) {
        AccountAuthentication authentication = securityUtils.getCurrentAccount();
        if (authentication == null) {
            return UIState.notAuthenticated();
        } else {
            return UIState.authenticated(
                    teacherFromAuthentication.apply(authentication)
            );
        }
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UITeacher login(Locale locale) {
        AccountAuthentication authentication = securityUtils.getCurrentAccount();
        if (authentication == null) {
            throw new AccessDeniedException("No authentication");
        }
        return teacherFromAuthentication.apply(authentication);
    }

    /**
     * See {@link net.nemerosa.iteach.ui.config.WebSecurityConfig} for the actual logout.
     */
    @RequestMapping(value = "/logged-out", method = RequestMethod.GET)
    public Ack logout() {
        return Ack.validate(securityUtils.isLogged());
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public UIAccountCollection getAccounts(Locale locale) {
        return new UIAccountCollection(
                accountService
                        .getAccounts()
                        .map(
                                o -> new UIAccount(
                                        o.getId(),
                                        o.getEmail(),
                                        o.isAdministrator()
                                )
                        ).collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ID registerAsTeacherWithPassword(Locale locale, @RequestBody UITeacherPasswordForm form) {
        return accountService.register(
                locale,
                new TeacherRegistrationForm(
                        form.getName(),
                        form.getEmail(),
                        form.getPassword()
                )
        );
    }

    @Override
    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET)
    public Ack validate(Locale locale, @PathVariable TokenType tokenType, @PathVariable String token) {
        switch (tokenType) {
            case REGISTRATION:
                return accountService.completeRegistration(locale, token);
            default:
                throw new IllegalStateException("Token not handled: " + tokenType);
        }
    }

}
