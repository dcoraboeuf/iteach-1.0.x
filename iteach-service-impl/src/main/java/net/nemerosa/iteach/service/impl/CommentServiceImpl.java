package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.dao.model.TComment;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, SecurityUtils securityUtils) {
        this.commentRepository = commentRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public List<Comment> getComments(CommentEntity entity, int entityId) {
        // Gets the teacher
        int teacherId = securityUtils.checkTeacher();
        // Gets the list of comments
        return commentRepository.findByEntityId(teacherId, entity, entityId)
                .stream()
                .map(this::toComment)
                .collect(Collectors.toList());
    }

    @Override
    public Comment getComment(CommentEntity entity, int commentId) {
        // Gets the teacher
        int teacherId = securityUtils.checkTeacher();
        // Gets the comment
        return toComment(commentRepository.getById(teacherId, entity, commentId));
    }

    @Override
    public int postComment(CommentEntity entity, int entityId, String content) {
        // Gets the teacher
        int teacherId = securityUtils.checkTeacher();
        // Creates the comment
        return commentRepository.create(teacherId, entity, entityId, content);
    }

    @Override
    public Ack deleteComment(CommentEntity entity, int commentId) {
        // Gets the teacher
        int teacherId = securityUtils.checkTeacher();
        // Deletes the comment
        return commentRepository.delete(teacherId, entity, commentId);
    }

    @Override
    public void updateComment(CommentEntity entity, int commentId, String content) {
        // Gets the teacher
        int teacherId = securityUtils.checkTeacher();
        // Updates the comment
        commentRepository.update(teacherId, entity, commentId, content);
    }

    private Comment toComment(TComment t) {
        return new Comment(
                t.getId(),
                t.getEntity(),
                t.getEntityId(),
                t.getCreation(),
                t.getUpdate(),
                t.getRawContent()
        );
    }
}
