package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;

@EqualsAndHashCode(callSuper = false)
@Data
public class UIStudentSummary extends UIResource {

    private final int id;
    private final String href;
    private final boolean disabled;
    private final String name;
    private final UISchoolSummary school;

    @ConstructorProperties({"id", "disabled", "school", "name"})
    public UIStudentSummary(int id, boolean disabled, UISchoolSummary school, String name) {
        this.id = id;
        this.disabled = disabled;
        this.school = school;
        this.href = UILink.href("api/teacher/student/%d", id);
        this.name = name;
    }

}
