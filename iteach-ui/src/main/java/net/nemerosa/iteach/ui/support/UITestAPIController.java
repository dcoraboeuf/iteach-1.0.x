package net.nemerosa.iteach.ui.support;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.common.RunProfile;
import net.nemerosa.iteach.service.AccessibleMessagePost;
import net.nemerosa.iteach.ui.model.UITestAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is enabled only when the active profile is {@link RunProfile#ACCEPTANCE} and allows
 * acceptance tests to connect to the API to get additional information.
 */
@Profile({RunProfile.ACCEPTANCE, RunProfile.DEV})
@RestController
@RequestMapping("/api/test")
public class UITestAPIController implements UITestAPI {

    private final AccessibleMessagePost messagePost;

    @Autowired
    public UITestAPIController(AccessibleMessagePost accessibleMessagePost) {
        this.messagePost = accessibleMessagePost;
    }

    /**
     * Collects the message for a user
     */
    @RequestMapping(value = "/message/{email:.*}", method = RequestMethod.GET)
    public synchronized Message getMessage(@PathVariable String email) {
        Message latestMessage = messagePost.getMessage(email);
        if (latestMessage != null) {
            return latestMessage;
        } else {
            throw new NoMessageFoundException(email);
        }
    }
}
