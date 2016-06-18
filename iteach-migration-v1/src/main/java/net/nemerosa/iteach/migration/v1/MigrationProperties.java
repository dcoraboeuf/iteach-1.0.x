package net.nemerosa.iteach.migration.v1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@ConfigurationProperties(prefix = "iteach.migration")
@Component
@Data
public class MigrationProperties {

    /**
     * List of teacher emails to include
     */
    private List<String> teacher = Collections.emptyList();

}
