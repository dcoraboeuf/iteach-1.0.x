package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.Message;
import net.nemerosa.iteach.common.RunProfile;
import net.nemerosa.iteach.service.MessagePost;
import net.nemerosa.iteach.service.config.MailSetup;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
@Profile(RunProfile.PROD)
public class MailMessagePost implements MessagePost {

    private final Logger logger = LoggerFactory.getLogger(MailMessagePost.class);

    private final JavaMailSender mailSender;
    private final MailSetup mailSetup;

    @Autowired
    public MailMessagePost(JavaMailSender mailSender, MailSetup mailSetup) {
        this.mailSender = mailSender;
        this.mailSetup = mailSetup;
    }

    @Override
    public void post(final Message message, final String email) {
        final String replyToAddress = mailSetup.getReplyTo();
        logger.debug("[mail] Sending message from: {}", replyToAddress);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {

            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                prepareMessage(mimeMessage, message, email, replyToAddress);
            }
        };
        try {
            this.mailSender.send(preparator);
        } catch (MailException ex) {
            logger.error("[mail] Cannot send mail: {}", ExceptionUtils.getRootCauseMessage(ex));
        }
    }

    protected void prepareMessage(MimeMessage mimeMessage, Message message, String destination, String replyToAddress) throws MessagingException, AddressException {
        mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destination));
        mimeMessage.setFrom(new InternetAddress(replyToAddress));
        mimeMessage.setSubject(message.getTitle());
        mimeMessage.setText(message.getContent().getText());
    }

}
