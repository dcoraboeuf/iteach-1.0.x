package net.nemerosa.iteach.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.ImportExportService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.io.ExportService;
import net.nemerosa.iteach.service.io.ImportService;
import net.nemerosa.iteach.service.model.Account;
import net.nemerosa.iteach.service.model.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ImportExportServiceImpl implements ImportExportService {

    private final ImportService importServiceV1;
    private final ExportService exportService;
    private final AccountService accountService;
    private final TeacherService teacherService;
    private final SecurityUtils securityUtils;

    @Autowired
    public ImportExportServiceImpl(
            @Qualifier("v1")
            ImportService importServiceV1,
            ExportService exportService, AccountService accountService, TeacherService teacherService, SecurityUtils securityUtils) {
        this.importServiceV1 = importServiceV1;
        this.exportService = exportService;
        this.accountService = accountService;
        this.teacherService = teacherService;
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
            securityUtils.asAccount(account, () -> doImport(importService, accountId, root));
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ImportException(ex);
        }
    }

    @Override
    public JsonNode exportFile(int accountId) {
        // Admin only
        securityUtils.checkAdmin();
        // Importing
        Account account = accountService.getAccount(accountId);
        try {
            return securityUtils.asAccount(account, exportService::export);
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ImportException(ex);
        }
    }

    private Ack doImport(ImportService importService, int accountId, ObjectNode root) {
        // Deletes all previous schools
        List<School> schools = teacherService.getSchools();
        for (School school : schools) {
            teacherService.deleteSchool(school.getId());
        }
        // OK
        return importService.importData(accountId, root);
    }

}
