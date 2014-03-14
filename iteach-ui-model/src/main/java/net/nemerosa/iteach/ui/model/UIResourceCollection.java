package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.util.List;

@Data
public abstract class UIResourceCollection<R extends UIResource<R>> {

    private final List<R> resources;

    public abstract String getHref();

}
