package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.service.MessagePost;
import net.nemerosa.iteach.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final List<MessagePost> messagePosts;

    @Autowired
    public MessageServiceImpl(List<MessagePost> messagePosts) {
        this.messagePosts = messagePosts;
    }

    @Override
    public void sendMessage(Message message, String email) {
        for (MessagePost messagePost : messagePosts) {
            messagePost.post(message, email);
        }
    }
}
