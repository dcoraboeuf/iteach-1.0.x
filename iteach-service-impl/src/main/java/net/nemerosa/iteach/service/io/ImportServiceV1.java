package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.LessonForm;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.service.model.StudentForm;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.*;

@Component
@Qualifier("v1")
public class ImportServiceV1 implements ImportService {

    private final TeacherService teacherService;
    private final CommentRepository commentRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public ImportServiceV1(TeacherService teacherService, CommentRepository commentRepository, SecurityUtils securityUtils) {
        this.teacherService = teacherService;
        this.commentRepository = commentRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Ack importData(int accountId, ObjectNode root) {
        for (JsonNode school : root.path("schools")) {
            importSchool(school);
        }
        return Ack.OK;
    }

    private void importSchool(JsonNode school) {
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
                        getWebSite(school),
                        "",
                        null
                )
        );
        // School comments
        importComments(CommentEntity.school, schoolId, school.get("comments"));
        // School students
        for (JsonNode student : school.path("students")) {
            importStudent(schoolId, student);
        }
    }

    private void importStudent(int schoolId, JsonNode node) {
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
        // Disabled state?
        if (node.path("disabled").booleanValue()) {
            teacherService.disableStudent(studentId);
        }
        // Lessons
        for (JsonNode lesson : node.path("lessons")) {
            importLesson(studentId, lesson);
        }
        // Student comments
        importComments(CommentEntity.student, studentId, node.get("comments"));
    }

    private void importLesson(int studentId, JsonNode node) {
        // Creates the lesson
        int lessonId = teacherService.createLesson(
                new LessonForm(
                        studentId,
                        getString(node, "location", false, ""),
                        getTime(node, "date", "from"),
                        getTime(node, "date", "to")
                )
        );
        // Lesson comments
        importComments(CommentEntity.lesson, lessonId, node.get("comments"));
    }

    protected void importComments(CommentEntity entity, int entityId, JsonNode comments) {
        for (JsonNode comment : comments) {
            long creation = comment.path("creation").asLong();
            long edition = comment.path("edition").asLong();
            String content = comment.path("content").asText();
            // Dates
            LocalDateTime creationTime = getTime(creation);
            LocalDateTime editionTime = getTime(edition);
            // Creation
            commentRepository.importComment(
                    securityUtils.getCurrentAccount().getId(),
                    entity,
                    entityId,
                    creationTime,
                    editionTime,
                    content
            );
        }
    }

    protected LocalDateTime getTime(long time) {
        if (time == 0) {
            return null;
        } else {
            return LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(time),
                    ZoneId.of("Europe/Brussels")
            );
        }
    }

    protected LocalDateTime getTime(JsonNode node, String fieldDate, String fieldTime) {
        String dateValue = getString(node, fieldDate, true, null);
        String timeValue = getString(node, fieldTime, true, null);
        return getTime(dateValue, timeValue);
    }

    /**
     * The times are stored for the Europe/Brussels time zone. We have to get them in the UTC
     * time zone before considering them as local.
     */
    protected LocalDateTime getTime(String date, String time) {
        LocalDateTime parsedTime = LocalDateTime.parse(date + "T" + time + ":00");
        ZonedDateTime zonedTime = parsedTime.atZone(ZoneId.of("Europe/Brussels"));
        ZonedDateTime utcTime = zonedTime.withZoneSameInstant(ZoneOffset.UTC);
        return utcTime.toLocalDateTime();
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
