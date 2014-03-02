package net.nemerosa.iteach.service.config;

import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import java.util.Locale;

@Configuration
public class ServiceConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }

    @Bean
    public Strings strings() {
        return StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
    }

}
