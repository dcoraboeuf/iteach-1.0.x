package net.nemerosa.iteach.service.io.model;

import lombok.Data;
import net.nemerosa.iteach.service.model.Contract;

@Data
public class RefContract {

    private final int refId;
    private final Contract contract;

}
