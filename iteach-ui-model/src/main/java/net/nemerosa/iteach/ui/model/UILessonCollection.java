package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UILessonCollection extends UIResourceCollection<UILesson> {

    private final String href = "api/teacher/lesson";
    private final UILessonFilter filter;

    @ConstructorProperties({"filter", "resources"})
    public UILessonCollection(UILessonFilter filter, List<UILesson> resources) {
        super(resources);
        this.filter = filter;
    }
}
