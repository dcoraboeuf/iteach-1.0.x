package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.model.UIAccountCollection;
import net.nemerosa.iteach.ui.model.UIState;
import net.nemerosa.iteach.ui.model.UITeacher;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;

import java.net.MalformedURLException;
import java.util.Locale;

public class UIAccountAPIClientImpl extends AbstractClient<UIAccountAPIClient> implements UIAccountAPIClient {

    public UIAccountAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public UIState state(Locale locale) {
        return get(locale, UIState.class, "/api/account/state");
    }

    @Override
    public UITeacher login(Locale locale) {
        return get(locale, UITeacher.class, "/api/account/login");
    }

    @Override
    public UIAccountCollection getAccounts(Locale locale) {
        return get(locale, UIAccountCollection.class, "/api/account");
    }

    @Override
    public ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form) {
        return post(locale, ID.class, form, "/api/account/register");
    }

    @Override
    public Ack validate(Locale locale, TokenType tokenType, String token) {
        return get(locale, Ack.class, "/api/account/validate/%s/%s", tokenType, token);
    }

}
