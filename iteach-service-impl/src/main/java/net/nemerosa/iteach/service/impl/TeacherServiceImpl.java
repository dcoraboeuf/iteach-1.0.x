package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.StudentRepository;
import net.nemerosa.iteach.dao.model.TLesson;
import net.nemerosa.iteach.dao.model.TSchool;
import net.nemerosa.iteach.dao.model.TStudent;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final SecurityUtils securityUtils;
    private final Function<? super TSchool, ? extends School> schoolFn = t -> new School(
            t.getId(),
            t.getTeacherId(),
            t.getName(),
            t.getColour(),
            t.getContact(),
            t.getHourlyRate(),
            t.getPostalAddress(),
            t.getPhone(),
            t.getMobilePhone(),
            t.getEmail(),
            t.getWebSite(),
            t.getVat()
    );
    private final Function<? super TStudent, ? extends Student> studentFn = t -> new Student(
            t.getId(),
            t.getTeacherId(),
            t.getSchoolId(),
            t.isDisabled(),
            t.getName(),
            t.getSubject(),
            t.getPostalAddress(),
            t.getPhone(),
            t.getMobilePhone(),
            t.getEmail()
    );
    private final Function<? super TLesson, ? extends Lesson> lessonFn = t -> new Lesson(
            t.getId(),
            t.getTeacherId(),
            t.getStudentId(),
            t.getLocation(),
            t.getFrom(),
            t.getTo()
    );

    @Autowired
    public TeacherServiceImpl(SchoolRepository schoolRepository, StudentRepository studentRepository, LessonRepository lessonRepository, SecurityUtils securityUtils) {
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public List<School> getSchools() {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Lists
        return schoolRepository.findAll(teacherId).stream().map(schoolFn).collect(Collectors.toList());
    }

    @Override
    public int createSchool(SchoolForm form) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Creation
        return schoolRepository.create(
                teacherId,
                form.getName(),
                form.getColour(),
                form.getContact(),
                form.getEmail(),
                form.getHourlyRate(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getWebSite(),
                form.getVat()
        );
    }

    @Override
    public Ack updateSchool(int schoolId, SchoolForm form) {
        // Checks the teacher access
        School school = getSchool(schoolId);
        // Updates
        return schoolRepository.update(
                school.getTeacherId(),
                schoolId,
                form.getName(),
                form.getColour(),
                form.getContact(),
                form.getEmail(),
                form.getHourlyRate(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getWebSite(),
                form.getVat()
        );
    }

    @Override
    public Ack deleteSchool(int schoolId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Deletion
        return schoolRepository.delete(teacherId, schoolId);
    }

    @Override
    public School getSchool(int schoolId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Access
        return schoolFn.apply(schoolRepository.getById(teacherId, schoolId));
    }

    @Override
    public int createStudent(StudentForm form) {
        // Checks the teacher access to the school
        School school = getSchool(form.getSchoolId());
        // Creation
        return studentRepository.create(
                school.getTeacherId(),
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
    public Student getStudent(int studentId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Access
        return studentFn.apply(studentRepository.getById(teacherId, studentId));
    }

    @Override
    public List<Student> getStudents() {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Lists
        return studentRepository
                .findAll(teacherId)
                .stream()
                .map(studentFn)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> getStudentsForSchool(int schoolId) {
        // Gets the school
        int teacherId = getSchool(schoolId).getTeacherId();
        // Lists
        return studentRepository
                .findBySchool(teacherId, schoolId)
                .stream()
                .map(studentFn)
                .collect(Collectors.toList());
    }

    @Override
    public Ack disableStudent(int studentId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Gets previous version
        TStudent t = studentRepository.getById(teacherId, studentId);
        // Change
        studentRepository.disable(teacherId, studentId, true);
        // OK
        return Ack.validate(!t.isDisabled());
    }

    @Override
    public Ack enableStudent(int studentId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Gets previous version
        TStudent t = studentRepository.getById(teacherId, studentId);
        // Change
        studentRepository.disable(teacherId, studentId, false);
        // OK
        return Ack.validate(t.isDisabled());
    }

    @Override
    public Ack deleteStudent(int studentId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Change
        return studentRepository.delete(teacherId, studentId);
    }

    @Override
    public Ack updateStudent(int studentId, StudentForm form) {
        // Checks the teacher access
        Student student = getStudent(studentId);
        // Updates
        return studentRepository.update(
                student.getTeacherId(),
                studentId,
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
    public int createLesson(LessonForm form) {
        checkLessonForm(form);
        // Checks the teacher access to the student
        Student student = getStudent(form.getStudentId());
        // Creation
        return lessonRepository.createLesson(
                student.getTeacherId(),
                form.getStudentId(),
                form.getLocation(),
                form.getFrom(),
                form.getTo()
        );
    }

    private void checkLessonForm(LessonForm form) {
        // Same day rule
        if (!form.getFrom().toLocalDate().equals(form.getTo().toLocalDate())) {
            throw new LessonNotSameDayException();
        }
        // Order rule
        if (!form.getTo().isAfter(form.getFrom())) {
            throw new LessonTimeOrderException();
        }
    }

    @Override
    public Lesson getLesson(int lessonId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Gets the lesson
        return lessonFn.apply(lessonRepository.getById(teacherId, lessonId));
    }

    @Override
    public List<Lesson> getLessons(Integer studentId, LocalDateTime from, LocalDateTime to) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Uses the repository
        return lessonRepository
                .filter(teacherId, studentId, from, to)
                .stream()
                .map(lessonFn)
                .collect(Collectors.toList());
    }

    @Override
    public Ack deleteLesson(int lessonId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Uses the repository
        return lessonRepository.delete(teacherId, lessonId);
    }

    @Override
    public void updateLesson(int lessonId, LessonForm form) {
        checkLessonForm(form);
        // Gets the previous lesson
        Lesson lesson = getLesson(lessonId);
        // Checks the student
        if (form.getStudentId() != lesson.getStudentId()) {
            throw new LessonCannotUpdateStudentException();
        }
        // Update
        lessonRepository.updateLesson(
                lessonId,
                lesson.getTeacherId(),
                form.getLocation(),
                form.getFrom(),
                form.getTo()
        );
    }

    @Override
    public Report getReport(YearMonth period) {
        // Gets all the school reports
        List<SchoolReport> reports = getSchools()
                .stream()
                .map(school -> getSchoolReport(school.getId(), toPeriod(period), true))
                .filter(report -> BigDecimal.ZERO.compareTo(report.getHours()) != 0)
                .collect(Collectors.toList());
        // Hours
        BigDecimal hours = reports
                .stream()
                .map(SchoolReport::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Income
        Money income = reports
                .stream()
                .map(SchoolReport::getIncome)
                .reduce(null, MoneyUtils::addIncome);
        // OK
        return new Report(
                hours,
                income,
                reports
        );
    }

    @Override
    public SchoolReport getSchoolReport(int schoolId, Period period, boolean filter) {
        // Gets the school
        School school = getSchool(schoolId);
        // Gets all the students (including the disabled ones)
        List<Student> students = getStudentsForSchool(schoolId);
        // Gets the report for every student
        List<StudentReport> studentReports = students
                .stream()
                .map(student -> getStudentReport(student.getId(), period))
                .filter(report -> !filter || BigDecimal.ZERO.compareTo(report.getHours()) != 0)
                .collect(Collectors.toList());
        // Consolidation at school level
        BigDecimal hours = BigDecimal.ZERO;
        for (StudentReport studentReport : studentReports) {
            hours = hours.add(studentReport.getHours());
        }
        // Global income
        Money income = MoneyUtils.computeIncome(school, hours);
        // OK
        return new SchoolReport(
                school.getId(),
                school.getName(),
                school.getColour(),
                school.getHourlyRate(),
                hours,
                income,
                studentReports
        );
    }

    @Override
    public StudentReport getStudentReport(int studentId, Period period) {
        // Gets the student
        Student student = getStudent(studentId);
        // Gets the school
        School school = getSchool(student.getSchoolId());
        // Gets all the student lessons for the given period
        List<Lesson> lessons = getLessons(studentId, period.getFrom(), period.getTo());
        // Hours
        BigDecimal hours = BigDecimal.ZERO;
        for (Lesson lesson : lessons) {
            BigDecimal lessonDuration = lesson.getHours();
            hours = hours.add(lessonDuration);
        }
        // Income
        Money income = MoneyUtils.computeIncome(school, hours);
        // OK
        return new StudentReport(
                student.getId(),
                student.isDisabled(),
                student.getName(),
                student.getSubject(),
                hours,
                income
        );
    }

    @Override
    public LessonReport getLessonReport(int studentId, YearMonth period) {
        // Gets all the lessons for the student and getting their total number of hours
        BigDecimal totalHours = getLessons(studentId, null, null)
                .stream()
                .map(Lesson::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Gets all the student lessons for the given period
        Period p = toPeriod(period);
        List<Lesson> lessons = getLessons(studentId, p.getFrom(), p.getTo());
        // Number of hours for this period
        BigDecimal periodHours = lessons
                .stream()
                .map(Lesson::getHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // OK
        return new LessonReport(
                totalHours,
                periodHours,
                lessons
        );
    }

    private Period toPeriod(YearMonth period) {
        Period p = new Period();
        p.setFrom(period.atDay(1).atStartOfDay());
        p.setTo(period.atEndOfMonth().atTime(23, 59));
        return p;
    }

}
