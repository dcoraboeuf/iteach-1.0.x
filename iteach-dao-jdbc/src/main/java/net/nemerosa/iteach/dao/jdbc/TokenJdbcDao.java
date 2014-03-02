package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.dao.TokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Timestamp;

@Component
public class TokenJdbcDao extends AbstractJdbcDaoSupport implements TokenDao {

    @Autowired
    public TokenJdbcDao(DataSource dataSource) {
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
}
