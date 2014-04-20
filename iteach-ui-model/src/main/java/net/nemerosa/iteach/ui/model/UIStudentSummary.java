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
    private final String subject;
    private final UISchoolSummary school;
    private final UIContract contract;
    private final boolean hasComments;

    @ConstructorProperties({"id", "disabled", "school", "name", "subject", "contract", "hasComments"})
    public UIStudentSummary(int id, boolean disabled, UISchoolSummary school, String name, String subject, UIContract contract, boolean hasComments) {
        this.id = id;
        this.disabled = disabled;
        this.school = school;
        this.subject = subject;
        this.contract = contract;
        this.href = UILink.href("api/teacher/student/%d", id);
        this.name = name;
        this.hasComments = hasComments;
    }

}
