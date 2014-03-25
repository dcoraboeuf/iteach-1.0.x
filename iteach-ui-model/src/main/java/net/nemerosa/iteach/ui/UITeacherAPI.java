package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.ui.model.*;

import java.util.List;
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
     * Deletes a school
     */
    Ack deleteSchool(Locale locale, int schoolId);

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

    /**
     * Gets a lesson
     */
    UILesson getLesson(Locale locale, int lessonId);

    /**
     * Filtered list of resources
     */
    UILessonCollection filterLessons(Locale locale, UILessonFilter filter);

    /**
     * Updating a lesson
     */
    UILesson updateLesson(Locale locale, int lessonId, UILessonForm form);

    /**
     * Deleting a lesson
     */
    Ack deleteLesson(Locale locale, int lessonId);

}
