package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;

@EqualsAndHashCode(callSuper = false)
@Data
public class UISchoolSummary extends UIResource {

    private final int id;
    private final String href;
    private final String name;
    private final String colour;
    private final boolean hasComments;

    @ConstructorProperties({"id", "name", "colour", "hasComments"})
    public UISchoolSummary(int id, String name, String colour, boolean hasComments) {
        this.id = id;
        this.hasComments = hasComments;
        this.href = UILink.href("api/teacher/school/%d", id);
        this.name = name;
        this.colour = colour;
    }

}
