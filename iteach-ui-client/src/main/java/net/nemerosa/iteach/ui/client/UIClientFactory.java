package net.nemerosa.iteach.ui.client;

import net.nemerosa.iteach.ui.client.support.UIAccountAPIClientImpl;
import net.nemerosa.iteach.ui.client.support.UICommentAPIClientImpl;
import net.nemerosa.iteach.ui.client.support.UITeacherAPIClientImpl;
import net.nemerosa.iteach.ui.client.support.UITestAPIClientImpl;

import java.net.MalformedURLException;

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

    public UIAccountAPIClient accountClient() throws MalformedURLException {
        return new UIAccountAPIClientImpl(url);
    }

    public UITestAPIClient testClient() throws MalformedURLException {
        return new UITestAPIClientImpl(url);
    }

    public UITeacherAPIClient teacherClient() throws MalformedURLException {
        return new UITeacherAPIClientImpl(url);
    }

    public UICommentAPIClient commentClient() throws MalformedURLException {
        return new UICommentAPIClientImpl(url);
    }
}
