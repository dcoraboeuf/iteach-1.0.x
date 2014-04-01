package net.nemerosa.iteach.ui.model;

import lombok.Data;
import net.nemerosa.iteach.common.CommentEntity;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

@Data
public class UIComment extends UIResource {

    private final int id;
    private final CommentEntity entity;
    private final int entityId;
    private final String href;
    private final LocalDateTime creation;
    private final LocalDateTime update;
    private final String rawContent;
    private final String renderedContent;
    private final String formattedCreationDate;
    private final String formattedUpdateDate;

    @ConstructorProperties({"id", "entity", "entityId", "creation", "update", "rawContent", "renderedContent", "formattedCreationDate", "formattedUpdateDate"})
    public UIComment(int id, CommentEntity entity, int entityId, LocalDateTime creation, LocalDateTime update, String rawContent, String renderedContent, String formattedCreationDate, String formattedUpdateDate) {
        this.id = id;
        this.entity = entity;
        this.entityId = entityId;
        this.creation = creation;
        this.update = update;
        this.rawContent = rawContent;
        this.renderedContent = renderedContent;
        this.formattedCreationDate = formattedCreationDate;
        this.formattedUpdateDate = formattedUpdateDate;
        // Link using the ID only
        this.href = UILink.href("api/comment/%d", id);
    }

}
