package net.nemerosa.iteach.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

public class ID {

    @Data
    public static class IDAck {

        public static IDAck test(boolean b) {
            return new IDAck(b);
        }

        private final boolean success;

        public ID withId(int id) {
            if (success) {
                return ID.success(id);
            } else {
                return ID.failure();
            }
        }

    }

    @JsonCreator
    public static ID fromJson(JsonNode node) {
        return new ID(
                node.get("success").booleanValue(),
                node.get("value").intValue()
        );
    }

    public static ID failure() {
        return new ID(false, -1);
    }

    public static ID success(int value) {
        return new ID(true, value);
    }

    public static IDAck count(int count) {
        return IDAck.test(count == 1);
    }

    private final boolean success;
    private final int value;

    protected ID(boolean success, int value) {
        this.success = success;
        this.value = value;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getValue() {
        return value;
    }

}
