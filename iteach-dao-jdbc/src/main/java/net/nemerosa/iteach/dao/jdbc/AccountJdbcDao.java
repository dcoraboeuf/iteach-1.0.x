package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class AccountJdbcDao extends AbstractJdbcDaoSupport implements AccountDao {

    @Autowired
    public AccountJdbcDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void createAccount(AuthenticationMode mode, String identifier, String email, String name, String encodedPassword) {
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
        getNamedParameterJdbcTemplate().update(SQL.ACCOUNT_CREATE, params);

    }

}
