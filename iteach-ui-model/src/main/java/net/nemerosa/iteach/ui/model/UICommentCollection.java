package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.CommentEntity;

import java.beans.ConstructorProperties;
import java.util.List;

@Data
public class UICommentCollection extends UIResourceCollection<UIComment> {

    private final String href;
    private final CommentEntity entity;
    private final int entityId;

    @ConstructorProperties({"entity", "entityId", "resources"})
    public UICommentCollection(CommentEntity entity, int entityId, List<UIComment> resources) {
        super(resources);
        this.entity = entity;
        this.entityId = entityId;
        this.href = UILink.href("api/comment/%s/%d", entity, entityId);
    }

}
