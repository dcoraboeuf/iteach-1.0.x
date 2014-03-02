package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.ui.model.UIAccountAPI;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/account")
public class UIAccountAPIController implements UIAccountAPI {

    @Override
    @RequestMapping(value = "/register")
    public Ack registerAsTeacherWithPassword(Locale locale, @RequestBody UITeacherPasswordForm form) {
        // FIXME registerAsTeacherWithPassword
        return Ack.NOK;
    }

}
