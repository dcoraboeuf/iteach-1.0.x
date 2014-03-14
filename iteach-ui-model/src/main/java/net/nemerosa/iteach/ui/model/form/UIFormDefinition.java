package net.nemerosa.iteach.ui.model.form;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the content of a form.
 */
@Data
public class UIFormDefinition {

    private final List<UIFormField> fields;

    public static UIFormDefinition create() {
        return new UIFormDefinition(new ArrayList<>());
    }

    public UIFormDefinition withText(String name, String title, int size) {
        return with(UIFormField.of(name, title, "text").with("size", size));
    }

    public UIFormDefinition withMemo(String name, String title, int size) {
        return with(UIFormField.of(name, title, "memo").with("size", size));
    }

    public UIFormDefinition withEmail(String name, String title) {
        return with(UIFormField.of(name, title, "email"));
    }

    public UIFormDefinition withColour(String name, String title) {
        return with(UIFormField.of(name, title, "colour"));
    }

    public UIFormDefinition with(String name, String title, String type) {
        return with(UIFormField.of(name, title, type));
    }

    public UIFormDefinition with(UIFormField field) {
        fields.add(field);
        return this;
    }
}
