package net.nemerosa.iteach.dao.jdbc;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.ContractRepository;
import net.nemerosa.iteach.dao.model.TContract;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class ContractJdbcRepository extends AbstractJdbcRepository implements ContractRepository {

    @Autowired
    public ContractJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<TContract> findBySchool(int teacherId, int schoolId) {
        return getNamedParameterJdbcTemplate().query(
                SQL.CONTRACT_BY_SCHOOL,
                params("teacherId", teacherId).addValue("schoolId", schoolId),
                (rs, rowNum) -> mapContract(rs)
        );
    }

    private TContract mapContract(ResultSet rs) throws SQLException {
        return new TContract(
                rs.getInt("id"),
                rs.getInt("teacherId"),
                rs.getInt("schoolId"),
                rs.getString("name"),
                SQLUtils.toMoney(rs, "hourlyRate"),
                rs.getBigDecimal("vatRate")
        );
    }

    @Override
    public int create(int teacherId, int schoolId, String name, Money hourlyRate, BigDecimal vatRate) {
        return dbCreate(
                SQL.CONTRACT_CREATE,
                params("teacherId", teacherId)
                        .addValue("schoolId", schoolId)
                        .addValue("name", name)
                        .addValue("hourlyRate", hourlyRate != null ? hourlyRate.toString() : null)
                        .addValue("vatRate", vatRate)
        );
    }

    @Override
    public TContract getById(int teacherId, int contractId) {
        return getNamedParameterJdbcTemplate().queryForObject(
                SQL.CONTRACT_BY_ID,
                params("teacherId", teacherId).addValue("id", contractId),
                (rs, rowNum) -> mapContract(rs)
        );
    }

    @Override
    public Ack delete(int teacherId, int contractId) {
        return Ack.one(
                getNamedParameterJdbcTemplate().update(
                        SQL.CONTRACT_DELETE,
                        params("teacherId", teacherId).addValue("id", contractId)
                )
        );
    }

    @Override
    public void update(int teacherId, int contractId, String name, Money hourlyRate, BigDecimal vatRate) {
        getNamedParameterJdbcTemplate().update(
                SQL.CONTRACT_UPDATE,
                params("teacherId", teacherId)
                        .addValue("id", contractId)
                        .addValue("name", name)
                        .addValue("hourlyRate", hourlyRate != null ? hourlyRate.toString() : null)
                        .addValue("vatRate", vatRate)
        );
    }
}
