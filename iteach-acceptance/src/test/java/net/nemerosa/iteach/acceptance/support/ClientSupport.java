package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.ui.client.*;

import java.net.MalformedURLException;
import java.util.function.Function;

import static net.nemerosa.iteach.test.TestUtils.getEnvIfPresent;

public class ClientSupport {

    private final UIAccountAPIClient accountClient;
    private final UITestAPIClient testClient;
    private final UITeacherAPIClient teacherClient;

    public ClientSupport(String url) throws MalformedURLException {
        UIClientFactory clientFactory = UIClientFactory.create(url);
        accountClient = clientFactory.accountClient();
        testClient = clientFactory.testClient();
        teacherClient = clientFactory.teacherClient();
    }

    /**
     * Client for the Account API.
     */
    public ConfigurableClient<UIAccountAPIClient> account() {
        return new ClientImpl<>(accountClient);
    }

    /**
     * Client for the Test API.
     */
    public ConfigurableClient<UITestAPIClient> test() {
        return new ClientImpl<>(testClient);
    }

    public ConfigurableClient<UITeacherAPIClient> teacher() {
        return new ClientImpl<>(teacherClient);
    }

    public static interface Client<C extends UIClient<C>> {

        <T> T call(Function<C, T> call);

    }

    public static interface ConfigurableClient<C extends UIClient<C>> {

        Client<C> asAdmin();

        Client<C> asUser(String email, String password);

        Client<C> anonymous();

        Client<C> asTeacher(TeacherContext teacherContext);
    }

    private static class ClientImpl<C extends UIClient<C>> implements Client<C>, ConfigurableClient<C> {

        private final C internalClient;

        private ClientImpl(C internalClient) {
            this.internalClient = internalClient;
        }

        @Override
        public Client<C> asAdmin() {
            String adminPassword = getEnvIfPresent("iteach.admin.password", "ITEACH_ADMIN_PASSWORD", "admin");
            return asUser("admin", adminPassword);
        }

        @Override
        public Client<C> asUser(String email, String password) {
            return new ClientImpl<>(internalClient.withBasicLogin(email, password));
        }

        @Override
        public Client<C> asTeacher(TeacherContext teacherContext) {
            return asUser(teacherContext.getTeacher().getEmail(), teacherContext.getPassword());
        }

        @Override
        public Client<C> anonymous() {
            return new ClientImpl<>(internalClient.anonymous());
        }

        @Override
        public <T> T call(Function<C, T> call) {
            return call.apply(internalClient);
        }
    }

}
