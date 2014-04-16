package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.AuthenticationMode;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.dao.AccountEmailAlreadyExistsException;
import net.nemerosa.iteach.dao.AccountIdentifierAlreadyExistsException;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.dao.model.TProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Component
public class AccountJdbcRepository extends AbstractJdbcRepository implements AccountRepository {

    private final RowMapper<TAccount> accountRowMapper = (rs, rowNum) -> new TAccount(
            rs.getInt("id"),
            rs.getString("identifier"),
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
        // Checks for unicity of email
        checkEmailIsUnique(-1, email);
        // Checks for unicity of identifier
        checkIdentifierIsUnique(-1, identifier);
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

    private void checkIdentifierIsUnique(int accountId, String identifier) {
        Integer existingAccountId = getFirstItem(SQL.ACCOUNT_ID_BY_IDENTIFIER, params("identifier", identifier), Integer.class);
        if (existingAccountId != null && existingAccountId != accountId) {
            throw new AccountIdentifierAlreadyExistsException(identifier);
        }
    }

    private void checkEmailIsUnique(int accountId, String email) {
        Integer existingAccountIdWithEmail = getFirstItem(SQL.ACCOUNT_ID_BY_EMAIL, params("email", email), Integer.class);
        if (existingAccountIdWithEmail != null && existingAccountIdWithEmail != accountId) {
            throw new AccountEmailAlreadyExistsException(email);
        }
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
                        rs.getString("postalAddress"),
                        rs.getString("phone"),
                        rs.getString("vat"),
                        rs.getString("iban"),
                        rs.getString("bic")
                )
        );
    }

    @Override
    public void saveProfile(int accountId, TProfile profile) {
        getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_UPDATE_PROFILE,
                params("id", accountId)
                        .addValue("company", profile.getCompany())
                        .addValue("postalAddress", profile.getPostalAddress())
                        .addValue("phone", profile.getPhone())
                        .addValue("vat", profile.getVat())
                        .addValue("iban", profile.getIban())
                        .addValue("bic", profile.getBic())
        );
    }

    @Override
    public void updateEmail(int accountId, String email) {
        TAccount account = getById(accountId);
        // Gets the authentication mode
        AuthenticationMode mode = account.getAuthenticationMode();
        // Changes only the email for OPEN_ID or for the default admin account
        if (mode == AuthenticationMode.OPEN_ID || "admin".equals(account.getIdentifier())) {
            // Checks for unicity of email
            checkEmailIsUnique(accountId, email);
            // Update
            getNamedParameterJdbcTemplate().update(
                    SQL.ACCOUNT_CHANGE_EMAIL_ONLY,
                    params("id", accountId).addValue("email", email)
            );
        }
        // Changes both the identifier & the email for the PASSWORD
        else if (mode == AuthenticationMode.PASSWORD) {
            // Checks for unicity of email
            checkEmailIsUnique(accountId, email);
            // Checks for unicity of identifier
            checkIdentifierIsUnique(accountId, email);
            // Update
            getNamedParameterJdbcTemplate().update(
                    SQL.ACCOUNT_CHANGE_EMAIL_AND_IDENTIFIER,
                    params("id", accountId).addValue("email", email)
            );
        }
        // Not managed
        else {
            throw new IllegalStateException("Authentication mode not managed: " + mode);
        }
    }

    @Override
    public void changePassword(int accountId, String encodedPassword) {
        getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_CHANGE_PASSWORD,
                params("id", accountId).addValue("encodedPassword", encodedPassword)
        );
    }

    @Override
    public void disable(int accountId, boolean disabled) {
        getNamedParameterJdbcTemplate().update(
                SQL.ACCOUNT_DISABLE,
                params("id", accountId).addValue("disabled", disabled)
        );
    }

    @Override
    public Ack saveProfileCompanyLogo(int accountId, UntitledDocument file) {
        return Ack.one(
                getNamedParameterJdbcTemplate().update(
                        SQL.ACCOUNT_SAVE_PROFILE_COMPANY_LOGO,
                        params("id", accountId)
                                .addValue("companyLogo_type", file.getType())
                                .addValue("companyLogo_content", file.getContent())
                )
        );
    }
}
