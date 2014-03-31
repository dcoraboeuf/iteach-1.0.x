package net.nemerosa.iteach.ui.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UICommentForm {

    @NotNull
    @Size(min = 1, max = 3000)
    private final String content;

}
