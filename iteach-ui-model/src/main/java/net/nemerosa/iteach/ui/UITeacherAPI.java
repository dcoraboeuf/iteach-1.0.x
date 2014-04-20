package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.Period;
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
     * School report
     */
    UISchoolReport getSchoolReport(Locale locale, int schoolId, Period period);

    /**
     * List of contracts for a school
     */
    UIContractCollection getContracts(Locale locale, int schoolId);

    /**
     * Creates a contract for a school
     */
    UIContract createContract(Locale locale, int schoolId, UIContractForm form);

    /**
     * Gets a contract by ID
     */
    UIContract getContract(Locale locale, int contractId);

    /**
     * Deletes a contract
     */
    Ack deleteContract(Locale locale, int contractId);

    /**
     * Updates a contract
     */
    UIContract updateContract(Locale locale, int contractId, UIContractForm form);

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

    /**
     * Invoice data
     */
    UIInvoiceInfo generateInvoice(Locale locale, UIInvoiceForm form);

    /**
     * Gets invoice information
     */
    UIInvoiceInfo getInvoiceInfo(Locale locale, int invoiceId);

    /**
     * Invoice form data
     */
    UIInvoiceFormData getInvoiceFormData(Locale locale);

    /**
     * Downloads an invoice
     */
    Document downloadInvoice(Locale locale, int invoiceId);

    /**
     * List of invoices
     */
    UIInvoiceCollection getInvoices(Locale locale, UIInvoiceFilter filter);

    /**
     * Deletes a list of invoices
     */
    Ack deleteInvoices(Locale locale, UISelection selection);

    /**
     * Deletes one invoice by ID
     */
    Ack deleteInvoice(Locale locale, int invoiceId);

}
