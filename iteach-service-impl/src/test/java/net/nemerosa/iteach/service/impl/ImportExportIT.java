package net.nemerosa.iteach.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import net.nemerosa.iteach.it.AbstractITTestSupport;
import net.nemerosa.iteach.service.ImportExportService;
import net.nemerosa.iteach.service.TeacherService;
import net.nemerosa.iteach.service.model.*;
import org.joda.money.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Test for the import / export - same version.
 */
public class ImportExportIT extends AbstractITTestSupport {

    @Autowired
    private ServiceITSupport serviceITSupport;

    @Autowired
    private ImportExportService importExportService;

    @Autowired
    private TeacherService teacherService;

    @Test
    public void export_import_contract() throws Exception {

        // Data to export from
        int teacher1 = serviceITSupport.createTeacherAndCompleteRegistration();
        // School with contract
        int school11 = serviceITSupport.createSchool(teacher1);
        // Creates a contract for the school
        int contractId = serviceITSupport.asTeacher(teacher1, () ->
                        teacherService.createContract(school11, new ContractForm(
                                "Contract1",
                                Money.parse("EUR 50"),
                                BigDecimal.valueOf(12L)
                        )).getId()
        );
        // Creates a student for this contract
        serviceITSupport.asTeacher(teacher1, () -> teacherService.createStudent(new StudentForm(
                school11,
                contractId,
                "Student with contract",
                "Some subject",
                "Postal address",
                "Phone",
                "Mobile phone",
                "Email"
        )));

        // Date to import into
        int teacher2 = serviceITSupport.createTeacherAndCompleteRegistration();

        // Exporting
        JsonNode data = serviceITSupport.asAdmin(() -> importExportService.exportFile(teacher1));

        // Importing
        serviceITSupport.asAdmin(() -> {
            importExportService.importFile(teacher2, data);
            return null;
        });

        // Checks the imported student and its contract
        serviceITSupport.asTeacher(teacher2, () -> {
            School school = teacherService.getSchools().get(0);
            Contract contract = teacherService.getContracts(school.getId()).get(0);
            assertEquals("Contract1", contract.getName());
            assertEquals(Money.parse("EUR 50"), contract.getHourlyRate());
            assertEquals(BigDecimal.valueOf(12L), contract.getVatRate());
            Student student = teacherService.getStudentsForSchool(school.getId()).get(0);
            assertEquals("Student with contract", student.getName());
            assertEquals(Integer.valueOf(contract.getId()), student.getContractId());
            return null;
        });
    }

}
