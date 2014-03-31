package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.model.UICommentCollection;

public interface UICommentAPI {

    /**
     * Gets the list of comments
     */
    UICommentCollection getComments(CommentEntity entity, int entityId);

}
