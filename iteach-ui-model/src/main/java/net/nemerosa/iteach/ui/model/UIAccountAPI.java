package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.ID;

import java.util.List;
import java.util.Locale;

public interface UIAccountAPI {

    UITeacher login(Locale locale);

    List<UIAccount> getAccounts(Locale locale);

    ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

}
