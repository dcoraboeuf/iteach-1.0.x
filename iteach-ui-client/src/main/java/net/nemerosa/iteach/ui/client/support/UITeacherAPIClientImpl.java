package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.ui.client.UITeacherAPIClient;
import net.nemerosa.iteach.ui.model.*;

import java.net.MalformedURLException;
import java.util.Locale;

public class UITeacherAPIClientImpl extends AbstractClient<UITeacherAPIClient> implements UITeacherAPIClient {

    public UITeacherAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public UISchoolCollection getSchools(Locale locale) {
        return get(locale, UISchoolCollection.class, "/api/teacher/school");
    }

    @Override
    public UISchool createSchool(Locale locale, UISchoolForm form) {
        return post(locale, UISchool.class, form, "/api/teacher/school");
    }

    @Override
    public UISchool getSchool(Locale locale, int schoolId) {
        return get(locale, UISchool.class, "/api/teacher/school/%d", schoolId);
    }

    @Override
    public UISchool updateSchool(Locale locale, int schoolId, UISchoolForm form) {
        return put(locale, UISchool.class, form, "/api/teacher/school/%d", schoolId);
    }

    @Override
    public Ack deleteSchool(Locale locale, int schoolId) {
        return delete(locale, Ack.class, "/api/teacher/school/%d", schoolId);
    }

    @Override
    public UISchoolReport getSchoolReport(Locale locale, int schoolId, Period period) {
        return post(locale, UISchoolReport.class, period, "/api/teacher/school/%d/report", schoolId);
    }

    @Override
    public UIContractCollection getContracts(Locale locale, int schoolId) {
        return get(locale, UIContractCollection.class, "/api/teacher/school/%d/contract", schoolId);
    }

    @Override
    public UIContract createContract(Locale locale, int schoolId, UIContractForm form) {
        return post(locale, UIContract.class, form, "/api/teacher/school/%d/contract", schoolId);
    }

    @Override
    public UIContract getContract(Locale locale, int contractId) {
        return get(locale, UIContract.class, "/api/teacher/contract/%d", contractId);
    }

    @Override
    public Ack deleteContract(Locale locale, int contractId) {
        return delete(locale, Ack.class, "/api/teacher/contract/%d", contractId);
    }

    @Override
    public UIContract updateContract(Locale locale, int contractId, UIContractForm form) {
        return put(locale, UIContract.class, form, "/api/teacher/contract/%d", contractId);
    }

    @Override
    public UIStudentCollection getStudents(Locale locale, boolean filtered) {
        return get(locale, UIStudentCollection.class, "/api/teacher/student?filtered=%s", filtered);
    }

    @Override
    public UIStudent createStudent(Locale locale, UIStudentForm form) {
        return post(locale, UIStudent.class, form, "/api/teacher/student");
    }

    @Override
    public UIStudent getStudent(Locale locale, int studentId) {
        return get(locale, UIStudent.class, "/api/teacher/student/%d", studentId);
    }

    @Override
    public UIStudent updateStudent(Locale locale, int studentId, UIStudentForm form) {
        return put(locale, UIStudent.class, form, "/api/teacher/student/%d", studentId);
    }

    @Override
    public Ack disableStudent(Locale locale, int studentId) {
        return put(locale, Ack.class, null, "/api/teacher/student/%d/disable", studentId);
    }

    @Override
    public Ack enableStudent(Locale locale, int studentId) {
        return put(locale, Ack.class, null, "/api/teacher/student/%d/enable", studentId);
    }

    @Override
    public Ack deleteStudent(Locale locale, int studentId) {
        return delete(locale, Ack.class, "/api/teacher/student/%d", studentId);
    }

    @Override
    public UILesson createLesson(Locale locale, UILessonForm form) {
        return post(locale, UILesson.class, form, "/api/teacher/lesson");
    }

    @Override
    public UILesson getLesson(Locale locale, int lessonId) {
        return get(locale, UILesson.class, "/api/teacher/lesson/%d", lessonId);
    }

    @Override
    public UILessonCollection filterLessons(Locale locale, UILessonFilter filter) {
        return post(locale, UILessonCollection.class, filter, "/api/teacher/lesson/filter");
    }

    @Override
    public UILesson updateLesson(Locale locale, int lessonId, UILessonForm form) {
        return put(locale, UILesson.class, form, "/api/teacher/lesson/%s", lessonId);
    }

    @Override
    public Ack deleteLesson(Locale locale, int lessonId) {
        return delete(locale, Ack.class, "/api/teacher/lesson/%d", lessonId);
    }

    @Override
    public UILessonReport getLessonReport(Locale locale, int studentId, int year, int month) {
        return get(locale, UILessonReport.class, "/api/teacher/student/%d/lessons/%d/%s", studentId, year, month);
    }

    @Override
    public UIReport getReport(Locale locale, int year, int month) {
        return get(locale, UIReport.class, "/api/teacher/report/%d/%d", year, month);
    }

    @Override
    public UIInvoiceInfo generateInvoice(Locale locale, UIInvoiceForm form) {
        return post(locale, UIInvoiceInfo.class, form, "/api/teacher/invoice");
    }

    @Override
    public UIInvoiceInfo getInvoiceInfo(Locale locale, int invoiceId) {
        return get(locale, UIInvoiceInfo.class, "/api/teacher/invoice/%d", invoiceId);
    }

    @Override
    public UIInvoiceFormData getInvoiceFormData(Locale locale) {
        return get(locale, UIInvoiceFormData.class, "/api/teacher/invoice/form");
    }

    @Override
    public Document downloadInvoice(Locale locale, int invoiceId) {
        return get(locale, Document.class, "/api/teacher/invoice/%d/download", invoiceId);
    }

    @Override
    public UIInvoiceCollection getInvoices(Locale locale, UIInvoiceFilter filter) {
        return post(locale, UIInvoiceCollection.class, filter, "/api/teacher/invoice/filter");
    }

    @Override
    public Ack deleteInvoices(Locale locale, UISelection selection) {
        return put(locale, Ack.class, selection, "/api/teacher/invoice/delete");
    }

    @Override
    public Ack deleteInvoice(Locale locale, int invoiceId) {
        return delete(locale, Ack.class, "/api/teacher/invoice/%d", invoiceId);
    }
}
