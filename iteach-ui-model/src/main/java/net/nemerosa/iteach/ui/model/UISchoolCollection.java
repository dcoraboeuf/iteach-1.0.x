package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UISchoolCollection extends UIResourceCollection<UISchoolSummary> {

    private final String href = "api/teacher/school";

    @ConstructorProperties({"resources"})
    public UISchoolCollection(List<UISchoolSummary> resources) {
        super(resources);
    }

}
