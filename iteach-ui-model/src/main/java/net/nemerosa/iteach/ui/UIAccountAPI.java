package net.nemerosa.iteach.ui;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.common.*;
import net.nemerosa.iteach.ui.model.*;

import java.util.Locale;

public interface UIAccountAPI {

    UIState state(Locale locale);

    UITeacher login(Locale locale);

    UIAccountCollection getAccounts(Locale locale);

    UIAccount getAccount(Locale locale, int accountId);

    Ack deleteAccount(Locale locale, int accountId);

    ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

    Ack validate(Locale locale, TokenType tokenType, String token);

    UIAccount importAccount(Locale locale, int accountId, JsonNode data);

    JsonNode exportAccount(Locale locale, int accountId);

    UIProfile getProfile(Locale locale);

    Ack saveProfile(Locale locale, UIProfile profile);

    Ack updateProfileCompanyLogo(Locale locale, Document file);

    UntitledDocument getProfileCompanyLogo(Locale locale);

    UISetup getSetup(Locale locale);

    Ack saveSetup(Locale locale, UISetupForm form);

    Ack disableAccount(Locale locale, int accountId);

    Ack enableAccount(Locale locale, int accountId);

    Ack passwordChangeRequest(Locale locale);

    Ack passwordChange(Locale locale, UIPasswordChangeForm form);

}
