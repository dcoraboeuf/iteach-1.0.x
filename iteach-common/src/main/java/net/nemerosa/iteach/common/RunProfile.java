package net.nemerosa.iteach.common;

public interface RunProfile {

    /**
     * Real environments
     */
    String PROD = "prod";

    /**
     * Automated tests running against a deployed application
     */
    String ACCEPTANCE = "acceptance";

    /**
     * Unit tests
     */
    String TEST = "test";

    /**
     * Development mode, from within an IDE
     */
    String DEV = "dev";

}
