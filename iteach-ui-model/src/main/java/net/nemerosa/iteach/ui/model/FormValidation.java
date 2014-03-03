package net.nemerosa.iteach.ui.model;

import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class FormValidation {

    public static FormValidation builder() {
        return new FormValidation();
    }

    private List<Function<String, Localizable>> functions = new ArrayList<>();

    private FormValidation() {
    }

    public Function<String, Localizable> build() {
        return value -> {
            for (Function<String, Localizable> function : functions) {
                Localizable message = function.apply(value);
                if (message != null) {
                    return message;
                }
            }
            return null;
        };
    }

    public FormValidation withMaxLength(int length) {
        return with(new PredicateValidation(
                value -> StringUtils.length(value) <= length,
                new LocalizableMessage("validation.maxlength", length)
        ));
    }

    public FormValidation withMinLength(int length) {
        return with(new PredicateValidation(
                value -> StringUtils.length(value) >= length,
                new LocalizableMessage("validation.minlength", length)
        ));
    }

    public FormValidation withRegex(String regex, Localizable format) {
        return with(new PredicateValidation(
                value -> Pattern.matches(regex, value),
                new LocalizableMessage("validation.regex", format, regex)
        ));
    }

    public FormValidation with(Function<String, Localizable> validation) {
        functions.add(validation);
        return this;
    }

    public static class PredicateValidation implements Function<String, Localizable> {

        private final Predicate<String> predicate;
        private final Localizable message;

        public PredicateValidation(Predicate<String> predicate, Localizable message) {
            this.predicate = predicate;
            this.message = message;
        }

        @Override
        public Localizable apply(String s) {
            if (predicate.test(s)) {
                return null;
            } else {
                return message;
            }
        }
    }
}
