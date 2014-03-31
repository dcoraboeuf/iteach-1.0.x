package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.model.Comment;
import net.nemerosa.iteach.ui.model.UIComment;
import net.nemerosa.iteach.ui.model.UICommentCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class UICommentAPIController implements UICommentAPI {

    private final CommentService commentService;

    @Autowired
    public UICommentAPIController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @RequestMapping(value = "/{entity}/{entityId}")
    public UICommentCollection getComments(Locale locale, @PathVariable CommentEntity entity, @PathVariable int entityId) {
        return new UICommentCollection(
                entity,
                entityId,
                commentService.getComments(entity, entityId)
                        .stream()
                        .map(this::toUIComment)
                        .collect(Collectors.toList())
        );
    }

    private UIComment toUIComment(Comment c) {
        return new UIComment(
                c.getId(),
                c.getEntity(),
                c.getId(),
                c.getCreation(),
                c.getUpdate(),
                c.getRawContent(),
                // FIXME Formatted content
                c.getRawContent(),
                // FIXME Formatted date
                null,
                // FIXME Formatted date
                null
        );
    }
}
