package net.nemerosa.iteach.service.model;

import lombok.Data;
import net.nemerosa.iteach.common.CommentEntity;

import java.time.LocalDateTime;

@Data
public class Comment {
    private final int id;
    private final CommentEntity entity;
    private final int entityId;
    private final LocalDateTime creation;
    private final LocalDateTime update;
    private final String rawContent;
}
