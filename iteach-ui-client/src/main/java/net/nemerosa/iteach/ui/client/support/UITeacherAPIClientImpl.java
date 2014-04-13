package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.ui.client.UITeacherAPIClient;
import net.nemerosa.iteach.ui.model.*;

import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

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
    public UIInvoiceCollection getInvoices(Locale locale, Integer schoolId, Integer year) {
        String path = "/api/teacher/invoice";
        Map<String, Integer> params = new LinkedHashMap<>();
        if (schoolId != null) params.put("schoolId", schoolId);
        if (year != null) params.put("year", year);
        int count = 0;
        for (Map.Entry<String, Integer> entry : params.entrySet()) {
            if (count > 0) {
                path += "&";
            } else {
                path += "?";
            }
            path += entry.getKey() + "=" + entry.getValue();
            count++;
        }
        return get(locale, UIInvoiceCollection.class, path);
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
