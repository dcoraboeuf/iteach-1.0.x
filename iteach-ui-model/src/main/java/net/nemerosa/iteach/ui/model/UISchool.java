package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

import java.beans.ConstructorProperties;

@Data
@EqualsAndHashCode(callSuper = false)
public class UISchool extends UIResource<UISchool> {

    private final int id;
    private final String href;
    private final String name;
    private final String colour;
    private final String contact;
    private final Money hourlyRate;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final String webSite;

    @ConstructorProperties({"id", "name", "colour", "contact", "hourlyRate", "postalAddress", "phone", "mobilePhone", "email", "webSite"})
    public UISchool(int id, String name, String colour, String contact, Money hourlyRate, String postalAddress, String phone, String mobilePhone, String email, String webSite) {
        this.id = id;
        this.href = UILink.href("api/teacher/school/%d", id);
        this.name = name;
        this.colour = colour;
        this.contact = contact;
        this.hourlyRate = hourlyRate;
        this.postalAddress = postalAddress;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.email = email;
        this.webSite = webSite;
    }
}
