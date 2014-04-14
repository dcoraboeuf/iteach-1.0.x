package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.common.Document;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.InvoiceService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.ui.model.*;
import net.nemerosa.iteach.ui.support.UIFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.YearMonth;
import java.util.Collections;
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
    private final InvoiceService invoiceService;
    private final CommentService commentService;
    private final UIFormatter formatter;

    private UISchoolSummary toUISchoolSummary(School school) {
        return new UISchoolSummary(
                school.getId(),
                school.getName(),
                school.getColour(),
                commentService.hasComments(CommentEntity.school, school.getId()));
    }

    private UIStudentSummary toUIStudentSummary(Student student) {
        return new UIStudentSummary(
                student.getId(),
                student.isDisabled(),
                getSchoolSummary(student),
                student.getName(),
                student.getSubject(),
                commentService.hasComments(CommentEntity.student, student.getId()));
    }

    private UISchoolSummary getSchoolSummary(Student student) {
        return toUISchoolSummary(teacherService.getSchool(student.getSchoolId()));
    }

    private UIStudentSummary getStudentSummary(int studentId) {
        return toUIStudentSummary(teacherService.getStudent(studentId));
    }

    @Autowired
    public UITeacherAPIController(TeacherService teacherService, InvoiceService invoiceService, CommentService commentService, UIFormatter formatter) {
        this.teacherService = teacherService;
        this.invoiceService = invoiceService;
        this.commentService = commentService;
        this.formatter = formatter;
    }

    @Override
    @RequestMapping(value = "/school", method = RequestMethod.GET)
    public UISchoolCollection getSchools(Locale locale) {
        List<School> schools = teacherService.getSchools();
        return new UISchoolCollection(
                schools.stream().map(this::toUISchoolSummary).collect(Collectors.toList())
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
                form.getWebSite(),
                form.getVat(),
                form.getVatRate()
        );
    }

    @Override
    @RequestMapping(value = "/school/{schoolId}", method = RequestMethod.GET)
    public UISchool getSchool(Locale locale, @PathVariable int schoolId) {
        School o = teacherService.getSchool(schoolId);
        return toUISchool(o);
    }

    private static UISchool toUISchool(School o) {
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
                o.getWebSite(),
                o.getVat(),
                o.getVatRate()
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
        SchoolReport report = teacherService.getSchoolReport(schoolId, period, false);
        // Transforms it to UI
        return toUISchoolReport(report);
    }

    private UISchoolReport toUISchoolReport(SchoolReport report) {
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
                        .map(this::toUIStudentSummary)
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
        return toUILesson(lesson);
    }

    private UILesson toUILesson(Lesson lesson) {
        return new UILesson(
                lesson.getId(),
                getStudentSummary(lesson.getStudentId()),
                lesson.getLocation(),
                lesson.getFrom(),
                lesson.getTo(),
                lesson.getHours()
        );
    }

    private UILessonSummary toUILessonSummary(Lesson lesson) {
        Student student = teacherService.getStudent(lesson.getStudentId());
        School school = teacherService.getSchool(student.getSchoolId());
        return new UILessonSummary(
                lesson.getId(),
                student.getName(),
                school.getName(),
                school.getColour(),
                lesson.getLocation(),
                lesson.getFrom(),
                lesson.getTo(),
                commentService.hasComments(CommentEntity.lesson, lesson.getId()));
    }

    @Override
    @RequestMapping(value = "/lesson/filter", method = RequestMethod.POST)
    public UILessonCollection filterLessons(Locale locale, @RequestBody UILessonFilter filter) {
        return new UILessonCollection(
                filter,
                teacherService
                        .getLessons(filter.getStudentId(), filter.getFrom(), filter.getTo())
                        .stream()
                        .map(this::toUILessonSummary)
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
                formatter.formatMonth(period, locale),
                period.minusMonths(1),
                period.plusMonths(1),
                report.getTotalHours(),
                report.getPeriodHours(),
                report.getLessons()
                        .stream()
                        .map(this::toUILesson)
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/report/{year}/{month}", method = RequestMethod.GET)
    public UIReport getReport(Locale locale, @PathVariable int year, @PathVariable int month) {
        YearMonth period = YearMonth.of(year, month);
        Report report = teacherService.getReport(period);
        return new UIReport(
                period,
                formatter.formatMonth(period, locale),
                period.minusMonths(1),
                period.plusMonths(1),
                report.getHours(),
                report.getIncome(),
                report.getSchools()
                        .stream()
                        .map(this::toUISchoolReport)
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    public UIInvoiceInfo generateInvoice(Locale locale, @RequestBody @Valid UIInvoiceForm form) {
        return toUIInvoiceInfo(invoiceService.generate(
                new InvoiceForm(
                        form.getSchoolId(),
                        YearMonth.of(form.getYear(), form.getMonth()),
                        form.getNumber(),
                        form.isDetailPerStudent(),
                        form.getComment()
                ),
                "application/pdf", // TODO Only PDF is supported right now
                locale
        ));
    }

    private UIInvoiceInfo toUIInvoiceInfo(InvoiceInfo info) {
        return new UIInvoiceInfo(
                info.getId(),
                info.getStatus(),
                info.getErrorMessage(),
                info.getErrorUuid(),
                toUISchoolSummary(teacherService.getSchool(info.getSchoolId())),
                info.getPeriod(),
                info.getNumber(),
                info.getGeneration(),
                info.isDownloaded(),
                info.getDocumentType()
        );
    }

    @Override
    @RequestMapping(value = "/invoice/{invoiceId}", method = RequestMethod.GET)
    public UIInvoiceInfo getInvoiceInfo(Locale locale, @PathVariable int invoiceId) {
        return toUIInvoiceInfo(invoiceService.getInvoiceInfo(invoiceId));
    }

    @Override
    @RequestMapping(value = "/invoice/form", method = RequestMethod.GET)
    public UIInvoiceFormData getInvoiceFormData(Locale locale) {
        return new UIInvoiceFormData(
                invoiceService.getNextInvoiceNumber()
        );
    }

    @Override
    @RequestMapping(value = "/invoice/{invoiceId}/download", method = RequestMethod.GET)
    public Document downloadInvoice(Locale locale, @PathVariable int invoiceId) {
        return invoiceService.downloadInvoice(invoiceId);
    }

    @Override
    @RequestMapping(value = "/invoice/filter", method = RequestMethod.POST)
    public UIInvoiceCollection getInvoices(Locale locale, @RequestBody UIInvoiceFilter filter) {
        return new UIInvoiceCollection(
                invoiceService.getInvoices(new InvoiceFilter(
                        filter.getSchoolId(),
                        filter.getYear(),
                        filter.getDownloaded(),
                        filter.getStatus()))
                        .stream()
                        .map(this::toUIInvoiceInfo)
                        .collect(Collectors.toList()),
                filter
        );
    }

    @Override
    @RequestMapping(value = "/invoice/delete", method = RequestMethod.PUT)
    public Ack deleteInvoices(Locale locale, @RequestBody UISelection selection) {
        return invoiceService.deleteInvoices(selection.getIds());
    }

    @Override
    @RequestMapping(value = "/invoice/{invoiceId}", method = RequestMethod.DELETE)
    public Ack deleteInvoice(Locale locale, @PathVariable int invoiceId) {
        return invoiceService.deleteInvoices(Collections.singletonList(invoiceId));
    }

    @RequestMapping(value = "/invoice/{invoiceId}/download/attached", method = RequestMethod.GET)
    public void downloadInvoice(Locale locale, @PathVariable int invoiceId, HttpServletResponse response) throws IOException {
        Document document = downloadInvoice(locale, invoiceId);
        // Writes as a file
        response.setContentType(document.getType());
        response.addHeader("Content-Disposition", String.format("attachment; filename=%s.%s", document.getTitle(), document.getExtension()));
        // Serializes
        response.getOutputStream().write(document.getContent());
        response.getOutputStream().flush();
        // Marks the document as downloaded
        invoiceService.invoiceIsDownloaded(invoiceId);
    }
}
