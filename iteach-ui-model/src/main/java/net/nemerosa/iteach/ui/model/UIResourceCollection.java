package net.nemerosa.iteach.ui.model;

import lombok.Data;

import java.util.List;

@Data
public class UIResourceCollection<R extends UIResource<R>> {

    private final String href;
    private final List<R> resources;

}
