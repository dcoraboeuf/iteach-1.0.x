package net.nemerosa.iteach.acceptance;

import net.nemerosa.iteach.acceptance.support.AbstractACCSupport;
import net.nemerosa.iteach.acceptance.support.TeacherContext;
import net.nemerosa.iteach.ui.client.support.ClientValidationException;
import net.nemerosa.iteach.ui.model.UISchool;
import net.nemerosa.iteach.ui.model.UISchoolForm;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Locale;

import static net.nemerosa.iteach.test.TestUtils.uid;
import static org.junit.Assert.*;

public class ACCTeacher extends AbstractACCSupport {

    @Test
    public void create_a_school() {
        // Prerequisites
        TeacherContext teacherContext = support.doCreateTeacher();
        // Data
        final String schoolName = uid("SCH");
        // Creates a school for this teacher
        UISchool school = support.client().teacher().asTeacher(teacherContext).call(client ->
                client.createSchool(
                        Locale.ENGLISH,
                        new UISchoolForm(
                                schoolName,
                                "#FFFF00",
                                "",
                                "EUR 45.00",
                                "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                                "",
                                "",
                                "",
                                ""
                        )
                )
        );
        // Checks the fields for the school
        assertNotNull(school);
        assertTrue(school.getId() > 0);
        assertEquals(schoolName, school.getName());
        assertEquals("#FFFF00", school.getColour());
        assertEquals("EUR", school.getHourlyRate().getCurrencyUnit().getCode());
        assertEquals(new BigDecimal("45.00"), school.getHourlyRate().getAmount());
        assertEquals("Rue des Professeurs 16\n1100 Brussels\nBelgique", school.getPostalAddress());
    }

    @Test
    public void create_a_school_validation_name_null() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        null,
                        "#FFFF00",
                        "",
                        "EUR 45.00",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        ""
                ),
                "School name may not be null"
        );
    }

    @Test
    public void create_a_school_validation_colour() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        "Some name",
                        "#FFF",
                        "",
                        "EUR 45.00",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        ""
                ),
                "School colour must be a hexadecimal colour code like #AA66CC"
        );
    }

    private void validate_school(Locale locale, UISchoolForm form, String expectedValidationMessage) {
        // Prerequisites
        TeacherContext teacherContext = support.doCreateTeacher();
        // Creates a school for this teacher
        try {
            support.client().teacher().asTeacher(teacherContext).call(client ->
                    client.createSchool(
                            locale,
                            form
                    )
            );
            fail("Validation error expected");
        } catch (ClientValidationException ex) {
            assertEquals(expectedValidationMessage, ex.getMessage());
        }
    }

}
