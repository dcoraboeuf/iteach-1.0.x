package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.LessonRepository;
import net.nemerosa.iteach.dao.model.TLesson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
    public List<TLesson> filter(int teacherId, Integer studentId, LocalDateTime from, LocalDateTime to) {
        StringBuilder sql = new StringBuilder("SELECT * FROM LESSON WHERE TEACHERID = :teacherId");
        MapSqlParameterSource params = params("teacherId", teacherId);
        if (studentId != null) {
            sql.append(" AND STUDENTID = :studentId");
            params.addValue("studentId", studentId);
        }
        if (from != null) {
            sql.append(" AND PLANNINGFROM >= :from");
            params.addValue("from", getDBValueFromLocalDateTime(from));
        }
        if (to != null) {
            sql.append(" AND PLANNINGTO <= :to");
            params.addValue("to", getDBValueFromLocalDateTime(to));
        }
        sql.append(" ORDER BY PLANNINGFROM");
        return getNamedParameterJdbcTemplate().query(
                sql.toString(),
                params,
                lessonRowMapper
        );
    }

    @Override
    public Ack delete(int teacherId, int lessonId) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.LESSON_DELETE,
                params("teacherId", teacherId).addValue("lessonId", lessonId)
        ));
    }

    @Override
    public void updateLesson(int lessonId, int teacherId, String location, LocalDateTime from, LocalDateTime to) {
        getNamedParameterJdbcTemplate().update(
                SQL.LESSON_UPDATE,
                params("teacherId", teacherId)
                        .addValue("lessonId", lessonId)
                        .addValue("location", location)
                        .addValue("planningFrom", getDBValueFromLocalDateTime(from))
                        .addValue("planningTo", getDBValueFromLocalDateTime(to))
        );
    }

}
