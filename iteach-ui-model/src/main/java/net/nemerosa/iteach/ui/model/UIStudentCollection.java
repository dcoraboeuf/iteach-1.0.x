package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIStudentCollection extends UIResourceCollection<UIStudentSummary> {

    // TODO This link is a variable
    private final String href = "api/teacher/student";

    @ConstructorProperties({"resources"})
    public UIStudentCollection(List<UIStudentSummary> resources) {
        super(resources);
    }

}
