package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.test.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

import static org.junit.Assert.*;

public class AccountServiceIT extends AbstractITTestSupport {

    @Autowired
    private AccountService accountService;

    @Autowired
    private InMemoryPost inMemoryPost;

    @Autowired
    private AccountRepository accountRepository;

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
        // Checks we CANNOT authenticate with the account yet
        assertNull("A non verified account cannot be authenticated", accountRepository.findUserByUsernameForPasswordMode(name));
        // Checks for notification
        Message message = inMemoryPost.getMessage(email);
        assertNotNull(message);
        // Validates the notification
        String token = message.getContent().getToken();
        ack = accountService.completeRegistration(Locale.ENGLISH, token);
        assertTrue("Account registration cannot be completed", ack.isSuccess());
        // Checks we can authenticate with the new account
        TAccount account = accountRepository.findUserByUsernameForPasswordMode(email);
        assertNotNull("The user can be authenticated now", account);
        assertEquals(email, account.getEmail());
        assertEquals(name, account.getName());
    }

}
