package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;

import java.util.List;

public interface TeacherService {

    List<School> getSchools(int teacherId);

    int createSchool(int teacherId, SchoolForm form);

    School getSchool(int teacherId, int schoolId);
}
