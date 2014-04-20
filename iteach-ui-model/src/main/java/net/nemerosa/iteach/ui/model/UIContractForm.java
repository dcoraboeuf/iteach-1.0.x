package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.MoneyUtils;
import org.joda.money.Money;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class UIContractForm {

    @NotNull
    @Size(min = 1, max = 80)
    private final String name;
    @NotNull
    @Size(min = 1, max = 20)
    @Pattern(regexp = "^(([A-Z]{3} )?\\d+(\\.\\d+)?)?$")
    private final String hourlyRate;
    private final BigDecimal vatRate;

    public Money toHourlyRate() {
        return MoneyUtils.fromString(hourlyRate);
    }
}
