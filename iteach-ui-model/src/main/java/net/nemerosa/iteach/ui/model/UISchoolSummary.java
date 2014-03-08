package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchoolSummary extends UIResource<UISchoolSummary> {

    private final int id;
    private final String name;
    private final String colour;

}
