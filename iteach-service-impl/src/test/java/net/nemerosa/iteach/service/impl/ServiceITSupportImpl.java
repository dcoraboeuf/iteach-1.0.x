package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.service.security.AccountAuthenticationDetails;
import net.nemerosa.iteach.service.security.AccountAuthenticationToken;
import net.nemerosa.iteach.service.support.InMemoryPost;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.assertNotNull;

@Component
public class ServiceITSupportImpl implements ServiceITSupport {

    private final AccountService accountService;
    private final TeacherService teacherService;
    private final InMemoryPost inMemoryPost;

    @Autowired
    public ServiceITSupportImpl(AccountService accountService, TeacherService teacherService, InMemoryPost inMemoryPost) {
        this.accountService = accountService;
        this.teacherService = teacherService;
        this.inMemoryPost = inMemoryPost;
    }

    @Override
    public ID createTeacher(String name, String email) {
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
    public ID createTeacherAndCompleteRegistration(String name, String email) {
        ID id = createTeacher(name, email);
        if (id.isSuccess()) {
            Ack ack = completeRegistration(email);
            if (ack.isSuccess()) {
                return id;
            }
        }
        return ID.failure();
    }

    @Override
    public <T> T asTeacher(int teacherId, Callable<T> call) throws Exception {
        Account account = accountService.getAccount(teacherId);
        return asAccount(account, call);
    }

    @Override
    public int createTeacherAndCompleteRegistration() {
        String name = uid("T");
        return createTeacherAndCompleteRegistration(
                name,
                format("%s@test.com", name)
        ).getValue();
    }

    @Override
    public int createSchool() throws Exception {
        return createSchool(createTeacherAndCompleteRegistration());
    }

    @Override
    public int createSchool(int teacherId) throws Exception {
        String name = uid("S");
        return asTeacher(teacherId,
                () -> teacherService.createSchool(
                        new SchoolForm(
                                name,
                                "#FFFF00",
                                "A contact",
                                Money.parse("EUR 56"),
                                "Rue de l'école 5\n1000 Brussels",
                                "+32 1 23 45 67 89",
                                "+32 4 23 45 67 89",
                                "school@test.com",
                                "http://school.com",
                                "BE0123 456 789",
                                BigDecimal.valueOf(21)
                        )
                )
        );
    }


    @Override
    public Student createStudent() throws Exception {
        int teacherId = createTeacherAndCompleteRegistration();
        int school = createSchool(teacherId);
        return createStudent(teacherId, school);
    }

    @Override
    public Student createStudent(int teacherId, int schoolId) throws Exception {
        return asTeacher(teacherId,
                () -> teacherService.getStudent(teacherService.createStudent(
                        new StudentForm(
                                schoolId,
                                null,
                                uid("ST"),
                                "Any subject",
                                "",
                                "",
                                "",
                                ""
                        )
                ))
        );
    }

    @Override
    public <T> T asAdmin(Callable<T> call) throws Exception {
        return asTeacher(1, call);
    }

    private <T> T asAccount(Account account, Callable<T> call) throws Exception {
        SecurityContext context = new SecurityContextImpl();
        Authentication authentication = new AccountAuthenticationToken(
                new AccountAuthenticationDetails(
                        account.getId(),
                        account.getName(),
                        account.getEmail(),
                        account.isAdministrator(),
                        account.getAuthenticationMode())
        );
        context.setAuthentication(authentication);
        SecurityContext oldContext = SecurityContextHolder.getContext();
        try {
            SecurityContextHolder.setContext(context);
            return call.call();
        } finally {
            SecurityContextHolder.setContext(oldContext);
        }
    }
}
