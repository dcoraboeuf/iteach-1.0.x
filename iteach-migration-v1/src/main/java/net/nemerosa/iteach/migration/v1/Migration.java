package net.nemerosa.iteach.migration.v1;

import com.google.common.collect.ImmutableMap;
import org.neo4j.ogm.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class Migration extends NamedParameterJdbcDaoSupport {

    private final Logger logger = LoggerFactory.getLogger(Migration.class);

    private final MigrationProperties properties;
    private final Neo4jOperations template;
    private final NamedParameterJdbcTemplate h2;

    private final String createdBy = "migration";
    private final Date createdAt = Date.from(LocalDateTime.now(ZoneOffset.UTC).toInstant(ZoneOffset.UTC));

    @Autowired
    public Migration(Neo4jOperations template, DataSource dataSource, MigrationProperties properties) {
        this.template = template;
        this.properties = properties;
        this.setDataSource(dataSource);
        this.h2 = new NamedParameterJdbcTemplate(dataSource);
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

        // Migrating the teacher
        Map<String, Object> teacher = h2.queryForMap(
                "SELECT * FROM ACCOUNT WHERE EMAIL = :email",
                Collections.singletonMap("email", email)
        );
        int teacherId = (Integer) teacher.get("ID");
        Map<String, Integer> teacherParam = Collections.singletonMap("teacherId", teacherId);
        createNode(
                "Teacher",
                teacher,
                "id", "email", "name", "company", "postalAddress", "phone", "vat", "iban", "bic"
        );

        // Migrating the schools
        h2.queryForList("SELECT * FROM SCHOOL WHERE TEACHERID = :teacherId", teacherParam).forEach(
                school -> migrateSchool(teacherId, school)
        );

        // Migrating the contracts
        h2.queryForList("SELECT * FROM CONTRACT WHERE TEACHERID = :teacherId", teacherParam).forEach(
                contract -> migrateContract(teacherId, contract)
        );

        // Migrating the students
        h2.queryForList("SELECT * FROM STUDENT WHERE TEACHERID = :teacherId", teacherParam).forEach(
                student -> migrateStudent(teacherId, student)
        );

        // TODO Migrating the lessons

    }

    private void migrateStudent(int teacherId, Map<String, Object> student) {
        logger.info("Migrating student: {}", student.get("NAME"));
        Integer schoolId = (Integer) student.get("SCHOOLID");
        Integer contractId = (Integer) student.get("CONTRACTID");
        // Student node
        createNode(
                "Student",
                student,
                // TODO `subject` should be part of the contract
                "id", "name", "disabled", "subject", "email", "postalAddress", "phone", "mobilePhone"
        );
        // No contract --> link to school contract
        int studentId = (Integer) student.get("ID");
        if (contractId == null) {
            template.query(
                    format(
                            "MATCH (c: Contract {id: %d}), (st: Student {id: %d}) " +
                                    "CREATE (c)-[:STUDENT]->(st)",
                            schoolId,
                            studentId
                    ),
                    Collections.emptyMap()
            );
        }
        // Contract, direct link
        else {
            template.query(
                    format(
                            "MATCH (c: Contract {id: %d}), (st: Student {id: %d}) " +
                                    "CREATE (c)-[:STUDENT]->(st)",
                            contractId,
                            studentId
                    ),
                    Collections.emptyMap()
            );
        }
    }

    private void migrateContract(int teacherId, Map<String, Object> contract) {
        logger.info("Migrating contract: {}", contract.get("NAME"));
        int schoolId = (Integer) contract.get("SCHOOLID");
        createNode(
                format(
                        "MATCH (s: School {id: %d}), (t: Teacher {id: %d})",
                        schoolId,
                        teacherId
                ),
                "Contract",
                ", (n)-[:SCHOOL]->(s), (n)-[:TEACHER]->(t)",
                contract,
                "id", "name", "hourlyRate", "vatRate"
        );
    }

    private void migrateSchool(int teacherId, Map<String, Object> school) {
        logger.info("Migrating school: {}", school.get("NAME"));
        createNode(
                "School",
                school,
                "id", "name", "contact", "colour", "email", "postalAddress", "phone", "mobilePhone", "webSite"
        );
        int schoolId = (Integer) school.get("ID");
        // School contract
        // TODO Marks as default school contract
        createNode(
                format(
                        "MATCH (s: School {id: %d}), (t: Teacher {id: %d})",
                        schoolId,
                        teacherId
                ),
                "Contract",
                ", (n)-[:SCHOOL]->(s), (n)-[:TEACHER]->(t)",
                school,
                "id", "name", "hourlyRate", "vatRate"
        );
    }

    private void createNode(String label, Map<String, Object> source, String... fields) {
        createNode("", label, "", source, fields);
    }

    private void createNode(String match, String label, String associations, Map<String, Object> source, String... fields) {

        List<String> parameters = new ArrayList<>(Arrays.asList(fields));
        parameters.add("createdBy");
        parameters.add("createdAt");

        Map<String, Object> sources = new HashMap<>(source);
        sources.put("CREATEDBY", createdBy);
        sources.put("CREATEDAT", createdAt);

        String cypher = format(
                "%s%nCREATE (n: %s {%s})%n%s",
                match,
                label,
                parameters.stream()
                        .map(field -> format("%s: {%s}", field, field.toUpperCase()))
                        .collect(Collectors.joining(", ")),
                associations
        );

        logger.info("CYPHER: {}", cypher);

        template.query(
                cypher,
                sources
        );
    }

    private void createUniqueIdGenerators() {
        createUniqueIdGenerator("Teacher");
        createUniqueIdGenerator("School");
        createUniqueIdGenerator("Student");
        createUniqueIdGenerator("Contract");
        // TODO createUniqueIdGenerator("Lesson");
    }

    private void createUniqueIdGenerator(String label) {
        Result query = template.query(format("MATCH (p:%s) RETURN MAX(p.id) as MAX", label),
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
