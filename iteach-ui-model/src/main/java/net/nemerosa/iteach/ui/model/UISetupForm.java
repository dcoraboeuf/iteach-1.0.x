package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@Data
public class UISetupForm {

    @NonNull
    @Size(min = 1, max = 120)
    @Email
    private final String email;

    @NonNull
    @Size(min = 1, max = 40)
    private final String password;

    @Size(min = 1, max = 40)
    private final String passwordChange;

}
