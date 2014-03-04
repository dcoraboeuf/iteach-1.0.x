package net.nemerosa.iteach.service.security;

import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.model.Account;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtilsImpl implements SecurityUtils {

    @Override
    public void checkTeacher(int teacherId) {
        // Gets the current authentication context if any
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object details = authentication.getDetails();
            if (details instanceof Account) {
                Account account = (Account) details;
                if (account.isAdministrator() || account.getId() == teacherId) {
                    // OK
                    return;
                }
            }
        }
        throw new AccessDeniedException("Not authorized");
    }

}
