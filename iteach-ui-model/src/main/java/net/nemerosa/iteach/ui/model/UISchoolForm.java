package net.nemerosa.iteach.ui.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import org.joda.money.Money;

import javax.validation.constraints.*;

@Data
public class UISchoolForm {

    @NotNull
    @Size(min=1, max = 80)
    private final String name;
    @Pattern(regexp = "#[0-9A-Fa-f]{6}")
    private final String colour;
    @Size(min=0, max = 80)
    private final String contact;
    @Size(min=0, max = 20)
    @Pattern(regexp = "^([A-Z]{3} )?\\d+(\\.\\d+)?$")
    private final String hourlyRate;
    @Size(min=0, max = 200)
    private final String postalAddress;
    @Size(min=0, max = 40)
    private final String phone;
    @Size(min=0, max = 40)
    private final String mobilePhone;
    @Size(min=0, max = 120)
    @Email
    private final String email;
    @Size(min=0, max = 200)
    @URL
    private final String webSite;

    /**
     * CONTACT       VARCHAR(80)  NULL,
     * COLOUR        CHAR(7)      NOT NULL,
     * EMAIL         VARCHAR(120) NULL,
     * HOURLYRATE    VARCHAR(20)  NULL,
     * POSTALADDRESS VARCHAR(200) NULL,
     * PHONE         VARCHAR(40)  NULL,
     * MOBILEPHONE   VARCHAR(40)  NULL,
     * WEBSITE       VARCHAR(200) NULL,
     */

    public Money toHourlyRate() {
        return StringUtils.isNotBlank(hourlyRate)
                ? Money.parse(hourlyRate)
                : null;
    }
}
