package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UILessonCollection extends UIResourceCollection<UILessonSummary> {

    private final String href = "api/teacher/lesson";
    private final UILessonFilter filter;

    @ConstructorProperties({"filter", "resources"})
    public UILessonCollection(UILessonFilter filter, List<UILessonSummary> resources) {
        super(resources);
        this.filter = filter;
    }
}
