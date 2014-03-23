package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.model.TLesson;

import java.time.LocalDateTime;
import java.util.List;

public interface LessonRepository {

    int createLesson(int teacherId, int studentId, String location, LocalDateTime start, LocalDateTime end);

    TLesson getById(int teacherId, int lessonId);

    List<TLesson> filter(int teacherId, Integer studentId, LocalDateTime from, LocalDateTime to);

    Ack delete(int teacherId, int lessonId);

    void updateLesson(int lessonId, int teacherId, String location, LocalDateTime from, LocalDateTime to);
}
