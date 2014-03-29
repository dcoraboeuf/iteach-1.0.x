package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.dao.model.TProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class AccountJdbcRepository extends AbstractJdbcRepository implements AccountRepository {

    private final RowMapper<TAccount> accountRowMapper = (rs, rowNum) -> new TAccount(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getBoolean("administrator"),
            SQLUtils.getEnum(AuthenticationMode.class, rs, "mode"),
            rs.getBoolean("verified"),
            rs.getBoolean("disabled")
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

    @Override
    public TAccount getById(int id) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.ACCOUNT_SUMMARY_BY_ID,
                params("id", id),
                accountRowMapper);
    }

    @Override
    public Stream<TAccount> findAll() {
        return getJdbcTemplate().query(
                SQL.ACCOUNT_ALL,
                accountRowMapper
        ).stream();
    }

    @Override
    public boolean checkPassword(int id, Predicate<String> check) {
        String encodedPassword = getFirstItem(
                SQL.ACCOUNT_PASSWORD,
                params("id", id),
                String.class
        );
        return encodedPassword != null && check.test(encodedPassword);
    }

    @Override
    public TAccount findUserByUsernameForOpenIDMode(String identifier) {
        try {
            return getNamedParameterJdbcTemplate().queryForObject(
                    SQL.ACCOUNT_BY_OPENID,
                    params("identifier", identifier),
                    accountRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Ack delete(int accountId) {
        return Ack.one(getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_DELETE,
                params("id", accountId)
        ));
    }

    @Override
    public TProfile getProfile(int accountId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.ACCOUNT_BY_ID,
                params("id", accountId),
                (rs, rowNum) -> new TProfile(
                        rs.getString("company"),
                        rs.getString("companyLogo"),
                        rs.getString("postalAddress"),
                        rs.getString("phone"),
                        rs.getString("vat"),
                        rs.getString("iban"),
                        rs.getString("bic"),
                        getLong(rs, "invoiceLastNb")
                )
        );
    }

    private Long getLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

    @Override
    public void saveProfile(int accountId, TProfile profile) {
        getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_UPDATE_PROFILE,
                params("id", accountId)
                        .addValue("company", profile.getCompany())
                        .addValue("companyLogo", profile.getCompanyLogo())
                        .addValue("postalAddress", profile.getPostalAddress())
                        .addValue("phone", profile.getPhone())
                        .addValue("vat", profile.getVat())
                        .addValue("iban", profile.getIban())
                        .addValue("bic", profile.getBic())
                        .addValue("invoiceLastNb", profile.getInvoiceLastNb())
        );
    }
}
