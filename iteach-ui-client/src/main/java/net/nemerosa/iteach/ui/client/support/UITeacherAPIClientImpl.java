package net.nemerosa.iteach.ui.client.support;

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
    public UIStudentCollection getStudents(Locale locale) {
        return get(locale, UIStudentCollection.class, "/api/teacher/student");
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
    public UILesson createLesson(Locale locale, UILessonForm form) {
        return post(locale, UILesson.class, form, "/api/teacher/lesson");
    }
}
