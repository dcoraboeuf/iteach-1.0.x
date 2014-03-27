package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Period;
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
     * Deletes a school
     */
    Ack deleteSchool(Locale locale, int schoolId);

    /**
     * School report
     */
    UISchoolReport getSchoolReport(Locale locale, int schoolId, Period period);

    /**
     * Gets the list of students for a teacher
     *
     * @param filtered <code>true</code> if only the non-disabled students must be retained
     */
    UIStudentCollection getStudents(Locale locale, boolean filtered);

    /**
     * Creates a student
     */
    UIStudent createStudent(Locale locale, UIStudentForm form);

    /**
     * Gets a student
     */
    UIStudent getStudent(Locale locale, int studentId);

    /**
     * Updates a student
     */
    UIStudent updateStudent(Locale locale, int studentId, UIStudentForm form);

    /**
     * Disables a student
     */
    Ack disableStudent(Locale locale, int studentId);

    /**
     * Enables a student
     */
    Ack enableStudent(Locale locale, int studentId);

    /**
     * Deletes a student
     */
    Ack deleteStudent(Locale locale, int studentId);

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

    /**
     * Lesson report for a student
     */
    UILessonReport getLessonReport(Locale locale, int studentId, int year, int month);

    /**
     * Monthly report
     */
    UIReport getReport(Locale locale, int year, int month);

}
