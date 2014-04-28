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
import net.nemerosa.iteach.service.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        // School contracts
        List<XContract> xContracts = x.getContracts();
        Map<Integer, RefContract> contracts;
        if (xContracts != null) {
            contracts = xContracts
                    .stream()
                    .map(s -> importContract(schoolId, s))
                    .collect(Collectors.toMap(RefContract::getRefId, Function.identity()));
        } else {
            contracts = new HashMap<>();
        }
        // School comments
        importComments(CommentEntity.school, schoolId, x.getComments());
        // School students
        x.getStudents().stream().forEach(s -> importStudent(schoolId, contracts, s));
    }

    protected RefContract importContract(int schoolId, XContract x) {
        Contract contract = teacherService.createContract(
                schoolId,
                new ContractForm(
                        x.getName(),
                        x.getHourlyRate(),
                        x.getVatRate()
                )
        );
        return new RefContract(x.getRefId(), contract);
    }

    protected void importStudent(int schoolId, Map<Integer, RefContract> contracts, XStudent x) {
        // Contract id
        Integer contractId = null;
        RefContract refContract = contracts.get(x.getContractRefId());
        if (refContract != null) {
            contractId = refContract.getContract().getId();
        }
        // Creates the student
        int studentId = teacherService.createStudent(
                new StudentForm(
                        schoolId,
                        contractId,
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
