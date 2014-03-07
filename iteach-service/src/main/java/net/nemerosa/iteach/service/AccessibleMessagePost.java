package net.nemerosa.iteach.service;

import net.nemerosa.iteach.common.Message;

public interface AccessibleMessagePost extends MessagePost {

    Message getMessage(String email);

}
