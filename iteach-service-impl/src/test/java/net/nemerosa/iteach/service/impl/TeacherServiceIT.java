package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.Period;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.InvoiceService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.*;

public class TeacherServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private InvoiceService invoiceService;

    @Test
    public void getSchoolReport() throws Exception {
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        int schoolId = serviceITSupport.createSchool(teacherId);
        Student student = serviceITSupport.createStudent(teacherId, schoolId);
        // 5 hours for this student
        serviceITSupport.asTeacher(teacherId, () -> {
            teacherService.createLesson(new LessonForm(
                    student.getId(),
                    "The location",
                    LocalDateTime.of(2014, 3, 20, 11, 0),
                    LocalDateTime.of(2014, 3, 20, 13, 30)));
            teacherService.createLesson(new LessonForm(
                    student.getId(),
                    "The location",
                    LocalDateTime.of(2014, 3, 21, 11, 0),
                    LocalDateTime.of(2014, 3, 21, 13, 30)));
            return null;
        });
        // Gets the school report
        SchoolReport report = serviceITSupport.asTeacher(teacherId, () -> teacherService.getSchoolReport(
                schoolId,
                new Period(),
                false
        ));
        // Checks
        assertEquals(new BigDecimal("5.00"), report.getHours()); // 5
        assertEquals(Money.of(CurrencyUnit.EUR, 338.8), report.getIncomeTotal()); // 56 * 5 + 21% VAT
    }

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
                            "http://school.test.com",
                            "BE0123456789",
                            new BigDecimal("21")
                    )
            );
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
    public void update_student() throws Exception {
        // Creates a student
        Student student = serviceITSupport.createStudent();
        // Updates the student
        Ack ack = serviceITSupport.asTeacher(student.getTeacherId(), () ->
                        teacherService.updateStudent(student.getId(),
                                new StudentForm(
                                        student.getSchoolId(),
                                        null,
                                        student.getName(),
                                        student.getSubject(),
                                        student.getPostalAddress(),
                                        "012345",
                                        student.getMobilePhone(),
                                        student.getEmail()
                                )
                        )
        );
        assertTrue(ack.isSuccess());
        // Gets the new student
        Student newStudent = serviceITSupport.asTeacher(student.getTeacherId(), () ->
                        teacherService.getStudent(student.getId())
        );
        assertEquals(student.getId(), newStudent.getId());
        assertEquals("012345", newStudent.getPhone());
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

    @Test
    public void next_invoice_number_when_none() throws Exception {
        // Creates a teacher
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration();
        // Gets next version number == 1
        assertEquals(
                1L,
                (long) serviceITSupport.asTeacher(teacherId, invoiceService::getNextInvoiceNumber)
        );
    }

}
