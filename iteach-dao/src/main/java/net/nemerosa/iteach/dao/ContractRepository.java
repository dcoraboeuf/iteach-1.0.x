package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.dao.model.TContract;
import org.joda.money.Money;

import java.math.BigDecimal;
import java.util.List;

public interface ContractRepository {
    List<TContract> findBySchool(int teacherId, int schoolId);

    int create(int teacherId, int schoolId, String name, Money hourlyRate, BigDecimal vatRate);

    TContract getById(int teacherId, int contractId);

    Ack delete(int teacherId, int contractId);

    void update(int teacherId, int contractId, String name, Money hourlyRate, BigDecimal vatRate);
}
