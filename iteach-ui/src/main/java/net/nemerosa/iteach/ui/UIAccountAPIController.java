package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.ui.model.UIAccountAPI;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/account")
public class UIAccountAPIController implements UIAccountAPI {

    private final AccountService accountService;

    @Autowired
    public UIAccountAPIController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    @RequestMapping(value = "/register")
    public Ack registerAsTeacherWithPassword(Locale locale, @RequestBody UITeacherPasswordForm form) {
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
