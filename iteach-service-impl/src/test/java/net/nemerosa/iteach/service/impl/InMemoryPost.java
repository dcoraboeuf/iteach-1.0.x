package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.service.MessagePost;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InMemoryPost implements MessagePost {

    private final Map<String, Message> messages = new LinkedHashMap<>();

    @Override
    public synchronized void post(Message message, String destination) {
        messages.put(destination, message);
    }

    public Message getMessage(String destination) {
        return messages.get(destination);
    }

}
