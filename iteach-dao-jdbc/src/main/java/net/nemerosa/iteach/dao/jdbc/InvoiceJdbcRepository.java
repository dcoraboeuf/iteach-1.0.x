package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.dao.InvoiceCannotReadException;
import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.dao.model.TInvoice;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;

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

    @Override
    public List<TInvoice> list(int teacherId, Integer schoolId, Integer year) {
        StringBuilder sql = new StringBuilder("SELECT ID, SCHOOL, YEAR, MONTH, GENERATION, INVOICENB, DOCUMENTTYPE " +
                "FROM INVOICE" +
                "WHERE TEACHER = :teacher");
        MapSqlParameterSource params = params("teacher", teacherId);
        // School
        if (schoolId != null) {
            sql.append(" AND SCHOOL = :school");
            params.addValue("school", schoolId);
        }
        // Year
        if (year != null) {
            sql.append(" AND YEAR = :year");
            params.addValue("year", year);
        }
        // Ordering
        sql.append(" ORDER BY INVOICENB DESC");
        // Query
        return getNamedParameterJdbcTemplate().query(
                sql.toString(),
                params,
                (rs, rowNum) -> new TInvoice(
                        rs.getInt("id"),
                        rs.getInt("school"),
                        rs.getInt("year"),
                        rs.getInt("month"),
                        SQLUtils.getLocalDateTimeFromDB(rs, "generation"),
                        rs.getLong("invoiceNb"),
                        rs.getString("documentType")
                )
        );
    }

    @Override
    public void download(int teacherId, int invoiceId, OutputStream out) {
        getNamedParameterJdbcTemplate().query(
                SQL.INVOICE_DOWNLOAD,
                params("teacherId", teacherId).addValue("invoiceId", invoiceId),
                rs -> {
                    try (InputStream in = rs.getBinaryStream("document")) {
                        IOUtils.copy(in, out);
                    } catch (IOException e) {
                        throw new InvoiceCannotReadException(e);
                    }
                    return null;
                }
        );
    }

    @Override
    public Long getLastInvoiceNumber(int teacherId) {
        return getFirstItem(
                SQL.INVOICE_LAST_NB,
                params("teacherId", teacherId),
                Long.class
        );
    }
}
