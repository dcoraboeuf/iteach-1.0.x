package net.nemerosa.iteach.service.config;

import net.sf.jstring.Strings;
import net.sf.jstring.support.StringsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;

@Configuration
public class ServiceConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Strings strings() {
        return StringsLoader.auto(Locale.ENGLISH, Locale.FRENCH, Locale.GERMAN);
    }

    public static void main(String[] args) {
        PasswordEncoder encoder = new ServiceConfiguration().passwordEncoder();
        for (String arg : args) {
            System.out.format(
                    "%s ---> %s",
                    arg,
                    encoder.encode(arg)
            );
        }
    }

}
