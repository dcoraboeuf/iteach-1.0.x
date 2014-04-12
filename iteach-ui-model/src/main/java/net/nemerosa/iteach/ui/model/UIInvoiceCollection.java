package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class UIInvoiceCollection extends UIResourceCollection<UIInvoiceInfo> {

    private final String href;
    private final Integer schoolId;
    private final Integer year;

    @ConstructorProperties({"resources", "schoolId", "year"})
    public UIInvoiceCollection(List<UIInvoiceInfo> resources, Integer schoolId, Integer year) {
        super(resources);
        this.schoolId = schoolId;
        this.year = year;
        this.href = "api/teacher/invoice";
    }

}
