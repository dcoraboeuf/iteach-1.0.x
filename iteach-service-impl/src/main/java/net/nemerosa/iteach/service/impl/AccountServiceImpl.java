package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.*;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.dao.model.TProfile;
import net.nemerosa.iteach.service.*;
import net.nemerosa.iteach.service.model.Account;
import net.nemerosa.iteach.service.model.Profile;
import net.nemerosa.iteach.service.model.TeacherRegistrationForm;
import net.nemerosa.iteach.service.model.TemplateModel;
import net.nemerosa.iteach.service.support.EnvService;
import net.sf.jstring.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final MessageService messageService;
    private final TokenService tokenService;
    private final TemplateService templateService;
    private final EnvService envService;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final Strings strings;
    private final SecurityUtils securityUtils;
    private final Function<? super TAccount, ? extends Account> accountMapper = t ->
            new Account(
                    t.getId(),
                    t.getName(),
                    t.getEmail(),
                    t.isAdministrator(),
                    t.getAuthenticationMode(),
                    t.isVerified(),
                    t.isDisabled()
            );

    @Autowired
    public AccountServiceImpl(MessageService messageService, TokenService tokenService, TemplateService templateService, EnvService envService, AccountRepository accountRepository, PasswordEncoder passwordEncoder, Strings strings, SecurityUtils securityUtils) {
        this.messageService = messageService;
        this.tokenService = tokenService;
        this.templateService = templateService;
        this.envService = envService;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.strings = strings;
        this.securityUtils = securityUtils;
    }

    @Override
    public ID register(Locale locale, TeacherRegistrationForm form) {
        // Creates the account
        int id = accountRepository.createAccount(
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
        return ID.success(id);
    }

    @Override
    public Ack completeRegistration(Locale locale, String token) {
        // User email
        TokenKey key = tokenService.checkToken(token, TokenType.REGISTRATION);
        String email = key.getKey();
        // Gets the user basic data for display
        TAccount account = accountRepository.findByEmail(email);
        // Consumes the token
        tokenService.consumesToken(token, TokenType.REGISTRATION, account.getEmail());
        // Updates the verified flag
        return accountRepository.accountVerified(account.getId());
    }

    @Override
    public Account getAccount(int id) {
        return accountMapper.apply(accountRepository.getById(id));
    }

    @Override
    public Stream<Account> getAccounts() {
        securityUtils.checkAdmin();
        return accountRepository.findAll().parallel().map(accountMapper);
    }

    @Override
    public Ack deleteAccount(int accountId) {
        int adminId = securityUtils.checkAdmin();
        if (adminId == accountId) {
            throw new AccountCannotDeleteHimselfException();
        } else {
            return accountRepository.delete(accountId);
        }
    }

    @Override
    public Profile getProfile() {
        int id = securityUtils.checkTeacher();
        TProfile t = accountRepository.getProfile(id);
        return new Profile(
                t.getCompany(),
                t.getCompanyLogo(),
                t.getPostalAddress(),
                t.getPhone(),
                t.getVat(),
                t.getIban(),
                t.getBic()
        );
    }

    @Override
    public void saveProfile(Profile profile) {
        int id = securityUtils.checkTeacher();
        accountRepository.saveProfile(
                id,
                new TProfile(
                        profile.getCompany(),
                        profile.getCompanyLogo(),
                        profile.getPostalAddress(),
                        profile.getPhone(),
                        profile.getVat(),
                        profile.getIban(),
                        profile.getBic()
                )
        );
    }

    private Message createNewUserMessage(Locale locale, String name, String email) {
        return createUserMessage(locale, name, email, TokenType.REGISTRATION, strings.get(locale, "message.registration"));
    }

    private Message createUserMessage(Locale locale, String name, String email,
                                      TokenType tokenType,
                                      String title) {
        // Generates a token for the response
        String token = tokenService.generateToken(tokenType, email);
        // Gets the return link
        String link = String.format(
                "%s/validate/%s/%s",
                envService.getBaseURL(),
                tokenType,
                token
        );
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
