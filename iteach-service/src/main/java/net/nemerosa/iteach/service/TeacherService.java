package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;

public interface TeacherService {

    int createSchool(int teacherId, SchoolForm form);

    School getSchool(int teacherId, int schoolId);
}
