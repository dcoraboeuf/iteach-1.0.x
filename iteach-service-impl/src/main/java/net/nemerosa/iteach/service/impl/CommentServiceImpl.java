package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.dao.model.TComment;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
