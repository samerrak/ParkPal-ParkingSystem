package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.PLMS.model.*;

/**
 * Testing suite for the persistence of the Employee class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class EmployeeRepositoryTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        employeeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadEmployee(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        String email = "jeff.jeff@jeff.com";
        String password = "PasswordSuperSecured12345";
        String name = "Jeff";
        String jobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
        int hourlyWage = 15;
        Employee jeff = new Employee();
        //Set all parameters
        jeff.setEmail(email);
        jeff.setPassword(password);
        jeff.setName(name);
        jeff.setJobTitle(jobDescription);
        jeff.setHourlyWage(hourlyWage);

        //=-=-=-=-=-=- Save employee -=-=-=-=-=-=//
        jeff = employeeRepository.save(jeff);
        String emailOfJeff = jeff.getEmail();

        //=-=-=-=-=-=- Read employee -=-=-=-=-=-=//
        jeff = employeeRepository.findEmployeeByEmail(emailOfJeff);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(jeff);
        assertEquals(email, jeff.getEmail());
        assertEquals(password, jeff.getPassword());
        assertEquals(name, jeff.getName());
        assertEquals(jobDescription, jeff.getJobTitle());
        assertEquals(hourlyWage, jeff.getHourlyWage());
    }
}
