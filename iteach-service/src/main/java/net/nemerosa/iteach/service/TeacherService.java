package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.*;

import java.util.List;

public interface TeacherService {

    // Schools

    List<School> getSchools();

    int createSchool(SchoolForm form);

    School getSchool(int schoolId);

    void updateSchool(int schoolId, SchoolForm form);

    // Students

    int createStudent(StudentForm form);

    Student getStudent(int studentId);

    List<Student> getStudents();

    // Lessons

    int createLesson(LessonForm form);

}
