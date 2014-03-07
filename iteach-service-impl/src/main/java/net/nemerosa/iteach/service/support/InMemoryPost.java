package net.nemerosa.iteach.service.support;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.common.RunProfile;
import net.nemerosa.iteach.service.AccessibleMessagePost;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This component is only available when running in {@link RunProfile#ACCEPTANCE} mode.
 */
@Profile({RunProfile.ACCEPTANCE, RunProfile.DEV})
@Component
public class InMemoryPost implements AccessibleMessagePost {

    private final Map<String, Message> messages = new LinkedHashMap<>();

    @Override
    public synchronized void post(Message message, String destination) {
        messages.put(destination, message);
    }

    @Override
    public Message getMessage(String destination) {
        return messages.get(destination);
    }

}
