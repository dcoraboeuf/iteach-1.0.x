package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.ui.client.UITestAPIClient;

import java.net.MalformedURLException;
import java.util.Locale;

public class UITestAPIClientImpl extends AbstractClient<UITestAPIClient> implements UITestAPIClient {

    public UITestAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public Message getMessage(String email) {
        return get(Locale.ENGLISH, Message.class, "/api/test/message/%s", email);
    }
}
