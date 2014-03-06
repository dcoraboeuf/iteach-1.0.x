package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.common.RunProfile;
import net.nemerosa.iteach.service.MessagePost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Message post that is not active in production environment. Just logs the messages.
 */
@Component
@Profile({RunProfile.DEV, RunProfile.ACCEPTANCE, RunProfile.TEST})
public class LogMessagePost implements MessagePost {

    private final Logger logger = LoggerFactory.getLogger(LogMessagePost.class);

    @Override
    public void post(Message message, String email) {
        logger.info(
                "[message] Sending message to '{}':\n-----------------\n{}\n\n{}\n-----------------\n",
                email,
                message.getTitle(),
                message.getContent());
    }
}
