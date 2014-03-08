package net.nemerosa.iteach.acceptance.support;

import java.net.MalformedURLException;

public class UIMalformedURLException extends RuntimeException {
    public UIMalformedURLException(String url, MalformedURLException e) {
        super(String.format("Wrong target URL: %s", url), e);
    }
}
