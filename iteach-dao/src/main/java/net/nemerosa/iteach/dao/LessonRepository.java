package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.dao.model.TLesson;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface LessonRepository {

    int createLesson(int teacherId, int studentId, String location, LocalDateTime start, LocalDateTime end);

    TLesson getById(int teacherId, int lessonId);

    List<TLesson> findByPeriod(int teacherId, LocalDateTime from, LocalDateTime to);
}
