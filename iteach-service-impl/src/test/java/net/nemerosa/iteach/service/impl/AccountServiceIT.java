package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.test.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AccountServiceIT extends AbstractITTestSupport {

    @Autowired
    private AccountService accountService;

    @Autowired
    private InMemoryPost inMemoryPost;

    @Test
    public void register() {
        String name = TestUtils.uid("T");
        String email = String.format("%s@test.com", name);
        Ack ack = accountService.register(
                Locale.ENGLISH,
                new TeacherRegistrationForm(
                        name,
                        email,
                        name
                )
        );
        assertTrue(ack.isSuccess());
        // Checks for notification
        Message message = inMemoryPost.getMessage(email);
        assertNotNull(message);
        // TODO Validates the notification
        String token = message.getContent().getToken();
        // TODO Checks we can authenticate with the new account
    }

}
