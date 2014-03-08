package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.ui.client.UITeacherAPIClient;
import net.nemerosa.iteach.ui.model.UIForm;
import net.nemerosa.iteach.ui.model.UISchool;
import net.nemerosa.iteach.ui.model.UISchoolSummary;

import java.util.List;
import java.util.Locale;

public class UITeacherAPIClientImpl extends AbstractClient<UITeacherAPIClient> implements UITeacherAPIClient {

    public UITeacherAPIClientImpl(String url) {
        super(url);
    }

    @Override
    public List<UISchoolSummary> getSchools(Locale locale) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UITeacherAPIClientImpl.getSchools
        return null;
    }

    @Override
    public UISchool createSchool(Locale locale, UIForm form) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UITeacherAPIClientImpl.createSchool
        return null;
    }

    @Override
    public UISchool getSchool(Locale locale, int schoolId) {
        // FIXME Method net.nemerosa.iteach.ui.client.support.UITeacherAPIClientImpl.getSchool
        return null;
    }
}
