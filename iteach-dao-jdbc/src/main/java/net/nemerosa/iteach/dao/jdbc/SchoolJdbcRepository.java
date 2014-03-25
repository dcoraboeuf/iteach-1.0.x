package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.SchoolNameAlreadyDefinedException;
import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.model.TSchool;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class SchoolJdbcRepository extends AbstractJdbcRepository implements SchoolRepository {

    private RowMapper<TSchool> schoolRowMapper = (rs, rowNum) -> new TSchool(
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
                schoolRowMapper
        );
    }

    @Override
    public List<TSchool> findAll(int teacherId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.SCHOOL_ALL,
                params("teacherId", teacherId),
                schoolRowMapper
        );
    }

    @Override
    public Ack update(int teacherId, int schoolId, String name, String colour, String contact, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite) {
        try {
            return Ack.one(getNamedParameterJdbcTemplate().update(
                    SQL.SCHOOL_UPDATE,
                    params("teacherId", teacherId)
                            .addValue("schoolId", schoolId)
                            .addValue("name", name)
                            .addValue("contact", contact)
                            .addValue("colour", colour)
                            .addValue("email", email)
                            .addValue("hourlyRate", hourlyRate != null ? hourlyRate.toString() : null)
                            .addValue("postalAddress", postalAddress)
                            .addValue("phone", phone)
                            .addValue("mobilePhone", mobilePhone)
                            .addValue("webSite", webSite)
            ));
        } catch (DuplicateKeyException ex) {
            throw new SchoolNameAlreadyDefinedException(teacherId, name);
        }
    }

    @Override
    public Ack delete(int teacherId, int schoolId) {
        return Ack.one(
                getNamedParameterJdbcTemplate().update(
                        SQL.SCHOOL_DELETE,
                        params("teacherId", teacherId).addValue("id", schoolId)
                )
        );
    }
}
