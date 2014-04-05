package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.nemerosa.iteach.service.CommentService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.io.model.*;
import net.nemerosa.iteach.service.model.Comment;
import net.nemerosa.iteach.service.model.Lesson;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExportServiceCurrent implements ExportService {

    private final ObjectMapper objectMapper = ObjectMapperFactory.create();
    private final TeacherService teacherService;
    private final CommentService commentService;

    @Autowired
    public ExportServiceCurrent(TeacherService teacherService, CommentService commentService) {
        this.teacherService = teacherService;
        this.commentService = commentService;
    }

    @Override
    public JsonNode export() {
        return objectMapper.valueToTree(exportAccount());
    }

    protected XAccount exportAccount() {
        return new XAccount(
                teacherService.getSchools()
                        .stream()
                        .map(this::exportSchool)
                        .collect(Collectors.toList())
        );
    }

    protected XSchool exportSchool(School school) {
        return new XSchool(
                school.getName(),
                school.getColour(),
                school.getContact(),
                school.getHourlyRate(),
                school.getPostalAddress(),
                school.getPhone(),
                school.getMobilePhone(),
                school.getEmail(),
                school.getWebSite(),
                school.getVat(),
                school.getVatRate(),
                teacherService.getStudentsForSchool(school.getId())
                        .stream()
                        .map(this::exportStudent)
                        .collect(Collectors.toList()),
                exportComments(CommentEntity.school, school.getId())
        );
    }

    protected List<XComment> exportComments(CommentEntity entity, int entityId) {
        return commentService.getComments(entity, entityId)
                .stream()
                .map(this::exportComment)
                .collect(Collectors.toList());
    }

    protected XComment exportComment(Comment comment) {
        return new XComment(
                comment.getCreation(),
                comment.getUpdate(),
                comment.getRawContent()
        );
    }

    protected XStudent exportStudent(Student student) {
        return new XStudent(
                student.isDisabled(),
                student.getName(),
                student.getSubject(),
                student.getPostalAddress(),
                student.getPhone(),
                student.getMobilePhone(),
                student.getEmail(),
                teacherService.getLessons(student.getId(), null, null)
                        .stream()
                        .map(this::exportLesson)
                        .collect(Collectors.toList()),
                exportComments(CommentEntity.student, student.getId())
        );
    }

    protected XLesson exportLesson(Lesson lesson) {
        return new XLesson(
                lesson.getLocation(),
                lesson.getFrom(),
                lesson.getTo(),
                exportComments(CommentEntity.lesson, lesson.getId())
        );
    }
}
