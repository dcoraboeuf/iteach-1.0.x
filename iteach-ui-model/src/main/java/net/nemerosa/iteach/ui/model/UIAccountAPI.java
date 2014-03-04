package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.ID;

import java.util.Locale;

public interface UIAccountAPI {

    ID registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

}
