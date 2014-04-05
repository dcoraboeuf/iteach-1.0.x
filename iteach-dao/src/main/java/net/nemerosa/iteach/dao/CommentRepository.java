package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.model.TComment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository {

    List<TComment> findByEntityId(int teacherId, CommentEntity entity, int entityId);

    TComment getById(int teacherId, CommentEntity entity, int commentId);

    int create(int teacherId, CommentEntity entity, int entityId, String content);

    Ack delete(int teacherId, CommentEntity entity, int commentId);

    void update(int teacherId, CommentEntity entity, int commentId, String content);

    void importComment(int teacherId, CommentEntity entity, int entityId, LocalDateTime creationTime, LocalDateTime editionTime, String content);

    boolean hasComments(int teacherId, CommentEntity entity, int entityId);
}
