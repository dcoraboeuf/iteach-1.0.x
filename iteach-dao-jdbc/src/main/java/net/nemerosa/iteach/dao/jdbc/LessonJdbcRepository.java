package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class LessonJdbcRepository extends AbstractJdbcRepository implements LessonRepository {

    @Autowired
    public LessonJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int createLesson(int teacherId, int studentId, String location, LocalDateTime start, LocalDateTime end) {
        return dbCreate(
                SQL.LESSON_CREATE,
                params("teacherId", teacherId)
                        .addValue("studentId", studentId)
                        .addValue("location", location)
                        .addValue("planningFrom", new Date(Instant.from(start).toEpochMilli()))
                        .addValue("planningTo", new Date(Instant.from(end).toEpochMilli()))
        );
    }
}
