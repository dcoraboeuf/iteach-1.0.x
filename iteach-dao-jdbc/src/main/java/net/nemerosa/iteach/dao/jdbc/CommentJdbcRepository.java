package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.dao.model.TComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class CommentJdbcRepository extends AbstractJdbcRepository implements CommentRepository {

    @Autowired
    public CommentJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<TComment> findByEntityId(int teacherId, CommentEntity entity, int entityId) {
        return getNamedParameterJdbcTemplate().query(
                String.format(SQL.COMMENT_BY_ENTITY_ID, entity.name()),
                params("teacherId", teacherId)
                        .addValue("entityId", entityId),
                new RowMapper<TComment>() {
                    @Override
                    public TComment mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new TComment(
                                rs.getInt("id"),
                                entity,
                                rs.getInt(entity.name()),
                                SQLUtils.getLocalDateTimeFromDB(rs, "creation"),
                                SQLUtils.getLocalDateTimeFromDB(rs, "edition"),
                                rs.getString("content")
                        );
                    }
                }
        );
    }
}
