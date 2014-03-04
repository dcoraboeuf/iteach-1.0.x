package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TeacherServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    public TeacherService teacherService;

    @Test
    public void create_school() {
        String teacherName = uid("T");
        String teacherEmail = String.format("%s@test.com", teacherName);
        // Creates a teacher
        int teacherId = serviceITSupport.createTeacherAndCompleteRegistration(teacherName, teacherEmail).getValue();
        // Creates a school for this teacher
        String schoolName = uid("S");
        int schoolId = teacherService.createSchool(
                teacherId,
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
        School school = teacherService.getSchool(teacherId, schoolId);
        assertNotNull(school);
        assertEquals(schoolId, school.getId());
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

    @Test(expected = AccessDeniedException.class)
    public void create_school_not_authorized() {
        // Creates a teacher
        ID id1 = serviceITSupport.createTeacherAndCompleteRegistration(uid("T"), String.format("%s@test.com", uid("T")));
        ID id2 = serviceITSupport.createTeacherAndCompleteRegistration(uid("T"), String.format("%s@test.com", uid("T")));
        // Trying to create a school for teacher2 as teacher1
        // TODO Sets the security context for teacher 1
        teacherService.createSchool(id2.getValue(), new SchoolForm(
                uid("S"),
                "#FFFF00",
                "",
                Money.parse("EUR 45"),
                "",
                "",
                "",
                "",
                ""
        ));
    }

}
