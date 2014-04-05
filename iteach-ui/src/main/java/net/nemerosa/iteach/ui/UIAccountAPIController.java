package net.nemerosa.iteach.ui;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.ID;
import net.nemerosa.iteach.common.TokenType;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.nemerosa.iteach.service.AccountService;
import net.nemerosa.iteach.service.ImportExportService;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.*;
import net.nemerosa.iteach.ui.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class UIAccountAPIController implements UIAccountAPI {

    private final AccountService accountService;
    private final ImportExportService importExportService;
    private final SecurityUtils securityUtils;
    private final Function<AccountAuthentication, UITeacher> teacherFromAuthentication = authentication ->
            new UITeacher(
                    authentication.getId(),
                    authentication.getName(),
                    authentication.getEmail(),
                    authentication.isAdministrator(),
                    authentication.getAuthenticationMode()
            );

    @Autowired
    public UIAccountAPIController(AccountService accountService, ImportExportService importExportService, SecurityUtils securityUtils) {
        this.accountService = accountService;
        this.importExportService = importExportService;
        this.securityUtils = securityUtils;
    }

    @Override
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public UIState state(Locale locale) {
        AccountAuthentication authentication = securityUtils.getCurrentAccount();
        if (authentication == null) {
            return UIState.notAuthenticated();
        } else {
            return UIState.authenticated(
                    teacherFromAuthentication.apply(authentication)
            );
        }
    }

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public UITeacher login(Locale locale) {
        AccountAuthentication authentication = securityUtils.getCurrentAccount();
        if (authentication == null) {
            throw new AccessDeniedException("No authentication");
        }
        return teacherFromAuthentication.apply(authentication);
    }

    /**
     * See {@link net.nemerosa.iteach.ui.config.WebSecurityConfig} for the actual logout.
     */
    @RequestMapping(value = "/logged-out", method = RequestMethod.GET)
    public Ack logout() {
        return Ack.validate(securityUtils.isLogged());
    }

    @Override
    @RequestMapping(value = "", method = RequestMethod.GET)
    public UIAccountCollection getAccounts(Locale locale) {
        return new UIAccountCollection(
                accountService
                        .getAccounts()
                        .map(this::toUIAccount)
                        .collect(Collectors.toList())
        );
    }

    private UIAccount toUIAccount(Account o) {
        return new UIAccount(
                o.getId(),
                o.getName(),
                o.getEmail(),
                o.getAuthenticationMode(),
                o.isAdministrator(),
                o.isVerified(),
                o.isDisabled()
        );
    }

    @Override
    @RequestMapping(value = "/{accountId}", method = RequestMethod.GET)
    public UIAccount getAccount(Locale locale, @PathVariable int accountId) {
        return toUIAccount(accountService.getAccount(accountId));
    }

    @Override
    @RequestMapping(value = "/{accountId}", method = RequestMethod.DELETE)
    public Ack deleteAccount(Locale locale, @PathVariable int accountId) {
        return accountService.deleteAccount(accountId);
    }

    @Override
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ID registerAsTeacherWithPassword(Locale locale, @RequestBody UITeacherPasswordForm form) {
        return accountService.register(
                locale,
                new TeacherRegistrationForm(
                        form.getName(),
                        form.getEmail(),
                        form.getPassword()
                )
        );
    }

    @Override
    @RequestMapping(value = "/validate/{tokenType}/{token}", method = RequestMethod.GET)
    public Ack validate(Locale locale, @PathVariable TokenType tokenType, @PathVariable String token) {
        switch (tokenType) {
            case REGISTRATION:
                return accountService.completeRegistration(locale, token);
            default:
                throw new IllegalStateException("Token not handled: " + tokenType);
        }
    }

    @RequestMapping(value = "/{accountId}/import", method = RequestMethod.POST)
    public UIAccount importAccount(Locale locale, @PathVariable int accountId, @RequestParam MultipartFile file) throws IOException {
        // Gets the file as JSON
        try (InputStream in = file.getInputStream()) {
            JsonNode data = ObjectMapperFactory.create().readTree(in);
            // Import
            return importAccount(locale, accountId, data);
        }
    }

    @Override
    public UIAccount importAccount(Locale locale, int accountId, JsonNode data) {
        importExportService.importFile(accountId, data);
        return getAccount(locale, accountId);
    }

    @RequestMapping(value = "/{accountId}/export", method = RequestMethod.GET)
    public void exportAccount(Locale locale, @PathVariable int accountId, HttpServletResponse response) throws IOException {
        // Gets the file as JSON
        JsonNode jsonNode = exportAccount(locale, accountId);
        // Writes as a JSON file
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", String.format("attachment; filename=account-%d.json", accountId));
        // Serializes as JSON
        ObjectMapperFactory.create().writeValue(
                new OutputStreamWriter(
                        response.getOutputStream(),
                        "UTF-8"),
                jsonNode);
    }

    @Override
    public JsonNode exportAccount(Locale locale, int accountId) {
        return importExportService.exportFile(accountId);
    }

    @Override
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public UIProfile getProfile(Locale locale) {
        Profile profile = accountService.getProfile();
        return toUIProfile(profile);
    }

    public static UIProfile toUIProfile(Profile profile) {
        return new UIProfile(
                profile.getCompany(),
                profile.getCompanyLogo(),
                profile.getPostalAddress(),
                profile.getPhone(),
                profile.getVat(),
                profile.getIban(),
                profile.getBic(),
                profile.getInvoiceLastNb()
        );
    }

    @Override
    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public Ack saveProfile(Locale locale, @RequestBody @Valid UIProfile profile) {
        accountService.saveProfile(
                new Profile(
                        profile.getCompany(),
                        profile.getCompanyLogo(),
                        profile.getPostalAddress(),
                        profile.getPhone(),
                        profile.getVat(),
                        profile.getIban(),
                        profile.getBic(),
                        profile.getInvoiceLastNb()
                )
        );
        return Ack.OK;
    }

    @Override
    @RequestMapping(value = "/setup", method = RequestMethod.GET)
    public UISetup getSetup(Locale locale) {
        securityUtils.checkAdmin();
        Setup setup = accountService.getSetup();
        return new UISetup(setup.getEmail());
    }

    @Override
    @RequestMapping(value = "/setup", method = RequestMethod.PUT)
    public Ack saveSetup(Locale locale, @RequestBody @Valid UISetupForm form) {
        securityUtils.checkAdmin();
        return accountService.saveSetup(new SetupForm(
                form.getEmail(),
                form.getPassword(),
                form.getPasswordChange()
        ));
    }

    @Override
    @RequestMapping(value = "/{accountId}/disable", method = RequestMethod.PUT)
    public Ack disableAccount(Locale locale, @PathVariable int accountId) {
        return accountService.disableAccount(accountId);
    }

    @Override
    @RequestMapping(value = "/{accountId}/enable", method = RequestMethod.PUT)
    public Ack enableAccount(Locale locale, @PathVariable int accountId) {
        return accountService.enableAccount(accountId);
    }

    @Override
    @RequestMapping(value = "/passwordChangeRequest", method = RequestMethod.GET)
    public Ack passwordChangeRequest(Locale locale) {
        return accountService.passwordChangeRequest(locale);
    }
}
