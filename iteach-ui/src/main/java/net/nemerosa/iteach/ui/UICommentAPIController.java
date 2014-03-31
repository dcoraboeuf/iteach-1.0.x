package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.model.Comment;
import net.nemerosa.iteach.ui.model.UIComment;
import net.nemerosa.iteach.ui.model.UICommentCollection;
import net.nemerosa.iteach.ui.model.UICommentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @RequestMapping(value = "/{entity}/list/{entityId}", method = RequestMethod.GET)
    public UICommentCollection getComments(Locale locale, @PathVariable CommentEntity entity, @PathVariable int entityId) {
        return new UICommentCollection(
                entity,
                entityId,
                commentService.getComments(entity, entityId)
                        .stream()
                        .map(c -> toUIComment(locale, c))
                        .collect(Collectors.toList())
        );
    }

    @Override
    @RequestMapping(value = "/{entity}/{entityId}", method = RequestMethod.POST)
    public UIComment postComment(Locale locale, @PathVariable CommentEntity entity, @PathVariable int entityId, @RequestBody @Valid UICommentForm form) {
        int id = commentService.postComment(entity, entityId, form.getContent());
        return getComment(locale, entity, id);
    }

    @Override
    @RequestMapping(value = "/{entity}/{commentId}", method = RequestMethod.GET)
    public UIComment getComment(Locale locale, @PathVariable CommentEntity entity, @PathVariable int commentId) {
        return toUIComment(locale, commentService.getComment(entity, commentId));
    }

    private UIComment toUIComment(Locale locale, Comment c) {
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
