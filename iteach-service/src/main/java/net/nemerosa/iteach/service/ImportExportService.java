package net.nemerosa.iteach.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface ImportExportService {

    void importFile(int accountId, JsonNode data);

    JsonNode exportFile(int accountId);

}
