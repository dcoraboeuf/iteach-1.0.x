package net.nemerosa.iteach.ui.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class UIResourceCollection<R extends UIResource> extends UIResource {

    private final List<R> resources;

    public abstract String getHref();

}
