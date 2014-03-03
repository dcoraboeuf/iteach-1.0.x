package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.dao.SchoolRepository;
import net.nemerosa.iteach.service.TeacherService;
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
                form.getContact(),
                form.getColour(),
                form.getEmail(),
                form.getHourlyRate(),
                form.getPostalAddress(),
                form.getPhone(),
                form.getMobilePhone(),
                form.getWebSite()
        );
    }

}
