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

    @ConstructorProperties({"resources", "filter"})
    public UIInvoiceCollection(List<UIInvoiceInfo> resources, UIInvoiceFilter filter) {
        super(resources);
        this.filter = filter;
        this.href = "api/teacher/invoice/filter";
    }

}
