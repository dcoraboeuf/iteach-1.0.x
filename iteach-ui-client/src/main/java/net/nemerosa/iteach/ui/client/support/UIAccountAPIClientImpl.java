package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.ui.client.UIAccountAPIClient;
import net.nemerosa.iteach.ui.model.UIAccount;
import net.nemerosa.iteach.ui.model.UITeacherPasswordForm;

import java.util.List;
import java.util.Locale;

public class UIAccountAPIClientImpl extends AbstractClient implements UIAccountAPIClient {

    public UIAccountAPIClientImpl(String url) {
        super(url);
    }

    @Override
    public List<UIAccount> getAccounts(Locale locale) {
        return list(locale, UIAccount.class, "/api/account");
    }

    @Override
    public ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form) {
        return post(locale, ID.class, form, "/api/account/register");
    }

}
