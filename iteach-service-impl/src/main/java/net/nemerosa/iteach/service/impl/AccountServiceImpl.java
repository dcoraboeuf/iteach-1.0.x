package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.common.*;
import net.nemerosa.iteach.dao.AccountRepository;
import net.nemerosa.iteach.dao.model.TAccount;
import net.nemerosa.iteach.dao.model.TProfile;
import net.nemerosa.iteach.service.*;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.service.support.EnvService;
import net.sf.jstring.Strings;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
    public Ack passwordChangeRequest(Locale locale) {
        // Gets the current account
        int id = securityUtils.checkTeacher();
        TAccount t = accountRepository.getById(id);
        // Checks the mode
        if (t.getAuthenticationMode() != AuthenticationMode.PASSWORD) return Ack.NOK;
        // Creates a message and sends it
        Message message = createPasswordChangeRequestMessage(locale, t.getName(), t.getEmail());
        // Sends the message
        messageService.sendMessage(message, t.getEmail());
        // OK
        return Ack.OK;
    }

    @Override
    public Ack passwordChange(String token, String oldPassword, String newPassword) {
        // User email
        TokenKey key = tokenService.checkToken(token, TokenType.PASSWORD_CHANGE);
        String email = key.getKey();
        // Gets the user by using its email
        TAccount account = accountRepository.findByEmail(email);
        // Checks the password
        if (!accountRepository.checkPassword(
                account.getId(),
                encodedPassword -> passwordEncoder.matches(oldPassword, encodedPassword)
        )) {
            throw new AccountPasswordCheckException();
        }
        // Consumes the token
        tokenService.consumesToken(token, TokenType.PASSWORD_CHANGE, account.getEmail());
        // Changes the password
        accountRepository.changePassword(account.getId(), passwordEncoder.encode(newPassword));
        // OK
        return Ack.OK;
    }

    @Override
    public Ack updateProfileCompanyLogo(Document file) {
        int teacherId = securityUtils.checkTeacher();
        // Control of the file type
        if (!ArrayUtils.contains(ACCEPTED_IMAGE_TYPES, file.getType())) {
            throw new ProfileCompanyLogoImageTypeException(file.getType(), StringUtils.join(ACCEPTED_IMAGE_TYPES, ", "));
        }
        // TODO Reduction of the image size using a service
        // Control of the file size
        if (file.getContent().length > AccountRepository.COMPANY_LOGO_MAX_SIZE) {
            throw new ProfileCompanyLogoFileSizeException(AccountRepository.COMPANY_LOGO_MAX_SIZE / 1000);
        }
        // Stores the image
        return accountRepository.saveProfileCompanyLogo(teacherId, file.toUntitledDocument());
    }

    @Override
    public UntitledDocument getProfileCompanyLogo() {
        return accountRepository.getProfileCompanyLogo(securityUtils.checkTeacher());
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
                        profile.getPostalAddress(),
                        profile.getPhone(),
                        profile.getVat(),
                        profile.getIban(),
                        profile.getBic()
                )
        );
    }

    @Override
    public Setup getSetup() {
        int adminId = securityUtils.checkAdmin();
        // Gets the account
        TAccount t = accountRepository.getById(adminId);
        // OK
        return new Setup(
                t.getEmail()
        );
    }

    @Override
    public Ack saveSetup(SetupForm form) {
        int adminId = securityUtils.checkAdmin();
        // Checks the password
        if (!accountRepository.checkPassword(
                adminId,
                encodedPassword -> passwordEncoder.matches(form.getPassword(), encodedPassword)
        )) {
            throw new AccountPasswordCheckException();
        }
        // Changes the email
        accountRepository.updateEmail(adminId, form.getEmail());
        // Changes the password if needed
        if (StringUtils.isNotBlank(form.getPasswordChange())) {
            accountRepository.changePassword(
                    adminId,
                    passwordEncoder.encode(
                            form.getPasswordChange()
                    )
            );
        }
        // OK
        return Ack.OK;
    }

    @Override
    public Ack disableAccount(int accountId) {
        securityUtils.checkAdmin();
        TAccount t = accountRepository.getById(accountId);
        accountRepository.disable(accountId, true);
        return Ack.validate(!t.isDisabled());
    }

    @Override
    public Ack enableAccount(int accountId) {
        securityUtils.checkAdmin();
        TAccount t = accountRepository.getById(accountId);
        accountRepository.disable(accountId, false);
        return Ack.validate(t.isDisabled());
    }

    private Message createNewUserMessage(Locale locale, String name, String email) {
        return createUserMessage(
                locale, name, email, TokenType.REGISTRATION, strings.get(locale, "message.registration")
        );
    }

    private Message createPasswordChangeRequestMessage(Locale locale, String name, String email) {
        return createUserMessage(
                locale, name, email, TokenType.PASSWORD_CHANGE, strings.get(locale, "message.passwordChangeRequest")
        );
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
