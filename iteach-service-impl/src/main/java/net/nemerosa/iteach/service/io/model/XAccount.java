package net.nemerosa.iteach.service.io.model;

import lombok.Data;

import java.util.List;

@Data
public class XAccount {

    private final int version = 2;
    private final List<XSchool> schools;

}
