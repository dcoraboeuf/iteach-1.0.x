package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;

import java.util.Locale;

public class UIAccountAPIClientImpl extends AbstractClient implements UIAccountAPIClient {

    public UIAccountAPIClientImpl(String url) {
        super(url);
    }

    @Override
    public Ack registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form) {
        return post(locale, Ack.class, form, "/api/account/register");
    }

}
