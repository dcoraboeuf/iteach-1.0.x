package net.nemerosa.iteach.dao;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface LessonRepository {

    int createLesson(int teacherId, int studentId, String location, LocalDateTime start, LocalDateTime end);

}
