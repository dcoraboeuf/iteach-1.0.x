package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.ui.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Controller for the teacher UI.
 */
@RestController
@RequestMapping("/api/teacher")
public class UITeacherAPIController implements UITeacherAPI {

    private final TeacherService teacherService;
    private final Function<? super School, ? extends UISchoolSummary> schoolSummaryFn = school -> new UISchoolSummary(
            school.getId(),
            school.getName(),
            school.getColour()
    );
    private final Function<? super Student, ? extends UIStudentSummary> studentSummaryFn = student -> new UIStudentSummary(
            student.getId(),
            getSchoolSummary(student),
            student.getName()
    );

    private UISchoolSummary getSchoolSummary(Student student) {
        return schoolSummaryFn.apply(teacherService.getSchool(student.getSchoolId()));
    }

    private UIStudentSummary getStudentSummary(int studentId) {
        return studentSummaryFn.apply(teacherService.getStudent(studentId));
    }

    @Autowired
    public UITeacherAPIController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    @RequestMapping(value = "/school", method = RequestMethod.GET)
    public UISchoolCollection getSchools(Locale locale) {
        List<School> schools = teacherService.getSchools();
        return new UISchoolCollection(
                schools.stream().map(schoolSummaryFn).collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/school", method = RequestMethod.POST)
    public UISchool createSchool(Locale locale, @RequestBody @Valid UISchoolForm form) {
        int schoolId = teacherService.createSchool(toSchoolForm(form));
        return getSchool(locale, schoolId);
    }

    private SchoolForm toSchoolForm(UISchoolForm form) {
        return new SchoolForm(
                form.getName(),
                form.getColour(),
                form.getContact(),
                form.toHourlyRate(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getEmail(),
                form.getWebSite()
        );
    }

    @Override
    @RequestMapping(value = "/school/{schoolId}", method = RequestMethod.GET)
    public UISchool getSchool(Locale locale, @PathVariable int schoolId) {
        School o = teacherService.getSchool(schoolId);
        return new UISchool(
                o.getId(),
                o.getName(),
                o.getColour(),
                o.getContact(),
                o.getHourlyRate(),
                o.getPostalAddress(),
                o.getPhone(),
                o.getMobilePhone(),
                o.getEmail(),
                o.getWebSite()
        );
    }

    @Override
    @RequestMapping(value = "/school/{schoolId}", method = RequestMethod.PUT)
    public UISchool updateSchool(Locale locale, @PathVariable int schoolId, @RequestBody UISchoolForm form) {
        teacherService.updateSchool(schoolId, toSchoolForm(form));
        return getSchool(locale, schoolId);
    }

    @Override
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public UIStudentCollection getStudents(Locale locale) {
        List<Student> schools = teacherService.getStudents();
        return new UIStudentCollection(
                schools.stream().map(studentSummaryFn).collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public UIStudent createStudent(Locale locale, @RequestBody @Valid UIStudentForm form) {
        int studentId = teacherService.createStudent(
                new StudentForm(
                        form.getSchoolId(),
                        form.getName(),
                        form.getSubject(),
                        form.getPostalAddress(),
                        form.getPhone(),
                        form.getMobilePhone(),
                        form.getEmail()
                )
        );
        return getStudent(locale, studentId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    public UIStudent getStudent(Locale locale, @PathVariable int studentId) {
        Student o = teacherService.getStudent(studentId);
        return new UIStudent(
                o.getId(),
                getSchoolSummary(o),
                o.getName(),
                o.getSubject(),
                o.getPostalAddress(),
                o.getPhone(),
                o.getMobilePhone(),
                o.getEmail()
        );
    }

    @Override
    @RequestMapping(value = "/lesson", method = RequestMethod.POST)
    public UILesson createLesson(Locale locale, @RequestBody @Valid UILessonForm form) {
        // Creation
        int lessonId = teacherService.createLesson(
                new LessonForm(
                        form.getStudentId(),
                        form.getLocation(),
                        form.getFrom(),
                        form.getTo()
                )
        );
        // Returns the lesson
        return getLesson(locale, lessonId);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId}", method = RequestMethod.GET)
    public UILesson getLesson(Locale locale, @PathVariable int lessonId) {
        Lesson lesson = teacherService.getLesson(lessonId);
        return toUILesson(lesson);
    }

    private UILesson toUILesson(Lesson lesson) {
        return new UILesson(
                lesson.getId(),
                getStudentSummary(lesson.getStudentId()),
                lesson.getLocation(),
                lesson.getFrom(),
                lesson.getTo()
        );
    }

    @Override
    @RequestMapping(value = "/lesson/filter", method = RequestMethod.POST)
    public UILessonCollection filterLessons(Locale locale, @RequestBody UILessonFilter filter) {
        return new UILessonCollection(
                filter,
                teacherService
                        .getLessons(filter.getStudentId(), filter.getFrom(), filter.getTo())
                        .stream()
                        .map(this::toUILesson)
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId}", method = RequestMethod.DELETE)
    public Ack deleteLesson(Locale locale, @PathVariable int lessonId) {
        return teacherService.deleteLesson(lessonId);
    }
}
