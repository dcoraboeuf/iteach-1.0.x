package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.InputException;

public class ProfileCompanyLogoFileSizeException extends InputException {
    public ProfileCompanyLogoFileSizeException(int sizeInK) {
        super(sizeInK);
    }
}
