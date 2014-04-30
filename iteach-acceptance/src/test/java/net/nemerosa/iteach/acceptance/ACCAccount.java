package net.nemerosa.iteach.acceptance;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.acceptance.support.AbstractACCSupport;
import net.nemerosa.iteach.acceptance.support.TeacherContext;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.nemerosa.iteach.ui.client.support.ClientForbiddenException;
import net.nemerosa.iteach.ui.model.UITeacher;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class ACCAccount extends AbstractACCSupport {

    @Test
    public void admin_login_logout() {
        support.client().account().asAdmin().call(client -> {
            UITeacher account = client.login(Locale.ENGLISH);
            // Checks
            assertNotNull(account);
            assertEquals(1, account.getId());
            assertEquals("Administrator", account.getName());
            assertEquals("", account.getEmail());
            assertEquals(AuthenticationMode.PASSWORD, account.getAuthenticationMode());
            // Logout
            client.logout();
            // Nothing to return
            return null;
        });
    }

    @Test
    public void profile_company_logo() {
        TeacherContext teacherContext = support.doCreateTeacher();
        support.client().account().asTeacher(teacherContext).call(client -> {
            // Initial state
            UntitledDocument logo = client.getProfileCompanyLogo(Locale.ENGLISH);
            assertNull("No logo initially", logo);
            // Sets a logo
            String path = "/testdata/logo.png";
            byte[] logoData = support.getTestDataAsBytes(path);
            logo = new UntitledDocument(
                    "image/png",
                    logoData
            );
            assertTrue("Logo updated", client.updateProfileCompanyLogo(Locale.ENGLISH, logo).isSuccess());
            // Gets the logo back
            UntitledDocument actualLogo = client.getProfileCompanyLogo(Locale.ENGLISH);
            assertNotNull("Logo is there", actualLogo);
            assertEquals("PNG logo", "image/png", actualLogo.getType());
            assertArrayEquals("Logo bytes", logoData, actualLogo.getContent());
            // End
            return null;
        });
    }

    @Test
    public void export_account() throws JsonProcessingException {
        TeacherContext teacherContext = support.doCreateTeacher();
        JsonNode data = support.client().account().asAdmin().call(client ->
                        client.exportAccount(Locale.ENGLISH, teacherContext.getTeacher().getId())
        );
        assertNotNull("Returned data", data);
        assertEquals(
                ObjectMapperFactory.create().writeValueAsString(data),
                "{\"version\":2,\"schools\":[]}"
        );
    }

    @Test(expected = ClientForbiddenException.class)
    public void export_account_denied() throws JsonProcessingException {
        TeacherContext teacherContext1 = support.doCreateTeacher();
        TeacherContext teacherContext2 = support.doCreateTeacher();
        support.client().account().asTeacher(teacherContext2).call(client ->
                        client.exportAccount(Locale.ENGLISH, teacherContext1.getTeacher().getId())
        );
    }

}
