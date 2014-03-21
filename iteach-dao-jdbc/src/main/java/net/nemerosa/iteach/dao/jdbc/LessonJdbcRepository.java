package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.model.TLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static net.nemerosa.iteach.dao.jdbc.SQLUtils.getDBValueFromLocalDateTime;
import static net.nemerosa.iteach.dao.jdbc.SQLUtils.getLocalDateTimeFromDB;

@Component
public class LessonJdbcRepository extends AbstractJdbcRepository implements LessonRepository {

    private final RowMapper<TLesson> lessonRowMapper = (rs, rowNum) -> new TLesson(
            rs.getInt("id"),
            rs.getInt("teacherId"),
            rs.getInt("studentId"),
            getLocalDateTimeFromDB(rs, "planningFrom"),
            getLocalDateTimeFromDB(rs, "planningTo"),
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
                        .addValue("planningFrom", getDBValueFromLocalDateTime(start))
                        .addValue("planningTo", getDBValueFromLocalDateTime(end))
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

    @Override
    public List<TLesson> findByPeriod(int teacherId, LocalDateTime from, LocalDateTime to) {
        return getNamedParameterJdbcTemplate().query(
                SQL.LESSON_BY_PERIOD,
                params("teacherId", teacherId)
                        .addValue("from", getDBValueFromLocalDateTime(from))
                        .addValue("to", getDBValueFromLocalDateTime(to)),
                lessonRowMapper
        );
    }
}
