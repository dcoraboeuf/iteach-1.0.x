package net.nemerosa.iteach.ui.model.form;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class UIFormField {

    private final String name;
    private final String label;
    private final String type;
    private final Map<String, Object> attributes;
    private final String value;

    public static UIFormField of(String name, String title, String type) {
        return new UIFormField(name, title, type, new HashMap<>(), null);
    }

    public UIFormField with(String name, int value) {
        attributes.put(name, Integer.valueOf(value));
        return this;
    }

    public UIFormField withValue(String value) {
        return new UIFormField(name, label, type, attributes, value);
    }

}
