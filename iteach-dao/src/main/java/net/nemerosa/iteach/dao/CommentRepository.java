package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.model.TComment;

import java.util.List;

public interface CommentRepository {

    List<TComment> findByEntityId(int teacherId, CommentEntity entity, int entityId);

    TComment getById(int teacherId, CommentEntity entity, int commentId);

    int create(int teacherId, CommentEntity entity, int entityId, String content);

    Ack delete(int teacherId, CommentEntity entity, int commentId);
}
