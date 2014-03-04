package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.SchoolNameAlreadyDefinedException;
import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.model.TSchool;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SchoolJdbcRepository extends AbstractJdbcRepository implements SchoolRepository {

    @Autowired
    public SchoolJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(int teacherId, String name, String colour, String contact, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite) {
        try {
            return dbCreate(
                    SQL.SCHOOL_CREATE,
                    params("teacherId", teacherId)
                            .addValue("name", name)
                            .addValue("contact", contact)
                            .addValue("colour", colour)
                            .addValue("email", email)
                            .addValue("hourlyRate", hourlyRate != null ? hourlyRate.toString() : null)
                            .addValue("postalAddress", postalAddress)
                            .addValue("phone", phone)
                            .addValue("mobilePhone", mobilePhone)
                            .addValue("webSite", webSite)
            );
        } catch (DuplicateKeyException ex) {
            throw new SchoolNameAlreadyDefinedException(teacherId, name);
        }
    }

    @Override
    public TSchool getById(int teacherId, int schoolId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.SCHOOL_BY_ID,
                params("teacherId", teacherId).addValue("schoolId", schoolId),
                new RowMapper<TSchool>() {
                    @Override
                    public TSchool mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new TSchool(
                                rs.getInt("id"),
                                rs.getInt("teacherId"),
                                rs.getString("name"),
                                rs.getString("colour"),
                                rs.getString("contact"),
                                SQLUtils.toMoney(rs, "hourlyRate"),
                                rs.getString("postalAddress"),
                                rs.getString("phone"),
                                rs.getString("mobilePhone"),
                                rs.getString("email"),
                                rs.getString("webSite")
                        );
                    }
                }
        );
    }
}
