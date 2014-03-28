package net.nemerosa.iteach.acceptance;

import net.nemerosa.iteach.acceptance.support.AbstractACCSupport;
import net.nemerosa.iteach.acceptance.support.TeacherContext;
import net.nemerosa.iteach.ui.client.support.ClientValidationException;
import net.nemerosa.iteach.ui.model.UISchool;
import net.nemerosa.iteach.ui.model.UISchoolForm;
import org.apache.commons.lang3.StringUtils;
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
                        "",
                        ""
                ),
                "School name may not be null"
        );
    }

    @Test
    public void create_a_school_validation_name_fr() {
        validate_school(Locale.FRENCH,
                new UISchoolForm(
                        null,
                        "#FFFF00",
                        "",
                        "EUR 45.00",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        "",
                        ""
                ),
                "Nom de l'école : ne doit pas être nul"
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
                        "",
                        ""
                ),
                "School colour must be a hexadecimal colour code like #AA66CC"
        );
    }

    @Test
    public void create_a_school_validation_hourly_rate() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        "Some name",
                        "#FFFFFF",
                        "",
                        "EUR",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        "",
                        ""
                ),
                "School hourly rate must be like EUR 45.00 or 45.00 (defaults to EUR)"
        );
    }

    @Test
    public void create_a_school_validation_name_too_long() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        StringUtils.repeat("x", 81),
                        "#FFFFFF",
                        "",
                        "",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        "",
                        ""
                ),
                "School name size must be between 1 and 80"
        );
    }

    @Test
    public void create_a_school_validation_name_too_long_fr() {
        validate_school(
                Locale.FRENCH,
                new UISchoolForm(
                        StringUtils.repeat("x", 81),
                        "#FFFFFF",
                        "",
                        "",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "",
                        "",
                        ""
                ),
                "Nom de l'école : le nombre de caractères doit être entre 1 et 80"
        );
    }

    @Test
    public void create_a_school_validation_email() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        "Any name",
                        "#FFFFFF",
                        "",
                        "",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "www",
                        "",
                        ""
                ),
                "School email is not a well-formed email address"
        );
    }

    @Test
    public void create_a_school_validation_webSite() {
        validate_school(Locale.ENGLISH,
                new UISchoolForm(
                        "Any name",
                        "#FFFFFF",
                        "",
                        "",
                        "Rue des Professeurs 16\n1100 Brussels\nBelgique",
                        "",
                        "",
                        "info@school.com",
                        "xxxx",
                        ""
                ),
                "School web site must be a valid URL"
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
