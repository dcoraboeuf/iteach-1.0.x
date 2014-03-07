package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UIClient;
import net.nemerosa.iteach.ui.client.UIClientFactory;
import net.nemerosa.iteach.ui.client.UITestAPIClient;

import java.util.function.Function;

public class ClientSupport {

    private final UIAccountAPIClient accountClient;
    private final UITestAPIClient testClient;

    public ClientSupport(String url) {
        UIClientFactory clientFactory = UIClientFactory.create(url);
        accountClient = clientFactory.accountClient();
        testClient = clientFactory.testClient();
    }

    @Deprecated
    public <T> T asAnonymous(Function<UIAccountAPIClient, T> call) {
        return call.apply(accountClient);
    }

    /**
     * Client for the Test API.
     */
    public ConfigurableClient<UITestAPIClient> test() {
        return new ClientImpl<>(testClient);
    }

    public static interface Client<C extends UIClient> {

        <T> T call(Function<C, T> call);

    }

    public static interface ConfigurableClient<C extends UIClient> {

        Client<C> asAdmin();

    }

    private static class ClientImpl<C extends UIClient> implements Client<C>, ConfigurableClient<C> {

        private final C internalClient;

        private ClientImpl(C internalClient) {
            this.internalClient = internalClient;
        }

        @Override
        public Client<C> asAdmin() {
            return new AuthenticationClient<>(
                    "admin",
                    "admin",
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

}
