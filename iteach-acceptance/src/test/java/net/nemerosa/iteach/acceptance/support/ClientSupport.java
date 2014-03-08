package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UIClient;
import net.nemerosa.iteach.ui.client.UIClientFactory;
import net.nemerosa.iteach.ui.client.UITestAPIClient;

import java.net.MalformedURLException;
import java.util.function.Function;

import static net.nemerosa.iteach.test.TestUtils.getEnvIfPresent;

public class ClientSupport {

    private final UIAccountAPIClient accountClient;
    private final UITestAPIClient testClient;

    public ClientSupport(String url) throws MalformedURLException {
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

    public static interface Client<C extends UIClient<C>> {

        <T> T call(Function<C, T> call);

    }

    public static interface ConfigurableClient<C extends UIClient<C>> {

        Client<C> asAdmin();

        Client<C> asUser(String email, String password);

        Client<C> anonymous();

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
        public Client<C> anonymous() {
            return new ClientImpl<>(internalClient.anonymous());
        }

        @Override
        public <T> T call(Function<C, T> call) {
            return call.apply(internalClient);
        }
    }

}
