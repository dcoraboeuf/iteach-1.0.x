package net.nemerosa.iteach.ui.model;

import lombok.Data;

@Data
public class UILink {

    private final String title;
    private final String href;

    public static UILink of(String title, String href) {
        return new UILink(title, href);
    }

    public static String href(String uri, Object... parameters) {
        return String.format(uri, parameters);
    }
}
