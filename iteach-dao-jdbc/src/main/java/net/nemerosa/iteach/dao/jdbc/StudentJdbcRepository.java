package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.StudentNameAlreadyDefinedException;
import net.nemerosa.iteach.dao.StudentRepository;
import net.nemerosa.iteach.dao.model.TStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class StudentJdbcRepository extends AbstractJdbcRepository implements StudentRepository {

    private RowMapper<TStudent> studentRowMapper = (rs, rowNum) -> new TStudent(
            rs.getInt("id"),
            rs.getInt("teacherId"),
            rs.getInt("schoolId"),
            SQLUtils.getInteger(rs, "contractId"),
            rs.getBoolean("disabled"),
            rs.getString("name"),
            rs.getString("subject"),
            rs.getString("postalAddress"),
            rs.getString("phone"),
            rs.getString("mobilePhone"),
            rs.getString("email")
    );

    @Autowired
    public StudentJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(int teacherId, int schoolId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email) {
        try {
            return dbCreate(
                    SQL.STUDENT_CREATE,
                    params("teacherId", teacherId)
                            .addValue("schoolId", schoolId)
                            .addValue("name", name)
                            .addValue("subject", subject)
                            .addValue("email", email)
                            .addValue("postalAddress", postalAddress)
                            .addValue("phone", phone)
                            .addValue("mobilePhone", mobilePhone)
            );
        } catch (DuplicateKeyException ex) {
            throw new StudentNameAlreadyDefinedException(teacherId, name);
        }
    }

    @Override
    public TStudent getById(int teacherId, int studentId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.STUDENT_BY_ID,
                params("teacherId", teacherId).addValue("studentId", studentId),
                studentRowMapper
        );
    }

    @Override
    public List<TStudent> findAll(int teacherId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.STUDENT_ALL,
                params("teacherId", teacherId),
                studentRowMapper
        );
    }

    @Override
    public void disable(int teacherId, int studentId, boolean disabled) {
        getNamedParameterJdbcTemplate().update(
                SQL.STUDENT_DISABLE,
                params("teacherId", teacherId)
                        .addValue("id", studentId)
                        .addValue("disabled", disabled)
        );
    }

    @Override
    public Ack delete(int teacherId, int studentId) {
        return Ack.one(
                getNamedParameterJdbcTemplate().update(
                        SQL.STUDENT_DELETE,
                        params("teacherId", teacherId)
                                .addValue("id", studentId)
                )
        );
    }

    @Override
    public Ack update(int teacherId, int studentId, int schoolId, String name, String subject, String postalAddress, String phone, String mobilePhone, String email) {
        try {
            return Ack.one(getNamedParameterJdbcTemplate().update(
                    SQL.STUDENT_UPDATE,
                    params("teacherId", teacherId)
                            .addValue("studentId", studentId)
                            .addValue("schoolId", schoolId)
                            .addValue("name", name)
                            .addValue("subject", subject)
                            .addValue("email", email)
                            .addValue("postalAddress", postalAddress)
                            .addValue("phone", phone)
                            .addValue("mobilePhone", mobilePhone)
            ));
        } catch (DuplicateKeyException ex) {
            throw new StudentNameAlreadyDefinedException(teacherId, name);
        }
    }

    @Override
    public List<TStudent> findBySchool(int teacherId, int schoolId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.STUDENT_BY_SCHOOL,
                params("teacherId", teacherId).addValue("schoolId", schoolId),
                studentRowMapper
        );
    }
}
