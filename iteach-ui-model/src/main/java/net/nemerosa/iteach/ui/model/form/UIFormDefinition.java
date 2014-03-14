package net.nemerosa.iteach.ui.model.form;

import lombok.Value;

import java.util.List;

/**
 * Defines the content of a form.
 */
@Value
public class UIFormDefinition {

    private final String title;
    private final String description;
    private final List<UIFormField> fields;

}
