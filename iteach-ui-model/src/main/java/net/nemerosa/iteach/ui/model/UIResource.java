package net.nemerosa.iteach.ui.model;

import lombok.Data;

@Data
public abstract class UIResource<R extends UIResource<R>> {

    private final int id;

}
