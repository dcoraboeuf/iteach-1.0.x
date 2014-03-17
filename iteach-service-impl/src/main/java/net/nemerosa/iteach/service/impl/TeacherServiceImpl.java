package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.model.TSchool;
import net.nemerosa.iteach.service.SecurityUtils;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.service.model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final SchoolRepository schoolRepository;
    private final SecurityUtils securityUtils;
    private final Function<? super TSchool, ? extends School> schoolFn = t -> new School(
            t.getId(),
            t.getTeacherId(),
            t.getName(),
            t.getColour(),
            t.getContact(),
            t.getHourlyRate(),
            t.getPostalAddress(),
            t.getPhone(),
            t.getMobilePhone(),
            t.getEmail(),
            t.getWebSite()
    );

    @Autowired
    public TeacherServiceImpl(SchoolRepository schoolRepository, SecurityUtils securityUtils) {
        this.schoolRepository = schoolRepository;
        this.securityUtils = securityUtils;
    }

    @Override
    public List<School> getSchools() {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Lists
        return schoolRepository.findAll(teacherId).parallelStream().map(schoolFn).collect(Collectors.toList());
    }

    @Override
    public int createSchool(SchoolForm form) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Creation
        return schoolRepository.create(
                teacherId,
                form.getName(),
                form.getColour(),
                form.getContact(),
                form.getEmail(),
                form.getHourlyRate(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getWebSite()
        );
    }

    @Override
    public School getSchool(int schoolId) {
        // Checks the teacher access
        int teacherId = securityUtils.checkTeacher();
        // Access
        return schoolFn.apply(schoolRepository.getById(teacherId, schoolId));
    }

    @Override
    public int createStudent(StudentForm form) {
        // FIXME Method net.nemerosa.iteach.service.impl.TeacherServiceImpl.createStudent
        return 0;
    }

}
