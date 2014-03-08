package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchool extends UIResource<UISchool> {

    private final int id;
    private final String name;
    private final String colour;
    private final Money hourlyRate;
    private final String postalAddress;

}
