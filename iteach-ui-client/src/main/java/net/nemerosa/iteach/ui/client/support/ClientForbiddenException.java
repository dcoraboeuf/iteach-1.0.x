package net.nemerosa.iteach.ui.client.support;

import java.util.Objects;

public class ClientForbiddenException extends ClientException {

    public ClientForbiddenException(Object request) {
        super(Objects.toString(request, ""));
    }

}
