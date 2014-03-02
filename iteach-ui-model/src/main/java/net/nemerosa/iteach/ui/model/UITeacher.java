package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UITeacher extends UIResource<UITeacher> {

    private final String name;
    private final String email;
    // TODO Other fields for the teacher

    public UITeacher(int id, String name, String email) {
        super(id);
        this.name = name;
        this.email = email;
    }

}
