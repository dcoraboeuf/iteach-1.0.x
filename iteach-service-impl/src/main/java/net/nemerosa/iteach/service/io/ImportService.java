package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.common.Ack;

public interface ImportService {

    Ack importData(int accountId, ObjectNode root);

}
