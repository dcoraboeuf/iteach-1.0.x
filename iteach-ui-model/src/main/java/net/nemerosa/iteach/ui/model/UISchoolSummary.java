package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchoolSummary extends UIResource<UISchoolSummary> {

    private final String name;
    private final String colour;

    public UISchoolSummary(int id, String name, String colour) {
        super(id);
        this.name = name;
        this.colour = colour;
    }

}
