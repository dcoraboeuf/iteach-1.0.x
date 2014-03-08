package net.nemerosa.iteach.acceptance;

import net.nemerosa.iteach.acceptance.support.AbstractACCSupport;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.ui.model.UITeacher;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

}
