package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class UIForm {

    public static UIForm create() {
        return new UIForm();
    }

    private final Map<String, Object> fields = new LinkedHashMap<String, Object>();

    public UIForm withName(String value) {
        return with("name", value);
    }

    public UIForm withColour(String value) {
        return with("colour", value);
    }

    public UIForm withPostalAddress(String value) {
        return with("postalAddress", value);
    }

    public UIForm with(String field, Object value) {
        fields.put(field, value);
        return this;
    }
}
