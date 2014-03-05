package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.ui.model.UIForm;
import net.nemerosa.iteach.ui.model.UISchool;
import net.nemerosa.iteach.ui.model.UISchoolSummary;
import net.nemerosa.iteach.ui.model.UITeacherAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controller for the teacher UI.
 */
@RestController
@RequestMapping("/api/teacher")
public class UITeacherAPIController implements UITeacherAPI {

    private final TeacherService teacherService;

    @Autowired
    public UITeacherAPIController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    @RequestMapping(value = "/{teacherId}/school", method = RequestMethod.GET)
    public List<UISchoolSummary> getSchools(Locale locale, @PathVariable int teacherId) {
        List<School> schools = teacherService.getSchools(teacherId);
        return schools.parallelStream().map(school -> new UISchoolSummary(
                school.getId(),
                school.getName(),
                school.getColour()
        )).collect(Collectors.toList());
    }

    @Override
    @RequestMapping(value = "/{teacherId}/school", method = RequestMethod.POST)
    public UISchool createSchool(Locale locale, @PathVariable int teacherId, @RequestBody UIForm form) {
        int schoolId = teacherService.createSchool(
                teacherId,
                new SchoolForm(
                        form.getName(),
                        form.getColour(),
                        form.getContact(),
                        form.getHourlyRate(),
                        form.getPostalAddress(),
                        form.getPhone(),
                        form.getMobilePhone(),
                        form.getEmail(false),
                        form.getWebSite()
                )
        );
        return getSchool(locale, teacherId, schoolId);
    }

    @Override
    @RequestMapping(value = "/{teacherId}/school/{schoolId}", method = RequestMethod.POST)
    public UISchool getSchool(Locale locale, @PathVariable int teacherId, @PathVariable int schoolId) {
        // FIXME Method net.nemerosa.iteach.ui.UITeacherAPIController.getSchool
        return null;
    }
}
