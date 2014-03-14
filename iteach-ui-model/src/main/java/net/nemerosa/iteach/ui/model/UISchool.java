package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchool extends UIResource<UISchool> {

    private final int id;
    // TODO Teacher resource stub?
    private final String name;
    private final String colour;
    private final String contact;
    private final Money hourlyRate;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final String webSite;

    @Override
    public String getHref() {
        return UILink.href("api/teacher/school/%d", id);
    }
}
