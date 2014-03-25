package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.common.AccountAuthentication;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Account;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class SecurityUtilsImpl implements SecurityUtils {

    @Override
    public int checkTeacher() {
        return checkAccount(account -> true);
    }

    @Override
    public int checkAdmin() {
        return checkAccount(AccountAuthentication::isAdministrator);
    }

    protected int checkAccount(Predicate<AccountAuthentication> check) {
        return withAccount(account -> {
            if (account != null && check.test(account)) return account.getId();
            else throw new AccessDeniedException("Not authorized");
        });
    }

    @Override
    public String getCurrentAccountName() {
        return withAccount(account -> account != null ? account.getEmail() : null);
    }

    @Override
    public AccountAuthentication getCurrentAccount() {
        return withAccount(Function.identity());
    }

    @Override
    public boolean isLogged() {
        return withAccount(account -> account != null);
    }

    @Override
    public <T> T asAccount(Account account, Callable<T> call) throws Exception {
        // Gets the previous context
        SecurityContext previousContext = SecurityContextHolder.getContext();
        try {
            // Switches the context
            SecurityContextImpl context = new SecurityContextImpl();
            AccountAuthenticationDetails details = new AccountAuthenticationDetails(account);
            context.setAuthentication(new AccountAuthenticationToken(details));
            SecurityContextHolder.setContext(context);
            // Performs the call
            return call.call();
        } finally {
            SecurityContextHolder.setContext(previousContext);
        }
    }

    protected <T> T withAccount(Function<AccountAuthentication, T> fn) {
        // Gets the current authentication context if any
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof AccountAuthentication) {
                AccountAuthentication account = (AccountAuthentication) principal;
                return fn.apply(account);
            }
        }
        return fn.apply(null);
    }

}
