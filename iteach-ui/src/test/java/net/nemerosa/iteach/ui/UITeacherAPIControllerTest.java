package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.Student;
import net.nemerosa.iteach.ui.model.UIStudentCollection;
import net.nemerosa.iteach.ui.model.UIStudentSummary;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UITeacherAPIControllerTest {

    private UITeacherAPIController controller;

    @Before
    public void before() {
        TeacherService teacherService = mock(TeacherService.class);
        when(teacherService.getSchool(10)).thenReturn(
                new School(10, 1, "The School", "#FFFFFF", "", null, "", "", "", "", "", "", null)
        );
        when(teacherService.getStudents()).thenReturn(
                Arrays.asList(
                        new Student(100, 1, 10, false, "A Name", "", "", "", "", ""),
                        new Student(101, 1, 10, true, "B Name", "", "", "", "", "")
                )
        );
        controller = new UITeacherAPIController(teacherService);
    }

    @Test
    public void unfilteredStudents() {
        UIStudentCollection students = controller.getStudents(Locale.ENGLISH, false);
        assertNotNull(students);
        List<UIStudentSummary> resources = students.getResources();
        assertNotNull(resources);
        assertEquals(2, resources.size());
        assertEquals("A Name", resources.get(0).getName());
        assertFalse(resources.get(0).isDisabled());
        assertEquals("B Name", resources.get(1).getName());
        assertTrue(resources.get(1).isDisabled());
    }

    @Test
    public void filteredStudents() {
        UIStudentCollection students = controller.getStudents(Locale.ENGLISH, true);
        assertNotNull(students);
        List<UIStudentSummary> resources = students.getResources();
        assertNotNull(resources);
        assertEquals(1, resources.size());
        assertEquals("A Name", resources.get(0).getName());
        assertFalse(resources.get(0).isDisabled());
    }

}
