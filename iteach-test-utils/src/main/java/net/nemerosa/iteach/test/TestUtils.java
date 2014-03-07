package net.nemerosa.iteach.test;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TestUtils {

    private TestUtils() {
    }

    public static String uid(String prefix) {
        return prefix + new SimpleDateFormat("mmssSSS").format(new Date());
    }

    public static String getEnvIfPresent(String systemProperty, String envProperty, String defaulValue) {
        // Trying with the system property first
        String value = System.getProperty(systemProperty);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        // Trying with the environment variable
        value = System.getenv(envProperty);
        if (StringUtils.isNotBlank(value)) {
            return value;
        }
        // Not found
        return defaulValue;
    }

    public static String getEnv(String systemProperty, String envProperty, String name) {
        String value = getEnvIfPresent(systemProperty, envProperty, null);
        if (StringUtils.isBlank(value)) {
            throw new IllegalStateException(
                    String.format(
                            "The %s value must be defined with the system property `%s` or the environment variable `%s`.",
                            name,
                            systemProperty,
                            envProperty
                    )
            );
        } else {
            return value;
        }
    }
}
