package net.nemerosa.iteach.service.io.model;

import lombok.Data;

import java.util.List;

@Data
public class XStudent {
    private final boolean disabled;
    private final String name;
    private final String subject;
    private final String postalAddress;
    private final String phone;
    private final String mobilePhone;
    private final String email;
    private final List<XLesson> lessons;
    private final List<XComment> comments;
}
