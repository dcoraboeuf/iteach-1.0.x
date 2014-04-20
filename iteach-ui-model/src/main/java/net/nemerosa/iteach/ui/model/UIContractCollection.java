package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIContractCollection extends UIResourceCollection<UIContract> {

    private final String href;
    private final int schoolId;

    @ConstructorProperties({"schoolId", "resources"})
    public UIContractCollection(int schoolId, List<UIContract> resources) {
        super(resources);
        this.schoolId = schoolId;
        this.href = UILink.href("api/teacher/school/%d/contract", schoolId);
    }

}
