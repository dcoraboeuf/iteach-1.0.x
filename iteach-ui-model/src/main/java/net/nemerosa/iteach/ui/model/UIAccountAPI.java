package net.nemerosa.iteach.ui.model;

import net.nemerosa.iteach.common.Ack;

import java.util.Locale;

public interface UIAccountAPI {

    Ack registerAsTeacherWithPassword(Locale locale, UITeacherPasswordForm form);

}
