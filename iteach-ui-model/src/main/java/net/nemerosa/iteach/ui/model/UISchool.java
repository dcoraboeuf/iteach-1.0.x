package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.money.Money;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchool extends UIResource<UISchool> {

    private final String name;
    private final String colour;
    private final Money hourlyRate;
    private final String postalAddress;
    // TODO Other fields for the school

    public UISchool(int id, String name, String colour, Money hourlyRate, String postalAddress) {
        super(id);
        this.name = name;
        this.colour = colour;
        this.hourlyRate = hourlyRate;
        this.postalAddress = postalAddress;
    }

}
