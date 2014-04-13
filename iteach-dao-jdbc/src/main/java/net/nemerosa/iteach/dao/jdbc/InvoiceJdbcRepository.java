package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.InvoiceStatus;
import net.nemerosa.iteach.common.UntitledDocument;
import net.nemerosa.iteach.dao.InvoiceRepository;
import net.nemerosa.iteach.dao.model.TInvoice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class InvoiceJdbcRepository extends AbstractJdbcRepository implements InvoiceRepository {

    public static final int MAX_ERROR_MESSAGE = 300;

    @Autowired
    public InvoiceJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int create(int teacherId, int schoolId, int year, int month, long number, String type, LocalDateTime generation) {
        return dbCreate(
                SQL.INVOICE_CREATE,
                params("teacher", teacherId)
                        .addValue("status", InvoiceStatus.CREATED.name())
                        .addValue("school", schoolId)
                        .addValue("year", year)
                        .addValue("month", month)
                        .addValue("generation", SQLUtils.getDBValueFromLocalDateTime(generation))
                        .addValue("invoiceNb", number)
                        .addValue("documentType", type)
        );
    }

    @Override
    public List<TInvoice> list(int teacherId, Integer schoolId, Integer year) {
        StringBuilder sql = new StringBuilder("SELECT ID, STATUS, ERRORMESSAGE, ERRORUUID, DOWNLOADED, SCHOOL, YEAR, MONTH, GENERATION, INVOICENB, DOCUMENTTYPE " +
                "FROM INVOICE " +
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
                (rs, rowNum) -> toInvoice(rs)
        );
    }

    private TInvoice toInvoice(ResultSet rs) throws SQLException {
        return new TInvoice(
                rs.getInt("id"),
                SQLUtils.getEnum(InvoiceStatus.class, rs, "status"),
                rs.getString("errorMessage"),
                rs.getString("errorUuid"),
                rs.getInt("school"),
                rs.getInt("year"),
                rs.getInt("month"),
                SQLUtils.getLocalDateTimeFromDB(rs, "generation"),
                rs.getLong("invoiceNb"),
                rs.getBoolean("downloaded"),
                rs.getString("documentType")
        );
    }

    @Override
    public UntitledDocument download(int teacherId, int invoiceId) {
        //noinspection Convert2Lambda
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.INVOICE_DOWNLOAD,
                params("teacherId", teacherId).addValue("invoiceId", invoiceId),
                new RowMapper<UntitledDocument>() {
                    @Override
                    public UntitledDocument mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new UntitledDocument(
                                rs.getString("documentType"),
                                rs.getBytes("document")
                        );
                    }
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

    @Override
    public void downloaded(int teacherId, int invoiceId) {
        getNamedParameterJdbcTemplate().update(
                SQL.INVOICE_DOWNLOADED,
                params("teacherId", teacherId).addValue("invoiceId", invoiceId)
        );
    }

    @Override
    public void save(int teacherId, int invoiceId, byte[] document) {
        getNamedParameterJdbcTemplate().update(
                SQL.INVOICE_SAVE,
                params("teacherId", teacherId)
                        .addValue("invoiceId", invoiceId)
                        .addValue("document", document)
                        .addValue("status", InvoiceStatus.READY.name())
        );
    }

    @Override
    public TInvoice getById(int teacherId, int invoiceId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.INVOICE_BY_ID,
                params("teacherId", teacherId)
                        .addValue("invoiceId", invoiceId),
                (rs, rowNum) -> toInvoice(rs)
        );
    }

    @Override
    public void startGeneration(int teacherId, int invoiceId) {
        getNamedParameterJdbcTemplate().update(
                SQL.INVOICE_STARTED,
                params("teacherId", teacherId)
                        .addValue("invoiceId", invoiceId)
                        .addValue("status", InvoiceStatus.GENERATING.name())
        );
    }

    @Override
    public void error(int teacherId, int invoiceId, String message, String uuid) {
        getNamedParameterJdbcTemplate().update(
                SQL.INVOICE_ERROR,
                params("teacherId", teacherId)
                        .addValue("invoiceId", invoiceId)
                        .addValue("status", InvoiceStatus.ERROR.name())
                        .addValue("message", StringUtils.left(message, MAX_ERROR_MESSAGE))
                        .addValue("uuid", uuid)
        );
    }

    @Override
    public void delete(int teacherId, int invoiceId) {
        getNamedParameterJdbcTemplate().update(
                SQL.INVOICE_DELETE,
                params("teacherId", teacherId)
                        .addValue("invoiceId", invoiceId)
        );
    }
}
