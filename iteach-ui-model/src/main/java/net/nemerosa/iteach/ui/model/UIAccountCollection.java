package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UIAccountCollection extends UIResourceCollection<UIAccount> {

    private final String href = "api/account";

    public UIAccountCollection(List<UIAccount> resources) {
        super(resources);
    }

}
