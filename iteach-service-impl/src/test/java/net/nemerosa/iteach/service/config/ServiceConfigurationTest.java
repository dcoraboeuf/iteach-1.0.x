package net.nemerosa.iteach.service.config;

import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertTrue;

public class ServiceConfigurationTest {

    @Test
    public void passwordEncoder() {
        PasswordEncoder encoder = new ServiceConfiguration().passwordEncoder();
        String encodedPassword = encoder.encode("admin");
        assertTrue("The encoded password must match", encoder.matches("admin", encodedPassword));
    }

}
