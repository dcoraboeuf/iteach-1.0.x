package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UISchoolCollection extends UIResourceCollection<UISchoolSummary> {

    private final String href = "api/teacher/school";
    private final UILink form = UILink.of("School form", "api/teacher/school/form");

    @ConstructorProperties({"resources"})
    public UISchoolCollection(List<UISchoolSummary> resources) {
        super(resources);
    }

}
