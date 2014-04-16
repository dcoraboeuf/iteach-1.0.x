package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.InputException;

public class ProfileCompanyLogoImageTypeException extends InputException {
    public ProfileCompanyLogoImageTypeException(String type, String list) {
        super(type, list);
    }
}
