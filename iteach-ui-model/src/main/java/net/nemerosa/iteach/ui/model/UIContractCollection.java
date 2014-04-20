package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIContractCollection extends UIResourceCollection<UISchoolSummary> {

    private final String href;
    private final int schoolId;

    @ConstructorProperties({"schoolId", "resources"})
    public UIContractCollection(int schoolId, List<UISchoolSummary> resources) {
        super(resources);
        this.schoolId = schoolId;
        this.href = UILink.href("api/teacher/school/%d/contract", schoolId);
    }

}
