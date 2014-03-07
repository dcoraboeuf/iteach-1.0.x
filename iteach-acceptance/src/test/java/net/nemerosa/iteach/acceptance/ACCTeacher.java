package net.nemerosa.iteach.acceptance;

import net.nemerosa.iteach.acceptance.support.AbstractACCSupport;
import net.nemerosa.iteach.acceptance.support.TeacherContext;
import org.junit.Test;

public class ACCTeacher extends AbstractACCSupport {

    @Test
    public void create_a_school() {
        // Prerequisites
        TeacherContext teacherContext = support.doCreateTeacher();
        // Data
        // final String schoolName = uid("SCH");
        // Creates a school for this teacher
        /*
        UISchool school = support.asTeacher(teacher, new TeacherCall<UISchool>() {
            @Override
            public UISchool call(UITeacherAPIClient client) {
                return client.createSchool(
                        UIForm.create()
                                .withName(schoolName)
                                .withColour("#FFFF00")
                                .with("hourlyRate", Money.parse("EUR 45.00"))
                                .withPostalAddress("Rue des Professeurs 16\n1100 Brussels\nBelgique")
                );
            }
        });
        // Checks the fields for the school
        assertNotNull(school);
        assertTrue(school.getId() > 0);
        assertEquals(schoolName, school.getName());
        assertEquals("#FFFF00", school.getColour());
        assertEquals("EUR", school.getHourlyRate().getCurrencyUnit().getCode());
        assertEquals(new BigDecimal("45.00"), school.getHourlyRate().getAmount());
        assertEquals("Rue des Professeurs 16\n1100 Brussels\nBelgique", school.getPostalAddress());
        */
    }

}
