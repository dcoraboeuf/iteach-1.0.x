package net.nemerosa.iteach.service;

import net.nemerosa.iteach.service.model.School;
import net.nemerosa.iteach.service.model.SchoolForm;
import net.nemerosa.iteach.service.model.Student;
import net.nemerosa.iteach.service.model.StudentForm;

import java.util.List;

public interface TeacherService {

    List<School> getSchools();

    int createSchool(SchoolForm form);

    School getSchool(int schoolId);

    int createStudent(StudentForm form);

    Student getStudent(int studentId);

    List<Student> getStudents();

    void updateSchool(int schoolId, SchoolForm form);
}
