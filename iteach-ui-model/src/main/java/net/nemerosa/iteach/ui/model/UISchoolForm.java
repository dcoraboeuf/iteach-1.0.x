package net.nemerosa.iteach.ui.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.joda.money.Money;

@Data
public class UISchoolForm {

    private final String name;
    private final String colour;
    private final String contact;
    private final String hourlyRate;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final String webSite;

    public Money toHourlyRate() {
        return StringUtils.isNotBlank(hourlyRate)
                ? Money.parse(hourlyRate)
                : null;
    }
}
