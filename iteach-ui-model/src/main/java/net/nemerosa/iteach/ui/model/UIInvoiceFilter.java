package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.InvoiceStatus;

@Data
public class UIInvoiceFilter {

    private Integer schoolId;
    private Integer year;
    private Boolean downloaded;
    private InvoiceStatus status;

}
