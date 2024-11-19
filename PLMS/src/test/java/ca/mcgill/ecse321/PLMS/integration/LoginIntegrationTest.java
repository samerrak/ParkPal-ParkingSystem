package ca.mcgill.ecse321.PLMS.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.PLMS.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerResponseDto;
import ca.mcgill.ecse321.PLMS.dto.OwnerResponseDto;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.Owner;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;

// Start the app for real so that we can send requests to it, but use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Reuse the same class for all the tests (instead of creating a new class each time).
@TestInstance(Lifecycle.PER_CLASS)
// Ensure the tests are run in the right order (e.g., POST before GET)
@TestMethodOrder(OrderAnnotation.class)
public class LoginIntegrationTest {
    private class LoginFixture{
        public static String validOwnerEmail = "owner.owner@email.ca";
        public static String validOwnerPassword = "Owner!123";
        public static String validOwnerName = "Owner Name";

        public static String validMonthlyCustomerEmail = "customer.customer@email.ca";
        public static String validMonthlyCustomerPassword = "Customer!123";
        public static String validMonthlyCustomerName = "Customer Name";

        public static String validEmployeeEmail = "employee.employee@email.ca";
        public static String validEmployeePassword = "Employee!123";
        public static String validEmployeeName = "Employee Name";
        public static String validOwnerJobTitle = "Mechanic";
        public static double validOwnerHourlyWage = 25.25;
    }

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void clearDatabaseAndCreateAccounts(){
        ownerRepository.deleteAll();
        monthlyCustomerRepository.deleteAll();
        employeeRepository.deleteAll();

        ownerRepository.save(new Owner(LoginFixture.validOwnerEmail, LoginFixture.validOwnerPassword, LoginFixture.validOwnerName));
        monthlyCustomerRepository.save(new MonthlyCustomer(LoginFixture.validMonthlyCustomerEmail, LoginFixture.validMonthlyCustomerPassword, LoginFixture.validMonthlyCustomerName));
        employeeRepository.save(new Employee(LoginFixture.validEmployeeEmail, LoginFixture.validEmployeePassword, LoginFixture.validEmployeeName, LoginFixture.validOwnerJobTitle, LoginFixture.validOwnerHourlyWage));
        
    }
    
    @AfterAll
    public void clearDatabase(){
        ownerRepository.deleteAll();
        monthlyCustomerRepository.deleteAll();
        employeeRepository.deleteAll();
    }


    @Test
    @Order(0)
    public void testLoginAsValidOwner(){
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/login/Owner?email="+LoginFixture.validOwnerEmail+"&password="+LoginFixture.validOwnerPassword, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(LoginFixture.validOwnerEmail, response.getBody().getEmail());
        assertEquals(LoginFixture.validOwnerPassword, response.getBody().getPassword());
        assertEquals(LoginFixture.validOwnerName, response.getBody().getName());
    }

    @Test
    @Order(1)
    public void testLoginAsValidCustomer(){
        ResponseEntity<MonthlyCustomerResponseDto> response = client.getForEntity("/login/Customer?email="+LoginFixture.validMonthlyCustomerEmail+"&password="+LoginFixture.validMonthlyCustomerPassword, MonthlyCustomerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(LoginFixture.validMonthlyCustomerEmail, response.getBody().getEmail());
        assertEquals(LoginFixture.validMonthlyCustomerPassword, response.getBody().getPassword());
        assertEquals(LoginFixture.validMonthlyCustomerName, response.getBody().getName());
    }

    @Test
    @Order(2)
    public void testLoginAsValidEmployee(){
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/login/Employee?email="+LoginFixture.validEmployeeEmail+"&password="+LoginFixture.validEmployeePassword, EmployeeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(LoginFixture.validEmployeeEmail, response.getBody().getEmail());
        assertEquals(LoginFixture.validEmployeePassword, response.getBody().getPassword());
        assertEquals(LoginFixture.validEmployeeName, response.getBody().getName());
        assertEquals(LoginFixture.validOwnerJobTitle, response.getBody().getJobTitle());
        assertEquals(LoginFixture.validOwnerHourlyWage, response.getBody().getHourlyWage());
    }

    @Test
    @Order(3)
    public void testLoginAsInvalidOwner(){
        ResponseEntity<String> response = client.getForEntity("/login/Owner?email="+LoginFixture.validOwnerEmail+"&password=invalid", String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("Please enter the correct password",response.getBody());
    }

    @Test
    @Order(4)
    public void testLoginAsInvalidCustomer(){
        ResponseEntity<String> response = client.getForEntity("/login/Customer?email="+LoginFixture.validMonthlyCustomerEmail+"&password=invalid", String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("Please enter the correct password",response.getBody());
    }

    @Test
    @Order(5)
    public void testLoginAsInvalidEmployee(){
        ResponseEntity<String> response = client.getForEntity("/login/Employee?email="+LoginFixture.validEmployeeEmail+"&password=invalid", String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("Please enter the correct password",response.getBody());
    }

    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);
    }
}
