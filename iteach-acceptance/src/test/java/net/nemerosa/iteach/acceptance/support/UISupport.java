package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.test.TestUtils;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UITestAPIClient;
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

    public TeacherID doCreateTeacher() {
        return doCreateTeacher(TestUtils.uid("T"));
    }

    private TeacherID doCreateTeacher(String name) {
        return doCreateTeacher(
                name,
                String.format("%s@test.com", name),
                name);

    }

    private TeacherID doCreateTeacher(String name, String email, String password) {
        // Registers a new teacher
        client.account().anonymous().call(
                (UIAccountAPIClient client) -> client.registerAsTeacherWithPassword(
                        Locale.ENGLISH,
                        new UITeacherPasswordForm(
                                name,
                                email,
                                password
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
        // Gets the teacher
        return new TeacherID(name, email, password);
    }

}
