package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.test.TestUtils;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UITestAPIClient;
import net.nemerosa.iteach.ui.model.UITeacher;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;

public class UISupport {

    private final ClientSupport client;

    public UISupport() {
        // Base URL
        String url = TestUtils.getEnv("iteach.url", "ITEACH_URL", "iTeach base URL");
        // Client support
        client = new ClientSupport(url);
    }

    public UITeacher doCreateTeacher() {
        return doCreateTeacher(TestUtils.uid("T"));
    }

    private UITeacher doCreateTeacher(String name) {
        String email = String.format("%s@test.com", name);
        // Registers a new teacher
        client.account().anonymous().call(
                (UIAccountAPIClient client) -> client.registerAsTeacherWithPassword(
                        Locale.ENGLISH,
                        new UITeacherPasswordForm(
                                name,
                                email,
                                name
                        )
                )
        );
        // Checks the returned mail
        Message message = client.test().asAdmin().call(((UITestAPIClient client) -> client.getMessage(email)));
        assertNotNull(String.format("Cannot find any message for %s", email), message);
        // Validates the mail by "clicking" on the URL
        String link = message.getContent().getLink();
        try {
            new URL(link).openConnection();
        } catch (IOException ex) {
            throw new UICannotAccessLinkException(ex, link);
        }
        // TODO Gets the teacher by ID
        return null;
    }

}
