package net.nemerosa.iteach.ui.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Builder;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UIStudentForm {

    @Min(1)
    private final int schoolId;
    @NotNull
    @Size(min = 1, max = 80)
    private final String name;
    @Size(min = 0, max = 120)
    private final String subject;
    @Size(min = 0, max = 200)
    private final String postalAddress;
    @Size(min = 0, max = 40)
    private final String phone;
    @Size(min = 0, max = 40)
    private final String mobilePhone;
    @Size(min = 0, max = 120)
    @Email
    private final String email;

}
