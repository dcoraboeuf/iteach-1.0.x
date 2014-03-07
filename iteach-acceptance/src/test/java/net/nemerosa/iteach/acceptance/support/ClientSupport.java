package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UIClient;
import net.nemerosa.iteach.ui.client.UIClientFactory;
import net.nemerosa.iteach.ui.client.UITestAPIClient;
import net.nemerosa.iteach.ui.model.UITeacher;

import java.util.function.Function;

import static net.nemerosa.iteach.test.TestUtils.getEnvIfPresent;

public class ClientSupport {

    private final UIAccountAPIClient accountClient;
    private final UITestAPIClient testClient;

    public ClientSupport(String url) {
        UIClientFactory clientFactory = UIClientFactory.create(url);
        accountClient = clientFactory.accountClient();
        testClient = clientFactory.testClient();
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

    /**
     * Connection as teacher
     */
    public UITeacher login(String email, String password) {
        return account().anonymous().call(client -> client.login(email, password));
    }

    public static interface Client<C extends UIClient> {

        <T> T call(Function<C, T> call);

    }

    public static interface ConfigurableClient<C extends UIClient> {

        Client<C> asAdmin();

        Client<C> anonymous();

    }

    private static class ClientImpl<C extends UIClient> implements Client<C>, ConfigurableClient<C> {

        private final C internalClient;

        private ClientImpl(C internalClient) {
            this.internalClient = internalClient;
        }

        @Override
        public Client<C> asAdmin() {
            String adminPassword = getEnvIfPresent("iteach.admin.password", "ITEACH_ADMIN_PASSWORD", "admin");
            return new AuthenticationClient<>(
                    "admin",
                    adminPassword,
                    internalClient,
                    this
            );
        }

        @Override
        public Client<C> anonymous() {
            return new AnonymousClient<>(
                    internalClient,
                    this
            );
        }

        @Override
        public <T> T call(Function<C, T> call) {
            return call.apply(internalClient);
        }
    }

    private static class AuthenticationClient<C extends UIClient> implements Client<C> {

        private final String user;
        private final String password;
        private final C uiClient;
        private final Client<C> client;

        private AuthenticationClient(String user, String password, C uiClient, Client<C> client) {
            this.user = user;
            this.password = password;
            this.uiClient = uiClient;
            this.client = client;
        }

        @Override
        public <T> T call(Function<C, T> call) {
            // Login
            uiClient.login(user, password);
            try {
                // Call
                return client.call(call);
            } finally {
                // Logout
                uiClient.logout();
            }
        }
    }

    private static class AnonymousClient<C extends UIClient> implements Client<C> {

        private final C uiClient;
        private final Client<C> client;

        private AnonymousClient(C uiClient, Client<C> client) {
            this.uiClient = uiClient;
            this.client = client;
        }

        @Override
        public <T> T call(Function<C, T> call) {
            // Makes sure to logout first
            uiClient.logout();
            // Call
            return client.call(call);
        }
    }

}
