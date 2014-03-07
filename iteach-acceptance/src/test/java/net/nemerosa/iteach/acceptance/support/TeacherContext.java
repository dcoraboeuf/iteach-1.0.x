package net.nemerosa.iteach.acceptance.support;

import lombok.Data;
import net.nemerosa.iteach.ui.model.UITeacher;

@Data
public class TeacherContext {

    private final UITeacher teacher;
    private final String password;

}
