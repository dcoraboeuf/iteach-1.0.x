package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.ID;

import java.util.List;
import java.util.Locale;

public interface UIAccountAPI {

    List<UIAccount> getAccounts(Locale locale);

    ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

}
