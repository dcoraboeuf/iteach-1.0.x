package net.nemerosa.iteach.service.config;

import net.nemerosa.iteach.common.RunProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Session;
import javax.naming.NamingException;

@Configuration
@Profile(RunProfile.PROD)
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() throws NamingException {
        // Mail session @ JNDI
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setExpectedType(Session.class);
        factory.setJndiName("java:comp/env/mail/iteach");
        factory.afterPropertiesSet();
        // Mail session
        Session session = (Session) factory.getObject();
        // Mail sender
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setSession(session);
        // OK
        return sender;
    }

    @Bean
    public MailSetup mailSetup() throws NamingException {
        // Mail session @ JNDI
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setExpectedType(String.class);
        factory.setJndiName("java:comp/env/value/replyto");
        factory.afterPropertiesSet();
        // OK
        String replyTo = (String) factory.getObject();
        return new MailSetup(replyTo);
    }

}
