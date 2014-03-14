package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.nemerosa.iteach.common.AuthenticationMode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UITeacher extends UIResource {

    private final int id;
    private final String href = "api/account/state";
    private final String name;
    private final String email;
    private final boolean administrator;
    private final AuthenticationMode authenticationMode;
    private final UILink schools = UILink.of("List of schools", "api/teacher/school");
    private final UILink students = UILink.of("List of students", "api/teacher/student");

}
