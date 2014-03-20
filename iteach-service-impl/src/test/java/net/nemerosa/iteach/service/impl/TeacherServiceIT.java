package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.*;

public class TeacherServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    public TeacherService teacherService;

    @Test
    public void create_school() throws Exception {
        String teacherName = uid("T");
        String teacherEmail = String.format("%s@test.com", teacherName);
        // Creates a teacher
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration(teacherName, teacherEmail).getValue();
        // Creates a school for this teacher
        String schoolName = uid("S");
        School school = serviceITSupport.asTeacher(teacherId, () -> {
            int schoolId = teacherService.createSchool(
                    new SchoolForm(
                            schoolName,
                            "#FFFF00",
                            "My Contact",
                            Money.parse("EUR 45"),
                            "My Postal Address\n1000 My City\nMy Country",
                            "0123",
                            "4567",
                            "school@test.com",
                            "http://school.test.com"
                    ));
            // Gets the school back and checks its fields
            return teacherService.getSchool(schoolId);
        });
        assertNotNull(school);
        assertTrue(school.getId() > 0);
        assertEquals(schoolName, school.getName());
        assertEquals("#FFFF00", school.getColour());
        assertEquals("My Contact", school.getContact());
        assertEquals("EUR 45.00", school.getHourlyRate().toString());
        assertEquals("My Postal Address\n1000 My City\nMy Country", school.getPostalAddress());
        assertEquals("0123", school.getPhone());
        assertEquals("4567", school.getMobilePhone());
        assertEquals("school@test.com", school.getEmail());
        assertEquals("http://school.test.com", school.getWebSite());
    }

    @Test
    public void create_lesson() throws Exception {
        // Creates a student
        Student student = serviceITSupport.createStudent();
        // Creates a lesson for this student
        int lessonId = serviceITSupport.asTeacher(student.getTeacherId(), () ->
                teacherService.createLesson(new LessonForm(
                        student.getId(),
                        "The location",
                        LocalDateTime.of(2014, 3, 20, 11, 0),
                        LocalDateTime.of(2014, 3, 20, 13, 30)
                )));
        // Gets the lesson back
        Lesson lesson = serviceITSupport.asTeacher(student.getTeacherId(), () -> teacherService.getLesson(lessonId));
        // Checks
        assertNotNull(lesson);
        assertEquals(student.getId(), lesson.getStudentId());
        assertEquals("The location", lesson.getLocation());
        assertEquals(LocalDateTime.of(2014, 3, 20, 11, 0), lesson.getFrom());
        assertEquals(LocalDateTime.of(2014, 3, 20, 13, 30), lesson.getTo());
    }

}
