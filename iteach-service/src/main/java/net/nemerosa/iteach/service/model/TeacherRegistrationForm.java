package net.nemerosa.iteach.service.model;

import lombok.Data;

@Data
public class TeacherRegistrationForm {

    private final String name;
    private final String email;
    private final String password;

}
