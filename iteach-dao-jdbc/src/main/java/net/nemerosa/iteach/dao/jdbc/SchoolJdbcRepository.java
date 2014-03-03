package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.SchoolNameAlreadyDefinedException;
import net.nemerosa.iteach.dao.SchoolRepository;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class SchoolJdbcRepository extends AbstractJdbcRepository implements SchoolRepository {

    @Autowired
    public SchoolJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(int teacherId, String name, String contact, String colour, String email, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String webSite) {
        try {
            return dbCreate(
                    "INSERT INTO SCHOOL (TEACHERID, NAME, CONTACT, COLOUR, EMAIL, HOURLYRATE, POSTALADDRESS, PHONE, MOBILEPHONE, WEBSITE) VALUES (:teacherId, :name, :contact, :colour, :email, :hourlyRate, :postalAddress, :phone, :mobilePhone, :webSite)",
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
}
