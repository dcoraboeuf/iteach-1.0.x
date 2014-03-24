package net.nemerosa.iteach.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.service.ImportExportService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.io.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ImportExportServiceImpl implements ImportExportService {

    private final ImportService importServiceV1;
    private final SecurityUtils securityUtils;

    @Autowired
    public ImportExportServiceImpl(
            @Qualifier("v1")
            ImportService importServiceV1,
            SecurityUtils securityUtils) {
        this.importServiceV1 = importServiceV1;
        this.securityUtils = securityUtils;
    }

    @Override
    public void importFile(int accountId, JsonNode data) {
        // Admin only
        securityUtils.checkAdmin();
        // Structure
        if (!data.isObject()) {
            throw new ImportNotObjectException();
        }
        ObjectNode root = (ObjectNode) data;
        // Version
        ImportService importService;
        int version = root.path("version").asInt();
        switch (version) {
            case 1:
                importService = importServiceV1;
                break;
            // TODO case 2: Importing current version
            default:
                throw new ImportVersionNotRecognizedException(version);
        }
        // Importing
        // TODO securityUtils.asAccount
        importService.importData(accountId, root);
    }

}
