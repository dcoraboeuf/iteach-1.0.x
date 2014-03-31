package net.nemerosa.iteach.ui.client.support;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.client.UICommentAPIClient;
import net.nemerosa.iteach.ui.model.UICommentCollection;

import java.net.MalformedURLException;
import java.util.Locale;

public class UICommentAPIClientImpl extends AbstractClient<UICommentAPIClient> implements UICommentAPIClient {

    public UICommentAPIClientImpl(String url) throws MalformedURLException {
        super(url);
    }

    @Override
    public UICommentCollection getComments(Locale locale, CommentEntity entity, int entityId) {
        return get(locale, UICommentCollection.class, "/api/comment/%s/%d", entity, entityId);
    }
}
