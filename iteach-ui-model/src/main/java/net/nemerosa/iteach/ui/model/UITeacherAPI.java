package net.nemerosa.iteach.ui.model;

import java.util.Locale;

/**
 * UI for the teacher.
 */
public interface UITeacherAPI {

    /**
     * Creates a school
     */
    UISchool createSchool(Locale locale, int teacherId, UIForm form);

    /**
     * Gets a school
     */
    UISchool getSchool(Locale locale, int teacherId, int schoolId);

}
