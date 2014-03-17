package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIStudent extends UIResource {

    private final int id;
    private final String href;
    private final String name;
    private final String subject;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final UISchoolSummary school;

    @ConstructorProperties({"id", "school", "name", "subject", "postalAddress", "phone", "mobilePhone", "email"})
    public UIStudent(int id, UISchoolSummary school, String name, String subject, String postalAddress, String phone, String mobilePhone, String email) {
        this.id = id;
        this.school = school;
        this.href = UILink.href("api/teacher/student/%d", id);
        this.name = name;
        this.subject = subject;
        this.postalAddress = postalAddress;
        this.phone = phone;
        this.mobilePhone = mobilePhone;
        this.email = email;
    }
}
