package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.model.UIComment;
import net.nemerosa.iteach.ui.model.UICommentCollection;
import net.nemerosa.iteach.ui.model.UICommentForm;

import java.util.Locale;

public interface UICommentAPI {

    /**
     * Gets the list of comments
     */
    UICommentCollection getComments(Locale locale, CommentEntity entity, int entityId);

    /**
     * Creates a comment
     */
    UIComment postComment(Locale locale, CommentEntity entity, int entityId, UICommentForm form);

    /**
     * Gets an individual comment
     */
    UIComment getComment(Locale locale, CommentEntity entity, int commentId);

}
