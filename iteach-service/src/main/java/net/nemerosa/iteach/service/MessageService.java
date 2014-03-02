package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Message;

public interface MessageService {

    void sendMessage(Message message, String email);

}
