package net.nemerosa.iteach.common;

public enum InvoiceStatus {

    /**
     * The invoice has been created, but no generation has taken place yet.
     */
    CREATED(false),

    /**
     * The invoice is currently generated.
     */
    GENERATING(false),

    /**
     * The invoice is ready for download.
     */
    READY(true),

    /**
     * The invoice could not be generated
     */
    ERROR(true);

    private final boolean finished;

    private InvoiceStatus(boolean finished) {
        this.finished = finished;
    }

    public boolean isFinished() {
        return finished;
    }

}
