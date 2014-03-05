package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Account;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class SecurityUtilsImpl implements SecurityUtils {

    @Override
    public void checkTeacher(int teacherId) {
        checkAccount(account -> account.isAdministrator() || account.getId() == teacherId);
    }

    protected void checkAccount(Predicate<Account> check) {
        // Gets the current authentication context if any
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object details = authentication.getDetails();
            if (details instanceof Account) {
                Account account = (Account) details;
                if (check.test(account)) {
                    // OK
                    return;
                }
            }
        }
        throw new AccessDeniedException("Not authorized");
    }

    @Override
    public void checkAdmin() {
        checkAccount(Account::isAdministrator);
    }

}
