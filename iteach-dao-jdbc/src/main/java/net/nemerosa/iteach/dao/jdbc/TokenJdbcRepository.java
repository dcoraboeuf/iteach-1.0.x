package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.dao.TokenRepository;
import net.nemerosa.iteach.dao.model.TToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class TokenJdbcRepository extends AbstractJdbcRepository implements TokenRepository {

    @Autowired
    public TokenJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void saveToken(TokenType type, String key, String token) {
        // Creation date
        Timestamp creation = SQLUtils.toTimestamp(SQLUtils.now());
        // Saves the token
        getNamedParameterJdbcTemplate().update(
                SQL.TOKEN_SAVE,
                new MapSqlParameterSource()
                        .addValue("token", token)
                        .addValue("tokentype", type.name())
                        .addValue("tokenkey", key)
                        .addValue("creation", creation));
    }

    @Override
    public TToken findByTokenAndType(String token, TokenType tokenType) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.TOKEN_BY_TOKEN_AND_TYPE,
                params("token", token).addValue("tokentype", tokenType.name()),
                new RowMapper<TToken>() {

                    @Override
                    public TToken mapRow(ResultSet rs, int index) throws SQLException {
                        return new TToken(
                                rs.getString("token"),
                                SQLUtils.getEnum(TokenType.class, rs, "tokenType"),
                                rs.getString("tokenkey"),
                                SQLUtils.fromTimestamp(rs, "creation")
                        );
                    }

                });
    }

    @Override
    public void deleteToken(TokenType tokenType, String key) {
        getNamedParameterJdbcTemplate().update(
                SQL.TOKEN_DELETE,
                new MapSqlParameterSource()
                        .addValue("tokentype", tokenType.name())
                        .addValue("tokenkey", key));
    }
}
