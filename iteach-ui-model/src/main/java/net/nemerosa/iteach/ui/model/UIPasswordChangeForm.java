package net.nemerosa.iteach.ui.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UIPasswordChangeForm {

    @NotNull
    @Size(min = 1)
    private final String token;
    @NotNull
    @Size(min = 1)
    private final String oldPassword;
    @NotNull
    @Size(min = 1)
    private final String newPassword;

}
