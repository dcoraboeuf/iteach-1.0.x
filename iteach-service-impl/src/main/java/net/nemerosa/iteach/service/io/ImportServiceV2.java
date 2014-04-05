package net.nemerosa.iteach.service.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.nemerosa.iteach.common.Ack;
import net.nemerosa.iteach.common.CommentEntity;
import net.nemerosa.iteach.common.json.ObjectMapperFactory;
import net.nemerosa.iteach.dao.CommentRepository;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.impl.ImportException;
import net.nemerosa.iteach.service.io.model.*;
import net.nemerosa.iteach.service.model.LessonForm;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.service.model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("v2")
public class ImportServiceV2 implements ImportService {

    private final TeacherService teacherService;
    private final CommentRepository commentRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public ImportServiceV2(TeacherService teacherService, CommentRepository commentRepository, SecurityUtils securityUtils) {
        this.teacherService = teacherService;
        this.commentRepository = commentRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public Ack importData(int accountId, ObjectNode root) {
        // Deserialization
        XAccount account;
        try {
            account = ObjectMapperFactory.create().treeToValue(root, XAccount.class);
        } catch (JsonProcessingException e) {
            throw new ImportException(e);
        }
        // Importing
        account.getSchools().stream().forEach(this::importSchool);
        // OK
        return Ack.OK;
    }

    private void importSchool(XSchool x) {
        // Creates the school
        int schoolId = teacherService.createSchool(
                new SchoolForm(
                        x.getName(),
                        x.getColour(),
                        x.getContact(),
                        x.getHourlyRate(),
                        x.getPostalAddress(),
                        x.getPhone(),
                        x.getMobilePhone(),
                        x.getEmail(),
                        x.getWebSite(),
                        x.getVat(),
                        x.getVatRate()
                )
        );
        // School comments
        importComments(CommentEntity.school, schoolId, x.getComments());
        // School students
        x.getStudents().stream().forEach(s -> importStudent(schoolId, s));
    }

    protected void importStudent(int schoolId, XStudent x) {
        // Creates the student
        int studentId = teacherService.createStudent(
                new StudentForm(
                        schoolId,
                        x.getName(),
                        x.getSubject(),
                        x.getPostalAddress(),
                        x.getPhone(),
                        x.getMobilePhone(),
                        x.getEmail()
                )
        );
        // Disabled state?
        if (x.isDisabled()) {
            teacherService.disableStudent(studentId);
        }
        // Lessons
        x.getLessons().stream().forEach(l -> importLesson(studentId, l));
        // Student comments
        importComments(CommentEntity.student, studentId, x.getComments());
    }

    protected void importLesson(int studentId, XLesson x) {
        // Creates the lesson
        int lessonId = teacherService.createLesson(
                new LessonForm(
                        studentId,
                        x.getLocation(),
                        x.getFrom(),
                        x.getTo()
                )
        );
        // Lesson comments
        importComments(CommentEntity.lesson, lessonId, x.getComments());
    }

    private void importComments(CommentEntity entity, int entityId, List<XComment> comments) {
        comments.stream().forEach(c ->
                commentRepository.importComment(
                        securityUtils.getCurrentAccount().getId(),
                        entity,
                        entityId,
                        c.getCreation(),
                        c.getUpdate(),
                        c.getRawContent()
                )
        );
    }

}
