package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.service.SecurityUtils;
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

    protected void checkAccount(Predicate<AccountAuthentication> check) {
        boolean ok = withAccount(check::test);
        if (!ok) {
            throw new AccessDeniedException("Not authorized");
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

    @Override
    public void checkAdmin() {
        checkAccount(account -> account != null && account.isAdministrator());
    }

    @Override
    public String getCurrentAccountName() {
        return withAccount(account -> account != null ? account.getEmail() : null);
    }

}
