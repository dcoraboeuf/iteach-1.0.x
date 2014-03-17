package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.StudentNameAlreadyDefinedException;
import net.nemerosa.iteach.dao.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class StudentJdbcRepository extends AbstractJdbcRepository implements StudentRepository {

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
}
