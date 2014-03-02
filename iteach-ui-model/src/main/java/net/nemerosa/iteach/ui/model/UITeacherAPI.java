package net.nemerosa.iteach.ui.model;

import java.util.Locale;

/**
 * UI for the teacher.
 */
public interface UITeacherAPI {

    /**
     * Creates a school
     */
    UISchool createSchool(Locale locale, UIForm form);

}
