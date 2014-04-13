package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class UIInvoiceCollection extends UIResourceCollection<UIInvoiceInfo> {

    private final String href;
    private final UIInvoiceFilter filter;
    private final int totalCount;

    @ConstructorProperties({"resources", "filter", "totalCount"})
    public UIInvoiceCollection(List<UIInvoiceInfo> resources, UIInvoiceFilter filter, int totalCount) {
        super(resources);
        this.filter = filter;
        this.totalCount = totalCount;
        // TODO Have the list of parameters for the filter
        this.href = "api/teacher/invoice";
    }

}
