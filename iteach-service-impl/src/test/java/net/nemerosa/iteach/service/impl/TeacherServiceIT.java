package net.nemerosa.iteach.service.impl;

import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.TeacherService;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TeacherServiceIT extends AbstractITTestSupport {

    @Autowired
    public TeacherService teacherService;

    @Test
    @Ignore
    public void create_school() {
        // TODO Creates a teacher
        // TODO Creates a school for this teacher
    }

}
