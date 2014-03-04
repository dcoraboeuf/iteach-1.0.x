package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;

@Component
public class ServiceITSupportImpl implements ServiceITSupport {

    private final AccountService accountService;
    private final InMemoryPost inMemoryPost;

    @Autowired
    public ServiceITSupportImpl(AccountService accountService, InMemoryPost inMemoryPost) {
        this.accountService = accountService;
        this.inMemoryPost = inMemoryPost;
    }

    @Override
    public Ack createTeacher(String name, String email) {
        return accountService.register(
                Locale.ENGLISH,
                new TeacherRegistrationForm(
                        name,
                        email,
                        name
                )
        );
    }

    @Override
    public Ack completeRegistration(String email) {
        // Checks for notification
        Message message = inMemoryPost.getMessage(email);
        assertNotNull(message);
        // Validates the notification
        String token = message.getContent().getToken();
        return accountService.completeRegistration(Locale.ENGLISH, token);
    }

    @Override
    public Ack createTeacherAndCompleteRegistration(String name, String email) {
        return Ack.validate(
                createTeacher(name, email).isSuccess()
                && completeRegistration(email).isSuccess()
        );
    }
}
