package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.ui.client.UITeacherAPIClient;
import net.nemerosa.iteach.ui.model.UIForm;
import net.nemerosa.iteach.ui.model.UISchool;
import net.nemerosa.iteach.ui.model.UISchoolSummary;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

public class UITeacherAPIClientImpl extends AbstractClient<UITeacherAPIClient> implements UITeacherAPIClient {

    public UITeacherAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public List<UISchoolSummary> getSchools(Locale locale) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UITeacherAPIClientImpl.getSchools
        return null;
    }

    @Override
    public UISchool createSchool(Locale locale, UIForm form) {
        return post(locale, UISchool.class, form, "/api/teacher/school");
    }

    @Override
    public UISchool getSchool(Locale locale, int schoolId) {
        return get(locale, UISchool.class, "/api/teacher/school/%d", schoolId);
    }
}
