package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.model.TLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;

@Component
public class LessonJdbcRepository extends AbstractJdbcRepository implements LessonRepository {

    private final RowMapper<TLesson> lessonRowMapper = (rs, rowNum) -> new TLesson(
            rs.getInt("id"),
            rs.getInt("teacherId"),
            rs.getInt("studentId"),
            SQLUtils.getLocalDateTimeFromDB(rs, "planningFrom"),
            SQLUtils.getLocalDateTimeFromDB(rs, "planningTo"),
            rs.getString("location")
    );

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
                        .addValue("planningFrom", SQLUtils.getDBValueFromLocalDateTime(start))
                        .addValue("planningTo", SQLUtils.getDBValueFromLocalDateTime(end))
        );
    }

    @Override
    public TLesson getById(int teacherId, int lessonId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.LESSON_BY_ID,
                params("teacherId", teacherId)
                        .addValue("lessonId", lessonId),
                lessonRowMapper
        );
    }
}
