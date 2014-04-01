package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.dao.model.TComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;

@Component
public class CommentJdbcRepository extends AbstractJdbcRepository implements CommentRepository {

    @Autowired
    public CommentJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<TComment> findByEntityId(int teacherId, CommentEntity entity, int entityId) {
        return getNamedParameterJdbcTemplate().query(
                format(SQL.COMMENT_BY_ENTITY_ID, entity.name()),
                params("teacherId", teacherId)
                        .addValue("entityId", entityId),
                getRowMapper(entity)
        );
    }

    private RowMapper<TComment> getRowMapper(final CommentEntity entity) {
        return (rs, rowNum) -> new TComment(
                rs.getInt("id"),
                entity,
                rs.getInt(entity.name()),
                SQLUtils.getLocalDateTimeFromDB(rs, "creation"),
                SQLUtils.getLocalDateTimeFromDB(rs, "edition"),
                rs.getString("content")
        );
    }

    @Override
    public TComment getById(int teacherId, CommentEntity entity, int commentId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.COMMENT_BY_ID,
                params("teacherId", teacherId).addValue("commentId", commentId),
                getRowMapper(entity)
        );
    }

    @Override
    public int create(int teacherId, CommentEntity entity, int entityId, String content) {
        return dbCreate(
                format(SQL.COMMENT_POST, entity.name()),
                params("teacherId", teacherId)
                        .addValue("entityId", entityId)
                        .addValue("creation", SQLUtils.getDBValueFromLocalDateTime(LocalDateTime.now()))
                        .addValue("content", content)
        );
    }

    @Override
    public Ack delete(int teacherId, CommentEntity entity, int commentId) {
        return Ack.one(
                getNamedParameterJdbcTemplate().update(
                        SQL.COMMENT_DELETE,
                        params("teacherId", teacherId).addValue("commentId", commentId)
                )
        );
    }

    @Override
    public void update(int teacherId, CommentEntity entity, int commentId, String content) {
        getNamedParameterJdbcTemplate().update(
                SQL.COMMENT_UPDATE,
                params("teacherId", teacherId)
                        .addValue("commentId", commentId)
                        .addValue("edition", SQLUtils.getDBValueFromLocalDateTime(LocalDateTime.now()))
                        .addValue("content", content)
        );
    }
}
