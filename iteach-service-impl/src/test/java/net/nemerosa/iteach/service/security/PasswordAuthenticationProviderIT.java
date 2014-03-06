package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.impl.ServiceITSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.*;

public class PasswordAuthenticationProviderIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport support;

    @Autowired
    @Qualifier("password")
    private AuthenticationProvider provider;

    @Test
    public void authentication_admin() {
        Authentication authentication = provider.authenticate(new UsernamePasswordAuthenticationToken("admin", "admin"));
        assertNotNull(authentication);
        assertTrue(authentication.getPrincipal() instanceof AccountAuthentication);
        AccountAuthentication aa = (AccountAuthentication) authentication.getPrincipal();
        assertEquals(1, aa.getId());
        assertEquals("", aa.getEmail());
        assertTrue(aa.isAdministrator());
    }

    @Test
    public void authentication_teacher() {
        String teacherName = uid("T");
        String teacherEmail = String.format("%s@test.com", teacherName);
        // Creates a teacher
        int teacherId = support.createTeacherAndCompleteRegistration(teacherName, teacherEmail).getValue();
        // Authentication
        Authentication authentication = provider.authenticate(new UsernamePasswordAuthenticationToken(teacherEmail, teacherName));
        assertNotNull(authentication);
        assertTrue(authentication.getPrincipal() instanceof AccountAuthentication);
        AccountAuthentication aa = (AccountAuthentication) authentication.getPrincipal();
        assertEquals(teacherId, aa.getId());
        assertEquals(teacherEmail, aa.getEmail());
        assertFalse(aa.isAdministrator());
    }

}
