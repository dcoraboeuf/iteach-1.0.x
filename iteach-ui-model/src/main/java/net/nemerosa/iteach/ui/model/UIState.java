package net.nemerosa.iteach.ui.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UIState {

    private final boolean authenticated;
    private final UITeacher teacher;

    public static UIState notAuthenticated() {
        return new UIState(false, null);
    }

    public static UIState authenticated(UITeacher teacher) {
        return new UIState(true, teacher);
    }

}
