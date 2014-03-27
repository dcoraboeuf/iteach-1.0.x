package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.ui.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
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
            student.isDisabled(),
            getSchoolSummary(student),
            student.getName(),
            student.getSubject());

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
    @RequestMapping(value = "/school/{schoolId}", method = RequestMethod.DELETE)
    public Ack deleteSchool(Locale locale, @PathVariable int schoolId) {
        return teacherService.deleteSchool(schoolId);
    }

    @Override
    @RequestMapping(value = "/school/{schoolId}/report", method = RequestMethod.POST)
    public UISchoolReport getSchoolReport(Locale locale, @PathVariable int schoolId, @RequestBody Period period) {
        // Gets the school report
        SchoolReport report = teacherService.getSchoolReport(schoolId, period);
        // Transforms it to UI
        return new UISchoolReport(
                report.getId(),
                report.getName(),
                report.getColour(),
                report.getHourlyRate(),
                report.getHours(),
                report.getIncome(),
                report.getStudents()
                        .stream()
                        .map(s -> new UIStudentReport(
                                s.getId(),
                                s.isDisabled(),
                                s.getName(),
                                s.getSubject(),
                                s.getHours(),
                                s.getIncome()
                        ))
                        .collect(Collectors.toList())
        );
    }


    @Override
    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public UIStudentCollection getStudents(Locale locale, @RequestParam(required = false, defaultValue = "false") boolean filtered) {
        List<Student> schools = teacherService.getStudents();
        return new UIStudentCollection(
                schools
                        .stream()
                        .filter(o -> !filtered || !o.isDisabled())
                        .map(studentSummaryFn)
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public UIStudent createStudent(Locale locale, @RequestBody @Valid UIStudentForm form) {
        int studentId = teacherService.createStudent(
                toStudentForm(form)
        );
        return getStudent(locale, studentId);
    }

    private StudentForm toStudentForm(UIStudentForm form) {
        return new StudentForm(
                form.getSchoolId(),
                form.getName(),
                form.getSubject(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getEmail()
        );
    }

    @Override
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.GET)
    public UIStudent getStudent(Locale locale, @PathVariable int studentId) {
        Student o = teacherService.getStudent(studentId);
        return new UIStudent(
                o.getId(),
                o.isDisabled(),
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
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.PUT)
    public UIStudent updateStudent(Locale locale, @PathVariable int studentId, @RequestBody @Valid UIStudentForm form) {
        teacherService.updateStudent(studentId, toStudentForm(form));
        return getStudent(locale, studentId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId}", method = RequestMethod.DELETE)
    public Ack deleteStudent(Locale locale, @PathVariable int studentId) {
        return teacherService.deleteStudent(studentId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId}/disable", method = RequestMethod.PUT)
    public Ack disableStudent(Locale locale, @PathVariable int studentId) {
        return teacherService.disableStudent(studentId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId}/enable", method = RequestMethod.PUT)
    public Ack enableStudent(Locale locale, @PathVariable int studentId) {
        return teacherService.enableStudent(studentId);
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
        return toUILesson(locale, lesson);
    }

    private UILesson toUILesson(Locale locale, Lesson lesson) {
        return new UILesson(
                lesson.getId(),
                getStudentSummary(lesson.getStudentId()),
                lesson.getLocation(),
                lesson.getFrom(),
                lesson.getTo(),
                lesson.getFrom().toLocalDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)),
                lesson.getFrom().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)),
                lesson.getTo().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale)),
                lesson.getHours()
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
                        .map(l -> toUILesson(locale, l))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId}", method = RequestMethod.PUT)
    public UILesson updateLesson(Locale locale, @PathVariable int lessonId, @RequestBody @Valid UILessonForm form) {
        teacherService.updateLesson(
                lessonId,
                new LessonForm(
                        form.getStudentId(),
                        form.getLocation(),
                        form.getFrom(),
                        form.getTo()
                )
        );
        return getLesson(locale, lessonId);
    }

    @Override
    @RequestMapping(value = "/lesson/{lessonId}", method = RequestMethod.DELETE)
    public Ack deleteLesson(Locale locale, @PathVariable int lessonId) {
        return teacherService.deleteLesson(lessonId);
    }

    @Override
    @RequestMapping(value = "/student/{studentId}/lessons/{year}/{month}", method = RequestMethod.GET)
    public UILessonReport getLessonReport(Locale locale, @PathVariable int studentId, @PathVariable int year, @PathVariable int month) {
        YearMonth period = YearMonth.of(year, month);
        LessonReport report = teacherService.getLessonReport(studentId, period);
        return new UILessonReport(
                studentId,
                period,
                period.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                period.minusMonths(1),
                period.plusMonths(1),
                report.getTotalHours(),
                report.getPeriodHours(),
                report.getLessons()
                        .stream()
                        .map(l -> toUILesson(locale, l))
                        .collect(Collectors.toList())
        );
    }
}
