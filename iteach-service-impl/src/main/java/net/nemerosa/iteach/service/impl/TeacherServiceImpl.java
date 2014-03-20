package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.StudentRepository;
import net.nemerosa.iteach.dao.model.TSchool;
import net.nemerosa.iteach.dao.model.TStudent;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            t.getWebSite()
    );
    private final Function<? super TStudent, ? extends Student> studentFn = t -> new Student(
            t.getId(),
            t.getTeacherId(),
            t.getSchoolId(),
            t.getName(),
            t.getSubject(),
            t.getPostalAddress(),
            t.getPhone(),
            t.getMobilePhone(),
            t.getEmail()
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
                form.getWebSite()
        );
    }

    @Override
    public void updateSchool(int schoolId, SchoolForm form) {
        // Checks the teacher access
        School school = getSchool(schoolId);
        // Updates
        schoolRepository.update(
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
                form.getWebSite()
        );
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
        return studentRepository.findAll(teacherId).stream().map(studentFn).collect(Collectors.toList());
    }

    @Override
    public int createLesson(LessonForm form) {
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

}
