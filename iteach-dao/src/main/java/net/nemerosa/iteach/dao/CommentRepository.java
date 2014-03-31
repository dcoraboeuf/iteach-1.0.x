package net.nemerosa.iteach.dao;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.model.TComment;

import java.util.List;

public interface CommentRepository {

    List<TComment> findByEntityId(int teacherId, CommentEntity entity, int entityId);

}
