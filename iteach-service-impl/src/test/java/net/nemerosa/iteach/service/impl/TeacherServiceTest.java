package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.StudentRepository;
import net.nemerosa.iteach.dao.model.TStudent;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.LessonForm;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeacherServiceTest {

    public static final int TEACHERID = 10;
    public static final int STUDENT_ID = 100;
    public static final int SCHOOL_ID = 50;
    private TeacherService service;
    private SchoolRepository schoolRepository;
    private StudentRepository studentRepository;
    private LessonRepository lessonRepository;
    private SecurityUtils securityUtils;

    @Before
    public void init() {
        schoolRepository = mock(SchoolRepository.class);
        studentRepository = mock(StudentRepository.class);
        lessonRepository = mock(LessonRepository.class);
        securityUtils = mock(SecurityUtils.class);
        service = new TeacherServiceImpl(
                schoolRepository,
                studentRepository,
                lessonRepository,
                securityUtils,
                accountService);
        when(securityUtils.checkTeacher()).thenReturn(TEACHERID);
        when(studentRepository.getById(TEACHERID, STUDENT_ID)).thenReturn(new TStudent(STUDENT_ID, TEACHERID, SCHOOL_ID, false, "A student", "", "", "", "", ""));
    }

    @Test
    public void create_lesson_ok() {
        LocalDateTime from = LocalDateTime.of(2014, 3, 31, 11, 30);
        LocalDateTime to = LocalDateTime.of(2014, 3, 31, 12, 30);
        when(lessonRepository.createLesson(TEACHERID, STUDENT_ID, "Some location", from, to)).thenReturn(1000);
        int id = service.createLesson(
                new LessonForm(STUDENT_ID, "Some location",
                        from,
                        to
                )
        );
        assertEquals(1000, id);
    }

    @Test(expected = LessonTimeOrderException.class)
    public void create_lesson_nok_order() {
        LocalDateTime from = LocalDateTime.of(2014, 3, 31, 12, 30);
        LocalDateTime to = LocalDateTime.of(2014, 3, 31, 11, 30);
        when(lessonRepository.createLesson(TEACHERID, STUDENT_ID, "Some location", from, to)).thenReturn(1000);
        service.createLesson(
                new LessonForm(STUDENT_ID, "Some location",
                        from,
                        to
                )
        );
    }

    @Test(expected = LessonTimeOrderException.class)
    public void create_lesson_nok_same_time() {
        LocalDateTime from = LocalDateTime.of(2014, 3, 31, 11, 30);
        LocalDateTime to = LocalDateTime.of(2014, 3, 31, 11, 30);
        when(lessonRepository.createLesson(TEACHERID, STUDENT_ID, "Some location", from, to)).thenReturn(1000);
        service.createLesson(
                new LessonForm(STUDENT_ID, "Some location",
                        from,
                        to
                )
        );
    }

    @Test(expected = LessonNotSameDayException.class)
    public void create_lesson_nok_not_same_day() {
        LocalDateTime from = LocalDateTime.of(2014, 3, 30, 11, 30);
        LocalDateTime to = LocalDateTime.of(2014, 3, 31, 12, 30);
        when(lessonRepository.createLesson(TEACHERID, STUDENT_ID, "Some location", from, to)).thenReturn(1000);
        service.createLesson(
                new LessonForm(STUDENT_ID, "Some location",
                        from,
                        to
                )
        );
    }

}
