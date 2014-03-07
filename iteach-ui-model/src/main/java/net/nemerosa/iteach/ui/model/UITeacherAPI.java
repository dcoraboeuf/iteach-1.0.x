package net.nemerosa.iteach.ui.model;

import java.util.List;
import java.util.Locale;

/**
 * UI for the teacher.
 */
public interface UITeacherAPI {

    /**
     * Gets the list of schools for a teacher
     */
    List<UISchoolSummary> getSchools(Locale locale);

    /**
     * Creates a school
     */
    UISchool createSchool(Locale locale, UIForm form);

    /**
     * Gets a school
     */
    UISchool getSchool(Locale locale, int schoolId);

}
