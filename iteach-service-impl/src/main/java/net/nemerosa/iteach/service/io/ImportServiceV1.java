package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.service.model.StudentForm;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Qualifier("v1")
public class ImportServiceV1 implements ImportService {

    private final TeacherService teacherService;

    @Autowired
    public ImportServiceV1(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public void importData(int accountId, ObjectNode root) {
        for (JsonNode school : root.path("schools")) {
            // Creates the school
            int schoolId = teacherService.createSchool(
                    new SchoolForm(
                            getName(school),
                            getColour(school),
                            "",
                            getHourlyRate(school),
                            getPostalAddress(school),
                            getPhone(school),
                            getMobilePhone(school),
                            getEmail(school),
                            getWebSite(school)
                    )
            );
            // TODO School comments
            // School students
            for (JsonNode student : school.path("students")) {
                importStudent(accountId, schoolId, student);
            }
        }
    }

    private void importStudent(int accountId, int schoolId, JsonNode node) {
        // Creates the student
        int studentId = teacherService.createStudent(
                new StudentForm(
                        schoolId,
                        getName(node),
                        getString(node, "subject", false, ""),
                        getPostalAddress(node),
                        getPhone(node),
                        getMobilePhone(node),
                        getEmail(node)
                )
        );
        // TODO Lessons
        // TODO Student comments
    }

    private String getWebSite(JsonNode node) {
        return getCoordinate(node, "WEB");
    }

    private String getEmail(JsonNode node) {
        return getCoordinate(node, "EMAIL");
    }

    private String getMobilePhone(JsonNode node) {
        return getCoordinate(node, "MOBILE_PHONE");
    }

    private String getPhone(JsonNode node) {
        return getCoordinate(node, "PHONE");
    }

    private String getPostalAddress(JsonNode node) {
        return getCoordinate(node, "ADDRESS");
    }

    private String getCoordinate(JsonNode node, String type) {
        for (JsonNode coordinate : node.path("coordinates")) {
            if (type.equals(coordinate.path("type").textValue())) {
                return coordinate.path("value").textValue();
            }
        }
        return "";
    }

    private Money getHourlyRate(JsonNode node) {
        JsonNode hrate = node.path("hrate");
        if (hrate.isMissingNode()) {
            return null;
        } else {
            double amount = hrate.doubleValue();
            return Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(amount));
        }
    }

    private String getColour(JsonNode node) {
        return getString(node, "color", false, "#FFFFFF");
    }

    protected String getName(JsonNode node) {
        return getString(node, "name", true, null);
    }

    protected String getString(JsonNode node, String field, boolean required, String defaultValue) {
        JsonNode fieldNode = node.path(field);
        if (fieldNode.isMissingNode()) {
            if (required) {
                // TODO Context
                throw new ImportMissingFieldException(field);
            } else {
                return defaultValue;
            }
        } else {
            return fieldNode.textValue();
        }
    }

}
