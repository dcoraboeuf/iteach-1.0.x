package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.client.UICommentAPIClient;
import net.nemerosa.iteach.ui.model.UIComment;
import net.nemerosa.iteach.ui.model.UICommentCollection;
import net.nemerosa.iteach.ui.model.UICommentForm;

import java.net.MalformedURLException;
import java.util.Locale;

public class UICommentAPIClientImpl extends AbstractClient<UICommentAPIClient> implements UICommentAPIClient {

    public UICommentAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public UICommentCollection getComments(Locale locale, CommentEntity entity, int entityId) {
        return get(locale, UICommentCollection.class, "/api/comment/%s/list/%d", entity, entityId);
    }

    @Override
    public UIComment postComment(Locale locale, CommentEntity entity, int entityId, UICommentForm form) {
        return post(locale, UIComment.class, form, "/api/comment/%s/%d", entity, entityId);
    }

    @Override
    public UIComment getComment(Locale locale, CommentEntity entity, int commentId) {
        return get(locale, UIComment.class, "/api/comment/%s/%d", entity, commentId);
    }

    @Override
    public Ack deleteComment(Locale locale, CommentEntity entity, int commentId) {
        return delete(locale, Ack.class, "/api/comment/%s/%d", entity, commentId);
    }

    @Override
    public UIComment updateComment(Locale locale, CommentEntity entity, int commentId, UICommentForm form) {
        return put(locale, UIComment.class, form, "/api/comment/%s/%d", entity, commentId);
    }
}
