package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;

import java.util.List;
import java.util.Locale;

public interface UIAccountAPI {

    UIState state(Locale locale);

    UITeacher login(Locale locale);

    List<UIAccount> getAccounts(Locale locale);

    ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

    Ack validate(Locale locale, TokenType tokenType, String token);
}
