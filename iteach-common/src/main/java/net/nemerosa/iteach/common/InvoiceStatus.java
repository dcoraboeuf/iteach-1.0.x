package net.nemerosa.iteach.common;

public enum InvoiceStatus {

    /**
     * The invoice has been created, but no generation has taken place yet.
     */
    CREATED,

    /**
     * The invoice is currently generated.
     */
    GENERATING,

    /**
     * The invoice is ready for download.
     */
    READY

}
