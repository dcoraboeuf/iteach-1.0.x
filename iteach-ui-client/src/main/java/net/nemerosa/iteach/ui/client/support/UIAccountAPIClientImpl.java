package net.nemerosa.iteach.ui.client.support;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.model.*;

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
    public UIAccount getAccount(Locale locale, int accountId) {
        return get(locale, UIAccount.class, "/api/account/%d", accountId);
    }

    @Override
    public Ack deleteAccount(Locale locale, int accountId) {
        return delete(locale, Ack.class, "/api/account/%d", accountId);
    }

    @Override
    public ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form) {
        return post(locale, ID.class, form, "/api/account/register");
    }

    @Override
    public Ack validate(Locale locale, TokenType tokenType, String token) {
        return get(locale, Ack.class, "/api/account/validate/%s/%s", tokenType, token);
    }

    @Override
    public UIAccount importAccount(Locale locale, int accountId, JsonNode data) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UIAccountAPIClientImpl.importAccount
        return null;
    }

    @Override
    public JsonNode exportAccount(Locale locale, int accountId) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UIAccountAPIClientImpl.exportAccount
        return null;
    }

    @Override
    public UIProfile getProfile(Locale locale) {
        return get(locale, UIProfile.class, "/api/account/profile");
    }

    @Override
    public Ack saveProfile(Locale locale, UIProfile profile) {
        return put(locale, Ack.class, profile, "/api/account/profile");
    }

    @Override
    public UISetup getSetup(Locale locale) {
        return get(locale, UISetup.class, "/api/account/setup");
    }

    @Override
    public Ack saveSetup(Locale locale, UISetupForm form) {
        return put(locale, Ack.class, form, "/api/account/setup");
    }
}
