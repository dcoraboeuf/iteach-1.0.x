package net.nemerosa.iteach.migration.v1;

import com.google.common.collect.ImmutableMap;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Collections;

@Component
public class Migration extends NamedParameterJdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(Migration.class);

    private final MigrationProperties properties;
    private final Neo4jOperations template;

    @Autowired
    public Migration(Neo4jOperations template, DataSource dataSource, MigrationProperties properties) {
        this.template = template;
        this.properties = properties;
        this.setDataSource(dataSource);
    }

    public void run() {
        logger.info("Starting migration...");
        long start = System.currentTimeMillis();
        // Deleting all nodes
        logger.info("Removing all nodes...");
        template.query("MATCH (n) DETACH DELETE n", Collections.emptyMap());

        // For each teacher
        logger.info("Migrating teachers...");
        properties.getTeacher().forEach(this::migrateTeacher);

        // Creating the counters
        logger.info("Creating unique id generators...");
        createUniqueIdGenerators();
        // OK
        long end = System.currentTimeMillis();
        logger.info("End of migration ({} ms)", end - start);
    }

    private void migrateTeacher(String email) {
        logger.info("Migrating teacher: {}", email);
        // TODO Migrating the teacher
        // TODO Migrating the schools
        // TODO Migrating the contracts
        // TODO Migrating the students
        // TODO Migrating the lessons

    }

    private void createUniqueIdGenerators() {
        // TODO createUniqueIdGenerator("Teacher");
        // TODO createUniqueIdGenerator("School");
        // TODO createUniqueIdGenerator("Student");
        // TODO createUniqueIdGenerator("Contract");
        // TODO createUniqueIdGenerator("Lesson");
    }

    private void createUniqueIdGenerator(String label) {
        Result query = template.query(String.format("MATCH (p:%s) RETURN MAX(p.id) as MAX", label),
                ImmutableMap.<String, Object>builder()
                        .put("label", label)
                        .build());
        int max = (Integer) query.queryResults().iterator().next().get("MAX");
        template.query(
                "CREATE (u:UniqueId {label: {label}, id: {id}})",
                ImmutableMap.<String, Object>builder()
                        .put("label", label)
                        .put("id", max)
                        .build()
        );
    }

}
