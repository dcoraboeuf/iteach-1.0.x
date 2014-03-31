package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.model.UICommentCollection;

import java.util.Locale;

public interface UICommentAPI {

    /**
     * Gets the list of comments
     */
    UICommentCollection getComments(Locale locale, CommentEntity entity, int entityId);

}
