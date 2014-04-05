package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.service.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> getComments(CommentEntity entity, int entityId);

    Comment getComment(CommentEntity entity, int commentId);

    int postComment(CommentEntity entity, int entityId, String content);

    Ack deleteComment(CommentEntity entity, int commentId);

    void updateComment(CommentEntity entity, int commentId, String content);

    boolean hasComments(CommentEntity entity, int entityId);
}
