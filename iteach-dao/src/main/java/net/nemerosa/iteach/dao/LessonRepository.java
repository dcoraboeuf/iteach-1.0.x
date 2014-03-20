package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TLesson;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface LessonRepository {

    int createLesson(int teacherId, int studentId, String location, LocalDateTime start, LocalDateTime end);

    TLesson getById(int teacherId, int lessonId);
}
