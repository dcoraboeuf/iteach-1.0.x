package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Message;

public interface MessagePost {

    void post(Message message, String email);

}
