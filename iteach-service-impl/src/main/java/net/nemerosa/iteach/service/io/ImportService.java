package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface ImportService {

    void importData(int accountId, ObjectNode root);

}
