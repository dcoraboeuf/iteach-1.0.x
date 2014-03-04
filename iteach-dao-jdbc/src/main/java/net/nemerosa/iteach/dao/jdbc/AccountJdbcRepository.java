package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AccountJdbcRepository extends AbstractJdbcRepository implements AccountRepository {

    private final RowMapper<TAccount> accountRowMapper = (rs, rowNum) -> new TAccount(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email")
    );

    @Autowired
    public AccountJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int createAccount(AuthenticationMode mode, String identifier, String email, String name, String encodedPassword) {
        // Checks for unicity of identifier
        Integer existingAccountId = getFirstItem(SQL.ACCOUNT_ID_BY_IDENTIFIER, params("identifier", identifier), Integer.class);
        if (existingAccountId != null) {
            throw new AccountIdentifierAlreadyExistsException(identifier);
        }
        // Checks for unicity of email
        existingAccountId = getFirstItem(SQL.ACCOUNT_ID_BY_EMAIL, params("email", email), Integer.class);
        if (existingAccountId != null) {
            throw new AccountEmailAlreadyExistsException(email);
        }
        // Parameters
        MapSqlParameterSource params = params("email", email);
        params.addValue("name", name);
        // Mode
        params.addValue("mode", mode.name());
        params.addValue("identifier", identifier);
        params.addValue("password", encodedPassword);
        // Insert the user
        return dbCreate(SQL.ACCOUNT_CREATE, params);

    }

    @Override
    public TAccount findByEmail(String email) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.ACCOUNT_SUMMARY_BY_EMAIL,
                params("email", email),
                accountRowMapper);
    }

    @Override
    public Ack accountVerified(int id) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_SET_VERIFIED,
                params("id", id)));
    }


    @Override
    public TAccount findUserByUsernameForPasswordMode(String email) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    SQL.ACCOUNT_BY_PASSWORD,
                    params("identifier", email),
                    accountRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
