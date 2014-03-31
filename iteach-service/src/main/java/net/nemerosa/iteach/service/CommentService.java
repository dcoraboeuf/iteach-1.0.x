package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.service.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getComments(CommentEntity entity, int entityId);

}
