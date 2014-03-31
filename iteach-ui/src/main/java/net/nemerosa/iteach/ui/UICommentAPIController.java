package net.nemerosa.iteach.ui;

import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.ui.model.UICommentCollection;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/account")
public class UICommentAPIController implements UICommentAPI {

    @Override
    @RequestMapping(value = "/{entity}/{entityId}")
    public UICommentCollection getComments(Locale locale, @PathVariable CommentEntity entity, @PathVariable int entityId) {
        // FIXME Method net.nemerosa.iteach.ui.UICommentAPIController.getComments
        return null;
    }
}
