package net.nemerosa.iteach.ui.support;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public interface ErrorHandler {

    ErrorMessage handleError(Locale locale, HttpServletRequest request, Exception ex);

}
