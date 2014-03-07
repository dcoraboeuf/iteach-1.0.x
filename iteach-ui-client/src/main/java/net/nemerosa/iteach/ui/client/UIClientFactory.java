package net.nemerosa.iteach.ui.client;

import net.nemerosa.iteach.ui.client.support.UIAccountAPIClientImpl;
import net.nemerosa.iteach.ui.client.support.UITestAPIClientImpl;

/**
 * Access to all UI clients.
 */
public class UIClientFactory {

    public static UIClientFactory create(String url) {
        return new UIClientFactory(url);
    }

    private final String url;

    private UIClientFactory(String url) {
        this.url = url;
    }

    public UIAccountAPIClient accountClient() {
        return new UIAccountAPIClientImpl(url);
    }

    public UITestAPIClient testClient() {
        return new UITestAPIClientImpl(url);
    }
}
