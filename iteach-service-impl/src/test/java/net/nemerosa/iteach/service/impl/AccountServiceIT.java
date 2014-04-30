package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.invoice.InvoiceFixtures;
import net.nemerosa.iteach.service.model.SetupForm;
import net.nemerosa.iteach.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import static net.nemerosa.iteach.service.invoice.InvoiceFixtures.companyLogo;
import static org.junit.Assert.*;

public class AccountServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    public void register() {
        String name = TestUtils.uid("T");
        String email = String.format("%s@test.com", name);
        ID id = serviceITSupport.createTeacher(name, email);
        assertTrue(id.isSuccess());
        // Checks we CANNOT authenticate with the account yet
        assertNull("A non verified account cannot be authenticated", accountRepository.findUserByUsernameForPasswordMode(name));
        // Completes the registration
        Ack ack = serviceITSupport.completeRegistration(email);
        assertTrue("Account registration cannot be completed", ack.isSuccess());
        // Checks we can authenticate with the new account
        TAccount account = accountRepository.findUserByUsernameForPasswordMode(email);
        assertNotNull("The user can be authenticated now", account);
        assertEquals(email, account.getEmail());
        assertEquals(name, account.getName());
    }

    @Test(expected = AccessDeniedException.class)
    public void delete_account_denied() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, () -> accountService.deleteAccount(teacherId));
    }

    @Test(expected = AccessDeniedException.class)
    public void get_account_denied() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, () -> accountService.getAccount(teacherId));
    }

    @Test(expected = AccessDeniedException.class)
    public void disable_account_denied() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, () -> accountService.disableAccount(teacherId));
    }

    @Test(expected = AccessDeniedException.class)
    public void enable_account_denied() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, () -> accountService.enableAccount(teacherId));
    }

    @Test(expected = AccessDeniedException.class)
    public void all_accounts_denied() throws Exception {
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, accountService::getAccounts);
    }

    @Test(expected = AccessDeniedException.class)
    public void setup_denied() throws Exception {
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, accountService::getSetup);
    }

    @Test(expected = AccessDeniedException.class)
    public void save_setup_denied() throws Exception {
        int teacherIdAnyOther = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherIdAnyOther, () -> accountService.saveSetup(new SetupForm("xxx", "x", "x")));
    }

    @Test
    public void disable_enable() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asAdmin(() -> {
            assertFalse("Account is enabled", accountService.getAccount(teacherId).isDisabled());
            accountService.disableAccount(teacherId);
            assertTrue("Account is disabled", accountService.getAccount(teacherId).isDisabled());
            accountService.enableAccount(teacherId);
            assertFalse("Account is enabled", accountService.getAccount(teacherId).isDisabled());
            // End
            return null;
        });
    }

    @Test
    public void company_logo_none() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        UntitledDocument logo = serviceITSupport.asTeacher(teacherId, accountService::getProfileCompanyLogo);
        assertNull("No logo", logo);
    }

    @Test
    public void company_logo() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        serviceITSupport.asTeacher(teacherId, () -> accountService.updateProfileCompanyLogo(companyLogo()));
        UntitledDocument logo = serviceITSupport.asTeacher(teacherId, accountService::getProfileCompanyLogo);
        assertNotNull("Logo", logo);
        assertEquals("PNG Logo", "image/png", logo.getType());
    }

}
