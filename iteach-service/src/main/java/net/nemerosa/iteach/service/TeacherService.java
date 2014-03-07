package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;

import java.util.List;

public interface TeacherService {

    List<School> getSchools();

    int createSchool(SchoolForm form);

    School getSchool(int schoolId);

}
