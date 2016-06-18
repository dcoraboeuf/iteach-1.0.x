package net.nemerosa.iteach.migration.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("net.nemerosa.iteach")
public class MigrationTool {

    @Autowired
    private MigrationProperties migrationProperties;

    @Autowired
    private Migration migration;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MigrationTool.class);
        ConfigurableApplicationContext context = application.run(args);
        context.getBeansOfType(Migration.class).get("migration").run();
    }

}
