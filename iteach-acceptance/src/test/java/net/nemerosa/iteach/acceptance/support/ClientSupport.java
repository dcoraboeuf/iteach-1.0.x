package net.nemerosa.iteach.acceptance.support;

import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.client.UIClientFactory;

import java.util.function.Function;

public class ClientSupport {

    private final UIAccountAPIClient accountClient;

    public ClientSupport(String url) {
        UIClientFactory clientFactory = UIClientFactory.create(url);
        accountClient = clientFactory.accountClient();
    }

    public <T> T asAnonymous(Function<UIAccountAPIClient, T> call) {
        return call.apply(accountClient);
    }

}
