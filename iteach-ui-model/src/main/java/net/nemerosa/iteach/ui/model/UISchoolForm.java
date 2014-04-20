package net.nemerosa.iteach.ui.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import net.nemerosa.iteach.common.MoneyUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import org.joda.money.Money;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UISchoolForm {

    @NotNull
    @Size(min = 1, max = 80)
    private final String name;
    @Pattern(regexp = "#[0-9A-Fa-f]{6}")
    private final String colour;
    @Size(min = 0, max = 80)
    private final String contact;
    @Size(min = 0, max = 20)
    @Pattern(regexp = "^(([A-Z]{3} )?\\d+(\\.\\d+)?)?$")
    private final String hourlyRate;
    @Size(min = 0, max = 200)
    private final String postalAddress;
    @Size(min = 0, max = 40)
    private final String phone;
    @Size(min = 0, max = 40)
    private final String mobilePhone;
    @Size(min = 0, max = 120)
    @Email
    private final String email;
    @Size(min = 0, max = 200)
    @URL
    private final String webSite;
    @Size(min = 0, max = 40)
    private final String vat;
    private final BigDecimal vatRate;

    public Money toHourlyRate() {
        return MoneyUtils.fromString(hourlyRate);
    }
}
