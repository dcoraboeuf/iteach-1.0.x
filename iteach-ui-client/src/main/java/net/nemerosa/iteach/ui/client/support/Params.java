package net.nemerosa.iteach.ui.client.support;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Params {

    public static Params create() {
        return new Params();
    }

    private final Map<String, String> map = new LinkedHashMap<>();

    public Params with(String name, Object value) {
        if (value != null) {
            map.put(name, Objects.toString(value));
        }
        return this;
    }

    public Map<String, String> build() {
        return map;
    }
}
