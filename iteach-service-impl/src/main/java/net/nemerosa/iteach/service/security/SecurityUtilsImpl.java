package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Account;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.Predicate;

@Component
public class SecurityUtilsImpl implements SecurityUtils {

    @Override
    public void checkTeacher(int teacherId) {
        checkAccount(account -> account != null && (account.isAdministrator() || account.getId() == teacherId));
    }

    protected void checkAccount(Predicate<Account> check) {
        boolean ok = withAccount(check::test);
        if (!ok) {
            throw new AccessDeniedException("Not authorized");
        }
    }

    protected <T> T withAccount(Function<Account, T> fn) {
        // Gets the current authentication context if any
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object details = authentication.getDetails();
            if (details instanceof Account) {
                Account account = (Account) details;
                return fn.apply(account);
            }
        }
        return fn.apply(null);
    }

    @Override
    public void checkAdmin() {
        checkAccount(account -> account != null && account.isAdministrator());
    }

    @Override
    public String getCurrentAccountName() {
        return withAccount(account -> account != null ? account.getEmail() : null);
    }

}
