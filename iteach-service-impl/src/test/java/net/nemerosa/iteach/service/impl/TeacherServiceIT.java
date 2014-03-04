package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.test.TestUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TeacherServiceIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    public TeacherService teacherService;

    @Test
    public void create_school() {
        String name = TestUtils.uid("T");
        String email = String.format("%s@test.com", name);
        // Creates a teacher
        serviceITSupport.createTeacherAndCompleteRegistration(name, email);
        // TODO Creates a school for this teacher
    }

}
