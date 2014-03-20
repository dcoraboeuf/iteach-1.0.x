package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.ui.model.*;

import java.util.Locale;

/**
 * UI for the teacher.
 */
public interface UITeacherAPI {

    /**
     * Gets the list of schools for a teacher
     */
    UISchoolCollection getSchools(Locale locale);

    /**
     * Creates a school
     */
    UISchool createSchool(Locale locale, UISchoolForm form);

    /**
     * Gets a school
     */
    UISchool getSchool(Locale locale, int schoolId);

    /**
     * Updates a school
     */
    UISchool updateSchool(Locale locale, int schoolId, UISchoolForm form);

    /**
     * Gets the list of students for a teacher
     */
    UIStudentCollection getStudents(Locale locale);

    /**
     * Creates a student
     */
    UIStudent createStudent(Locale locale, UIStudentForm form);

    /**
     * Gets a student
     */
    UIStudent getStudent(Locale locale, int studentId);

    /**
     * Creates a lesson
     */
    UILesson createLesson(Locale locale, UILessonForm form);

}
