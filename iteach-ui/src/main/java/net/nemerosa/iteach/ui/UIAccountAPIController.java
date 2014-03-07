package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.ui.model.UIAccount;
import net.nemerosa.iteach.ui.model.UIAccountAPI;
import net.nemerosa.iteach.ui.model.UITeacher;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class UIAccountAPIController implements UIAccountAPI {

    private final AccountService accountService;
    private final SecurityUtils securityUtils;

    @Autowired
    public UIAccountAPIController(AccountService accountService, SecurityUtils securityUtils) {
        this.accountService = accountService;
        this.securityUtils = securityUtils;
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UITeacher login(Locale locale) {
        AccountAuthentication authentication = securityUtils.getCurrentAccount();
        if (authentication == null) {
            throw new AccessDeniedException("No authentication");
        }
        return new UITeacher(
                authentication.getId(),
                authentication.getName(),
                authentication.getEmail(),
                authentication.isAdministrator(),
                authentication.getAuthenticationMode()
        );
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UIAccount> getAccounts(Locale locale) {
        return accountService.getAccounts().map(
                o -> new UIAccount(
                        o.getId(),
                        o.getEmail(),
                        o.isAdministrator()
                )
        ).collect(Collectors.toList());
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

}
