package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.dao.model.TSchool;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final SchoolRepository schoolRepository;

    @Autowired
    public TeacherServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public int createSchool(int teacherId, SchoolForm form) {
        // FIXME Checks the teacher access
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
    public School getSchool(int teacherId, int schoolId) {
        // FIXME Checks the teacher access
        TSchool t = schoolRepository.getById(teacherId, schoolId);
        return new School(
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
    }

}
