package net.nemerosa.iteach.ui;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;
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

    UIProfile getProfile(Locale locale);

    Ack saveProfile(Locale locale, UIProfile profile);

    UISetup getSetup(Locale locale);

    Ack saveSetup(Locale locale, UISetupForm form);

}
