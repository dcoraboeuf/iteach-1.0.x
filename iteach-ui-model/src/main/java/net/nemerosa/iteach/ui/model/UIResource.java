package net.nemerosa.iteach.ui.model;

public abstract class UIResource<R extends UIResource<R>> {

    private final int id;

    public UIResource(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof UIResource)) return false;
        final UIResource other = (UIResource) o;
        return other.canEqual(this) && this.id == other.id;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.id;
        return result;
    }

    public boolean canEqual(Object other) {
        return other instanceof UIResource;
    }

}
