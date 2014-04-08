package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

@Component
public class InvoiceJdbcRepository extends AbstractJdbcRepository implements InvoiceRepository {

    @Autowired
    public InvoiceJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int save(int teacherId, int schoolId, int year, Month month, long number, String type, byte[] document) {
        return dbCreate(
                "INSERT INTO (TEACHER, SCHOOL, YEAR, MONTH, GENERATION, INVOICENB, DOCUMENTTYPE, DOCUMENT) " +
                        "VALUES (:teacher, :school, :year, :month, :generation, :invoiceNb, :documentType, :document)",
                params("teacher", teacherId)
                        .addValue("school", schoolId)
                        .addValue("year", year)
                        .addValue("month", month)
                        .addValue("generation", SQLUtils.getDBValueFromLocalDateTime(LocalDateTime.now(ZoneOffset.UTC)))
                        .addValue("invoiceNb", number)
                        .addValue("documentType", type)
                        .addValue("document", document)
        );
    }
}
