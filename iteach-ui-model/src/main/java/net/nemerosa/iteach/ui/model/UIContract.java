package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIContract extends UIResource {

    private final int id;
    private final UISchoolSummary school;
    private final String href;
    private final String name;
    private final Money hourlyRate;
    private final BigDecimal vatRate;

    @ConstructorProperties({"id", "school", "name", "hourlyRate", "vatRate"})
    public UIContract(int id, UISchoolSummary school, String name, Money hourlyRate, BigDecimal vatRate) {
        this.id = id;
        this.school = school;
        this.href = UILink.href("api/teacher/contract/%d", id);
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.vatRate = vatRate;
    }
}
