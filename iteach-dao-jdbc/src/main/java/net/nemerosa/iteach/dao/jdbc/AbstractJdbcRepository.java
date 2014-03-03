package net.nemerosa.iteach.dao.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

public abstract class AbstractJdbcRepository extends NamedParameterJdbcDaoSupport {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected AbstractJdbcRepository(DataSource dataSource) {
        setDataSource(dataSource);
    }

    protected MapSqlParameterSource params(String name, Object value) {
        return new MapSqlParameterSource(name, value);
    }

    protected String jsonToDB(JsonNode data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new JsonWritingException(e);
        }
    }

    protected JsonNode dbToJson(String data) {
        try {
            return objectMapper.readTree(data);
        } catch (IOException e) {
            throw new JsonReadingException(e);
        }
    }

    protected int dbCreate(String sql, MapSqlParameterSource params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sql, params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    protected <T> T getFirstItem(String sql, MapSqlParameterSource criteria, Class<T> type) {
        List<T> items = getNamedParameterJdbcTemplate().queryForList(sql, criteria, type);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }
}
