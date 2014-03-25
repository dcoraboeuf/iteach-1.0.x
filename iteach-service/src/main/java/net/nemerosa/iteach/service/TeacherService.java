package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TeacherService {

    // Schools

    List<School> getSchools();

    int createSchool(SchoolForm form);

    School getSchool(int schoolId);

    Ack updateSchool(int schoolId, SchoolForm form);

    Ack deleteSchool(int schoolId);

    // Students

    int createStudent(StudentForm form);

    Student getStudent(int studentId);

    List<Student> getStudents();

    Ack disableStudent(int studentId);

    Ack enableStudent(int studentId);

    Ack deleteStudent(int studentId);

    Ack updateStudent(int studentId, StudentForm form);

    // Lessons

    int createLesson(LessonForm form);

    Lesson getLesson(int lessonId);

    List<Lesson> getLessons(Integer studentId, LocalDateTime from, LocalDateTime to);

    Ack deleteLesson(int lessonId);

    void updateLesson(int lessonId, LessonForm form);
}
