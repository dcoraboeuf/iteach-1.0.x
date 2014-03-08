package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.test.TestUtils;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UITestAPIClient;
import net.nemerosa.iteach.ui.model.UITeacher;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;

import java.net.MalformedURLException;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UISupport {

    private final ClientSupport client;

    public UISupport() {
        // Base URL
        String url = TestUtils.getEnv("iteach.url", "ITEACH_URL", "iTeach base URL");
        // Client support
        try {
            client = new ClientSupport(url);
        } catch (MalformedURLException e) {
            throw new UIMalformedURLException(url, e);
        }
    }

    public ClientSupport client() {
        return client;
    }

    public TeacherContext doCreateTeacher() {
        return doCreateTeacher(TestUtils.uid("T"));
    }

    private TeacherContext doCreateTeacher(String name) {
        return doCreateTeacher(
                name,
                String.format("%s@test.com", name),
                name);

    }

    private TeacherContext doCreateTeacher(String name, String email, String password) {
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
        Ack ack = client.account().anonymous().call(client -> client.validate(Locale.ENGLISH, TokenType.REGISTRATION, message.getContent().getToken()));
        assertTrue("Validation of the registration must be OK", ack.isSuccess());
        // Login
        UITeacher teacher = client.account().asUser(email, password).call(client -> client.login(Locale.ENGLISH));
        // Gets the teacher
        return new TeacherContext(teacher, password);
    }

}
