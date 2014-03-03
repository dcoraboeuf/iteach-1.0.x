package net.nemerosa.iteach.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import net.sf.jstring.Localizable;
import net.sf.jstring.LocalizableMessage;
import org.joda.money.Money;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Data
public class UIForm {

    public static UIForm create() {
        return new UIForm();
    }

    private final Map<String, String> fields = new LinkedHashMap<>();

    public UIForm withName(String value) {
        return with("name", value);
    }

    public UIForm withColour(String value) {
        return with("colour", value);
    }

    public UIForm withPostalAddress(String value) {
        return with("postalAddress", value);
    }

    public UIForm with(String field, String value) {
        fields.put(field, value);
        return this;
    }

    @JsonIgnore
    public String getName() {
        return get("name", true, null, FormValidation.builder().withMaxLength(80).withMinLength(1).build(), Function.identity());
    }

    public String getEmail(boolean required) {
        // TODO Email validation (done at browser but still needs to be done at server side)
        return get("email", required, null, FormValidation.builder().withMaxLength(120).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public String getColour() {
        return get("colour", false, "#000000", FormValidation.builder().withRegex("#[A-F0-9]{6}", new LocalizableMessage("validation.format.colour")).build(), Function.identity());
    }

    @JsonIgnore
    public String getContact() {
        return get("contact", false, null, FormValidation.builder().withMaxLength(80).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public String getPostalAddress() {
        return get("postalAddress", false, null, FormValidation.builder().withMaxLength(200).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public String getPhone() {
        return get("phone", false, null, FormValidation.builder().withMaxLength(40).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public String getMobilePhone() {
        return get("mobilePhone", false, null, FormValidation.builder().withMaxLength(40).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public String getWebSite() {
        return get("webSite", false, null, FormValidation.builder().withMaxLength(200).withMinLength(0).build(), Function.identity());
    }

    @JsonIgnore
    public Money getHourlyRate() {
        return get("hourlyRate", false, null,
                FormValidation.builder().with(
                        value -> {
                            try {
                                Money.parse(value);
                                return null;
                            } catch (IllegalArgumentException ex) {
                                return new LocalizableMessage("validation.format.money");
                            }
                        }
                ).build(),
                Money::parse
        );
    }

    private <T> T get(String field, boolean required, T defaultValue, Function<String, Localizable> validation, Function<String, T> transformation) {
        String value = fields.get(field);
        if (value == null) {
            if (required) {
                throw new MissingFormFieldException(field);
            } else {
                return defaultValue;
            }
        } else {
            // Validation
            Localizable message = validation.apply(value);
            if (message != null) {
                throw new InvalidFormFieldException(field, value, message);
            }
            // Transformation
            return transformation.apply(value);
        }
    }
}
