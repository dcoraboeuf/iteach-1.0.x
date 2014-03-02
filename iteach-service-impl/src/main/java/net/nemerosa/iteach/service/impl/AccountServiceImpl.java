package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.*;
import net.nemerosa.iteach.dao.AccountDao;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.MessageService;
import net.nemerosa.iteach.service.TemplateService;
import net.nemerosa.iteach.service.TokenService;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.service.model.TemplateModel;
import net.sf.jstring.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AccountServiceImpl implements AccountService {

    private final MessageService messageService;
    private final TokenService tokenService;
    private final TemplateService templateService;
    private final AccountDao accountDao;
    private final PasswordEncoder passwordEncoder;
    private final Strings strings;

    @Autowired
    public AccountServiceImpl(MessageService messageService, TokenService tokenService, TemplateService templateService, AccountDao accountDao, PasswordEncoder passwordEncoder, Strings strings) {
        this.messageService = messageService;
        this.tokenService = tokenService;
        this.templateService = templateService;
        this.accountDao = accountDao;
        this.passwordEncoder = passwordEncoder;
        this.strings = strings;
    }

    @Override
    @Transactional
    public Ack register(Locale locale, TeacherRegistrationForm form) {
        // Creates the account
        accountDao.createAccount(
                AuthenticationMode.PASSWORD,
                form.getEmail(),
                form.getEmail(),
                form.getName(),
                passwordEncoder.encode(form.getPassword())
        );

        // Its initial state is not verified and a notification must be sent by email
        Message message = createNewUserMessage(locale, form.getName(), form.getEmail());
        // Sends the message
        messageService.sendMessage(message, form.getEmail());

        // OK
        return Ack.OK;
    }

    private Message createNewUserMessage(Locale locale, String name, String email) {
        return createUserMessage(locale, name, email, TokenType.REGISTRATION, strings.get(locale, "message.registration"));
    }

    private Message createUserMessage(Locale locale, String name, String email,
                                      TokenType tokenType,
                                      String title) {
        // Generates a token for the response
        String token = tokenService.generateToken(tokenType, email);
        // FIXME Gets the return link
        // String link = uiService.getLink(tokenType, token);
        String link = "TODO-ReturnLink";
        // Message template model
        TemplateModel model = new TemplateModel();
        model.add("userName", name);
        model.add("userEmail", email);
        model.add("link", link);
        // Template ID
        String templateId = tokenType.name().toLowerCase() + ".txt";
        // Message content
        String content = templateService.generate(templateId, locale, model);
        // Creates the message
        return new Message(title, new MessageContent(content, link, token));
    }

}
