package net.nemerosa.iteach.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.ImportExportService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.io.ImportService;
import net.nemerosa.iteach.service.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ImportExportServiceImpl implements ImportExportService {

    private final ImportService importServiceV1;
    private final AccountService accountService;
    private final SecurityUtils securityUtils;

    @Autowired
    public ImportExportServiceImpl(
            @Qualifier("v1")
            ImportService importServiceV1,
            AccountService accountService, SecurityUtils securityUtils) {
        this.importServiceV1 = importServiceV1;
        this.accountService = accountService;
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
        Account account = accountService.getAccount(accountId);
        try {
            securityUtils.asAccount(account, () -> importService.importData(accountId, root));
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ImportException(ex);
        }
    }

}
