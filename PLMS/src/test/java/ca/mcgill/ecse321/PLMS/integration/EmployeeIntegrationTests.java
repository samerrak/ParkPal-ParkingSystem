package ca.mcgill.ecse321.PLMS.integration;


import ca.mcgill.ecse321.PLMS.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.PLMS.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Reuse the same class for all the tests (instead of creating a new class each time).
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Ensure the tests are run in the right order (e.g., POST before GET)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeIntegrationTests {

    private class EmployeeFixture {
        public String name = "John";
        public String email = "john.doe@mcgill.ca";
        public String password = "Samer+2003";
        public String jobTitle = "Mechanic";
        public Double hourlyWage = 17.5;

        public void setEmail(String email) { this.email = email; }
        public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
        public void setHourlyWage(Double hourlyWage) { this.hourlyWage = hourlyWage; }
        public void setPassword(String password) { this.password = password; }
        public Double getHourlyWage() { return hourlyWage; }
        public String getJobTitle() { return jobTitle; }
        public String getEmail() { return email; }
        public String getPassword() { return  password; }
    }

    private EmployeeFixture employeeFixture;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture() { this.employeeFixture = new EmployeeFixture(); }

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        employeeRepository.deleteAll();
    }

    private EmployeeRequestDto setRequest(String email, String name, String password, String jobTitle, Double hourlyWage) {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(hourlyWage);
        request.setJobTitle(jobTitle);
        request.setName(name);
        request.setPassword(password);
        request.setEmail(email);
        return request;
    }

    private boolean equals(EmployeeResponseDto response, EmployeeFixture e)
    {
        boolean b = response.getEmail().equals(e.email);
        b = b & response.getName().equals(e.name);
        b = b & response.getPassword().equals(e.password);
        b = b & (response.getHourlyWage() == e.hourlyWage);
        return b & (response.getJobTitle().equals(e.jobTitle));
    }


    @Test
    @Order(0)
    public void testCreateEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(employeeFixture.hourlyWage);
        request.setJobTitle(employeeFixture.jobTitle);
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail(employeeFixture.email);

        ResponseEntity<EmployeeResponseDto> response =  client.postForEntity("/employee/create", request, EmployeeResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), employeeFixture));
    }

    @Test
    @Order(1)
    public void testGetEmployee() {
        ResponseEntity<EmployeeResponseDto> response = client.getForEntity("/employee?email=" + employeeFixture.email, EmployeeResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), employeeFixture));
    }

    @Test
    @Order(2)
    public void testGetInvalidEmployee() {
        String not_created = "jane.doe@mcgill.ca";
        ResponseEntity<String> response =  client.getForEntity("/employee?email=" + not_created, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Employee not found.");
    }


    @Test
    @Order(3)
    public void testCreateBlankEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        ResponseEntity<String> response =  client.postForEntity("/employee/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email cannot be blank.", response.getBody());
        assertContains("Password cannot be blank.", response.getBody());
        assertContains("Name cannot be blank.", response.getBody());
        assertContains("Job title must not be blank", response.getBody());
    }

    @Test
    @Order(4)
    public void testCreateInvalidWageEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(-10);
        request.setJobTitle(employeeFixture.jobTitle);
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail(employeeFixture.email);
        ResponseEntity<String> response =  client.postForEntity("/employee/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Hourly wage must be positive.", response.getBody());
    }

    @Test
    @Order(5)
    public void testCreateInvalidEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setJobTitle("12");
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail("john");
        ResponseEntity<String> response =  client.postForEntity("/employee/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email must follow this format xxx@email.address", response.getBody());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
        assertContains("Job title can only have letters", response.getBody());
    }

    @Test
    @Order(6)
    public void testUpdateValidEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(13);
        request.setJobTitle("Cashier");
        request.setName("Jane");
        request.setPassword("John!Doe");
        request.setEmail(employeeFixture.email);
        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<EmployeeResponseDto> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity , EmployeeResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getName());
        assertEquals(employeeFixture.email, response.getBody().getEmail());
        assertEquals("John!Doe", response.getBody().getPassword());
        assertEquals("Cashier", response.getBody().getJobTitle());
        assertEquals(13, response.getBody().getHourlyWage());
    }

    @Test
    @Order(7)
    public void testUpdateInvalidEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setJobTitle("12");
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail(employeeFixture.email);
        request.setHourlyWage(12);

        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity , String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
        assertContains("Job title can only have letters", response.getBody());
    }

    @Test
    @Order(8)
    public void testUpdateInvalidWageEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(-10);
        request.setJobTitle(employeeFixture.jobTitle);
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail(employeeFixture.email);

        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity , String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Hourly wage must be positive.", response.getBody());
    }

    @Test
    @Order(9)
    public void testUpdateInvalidEmailEmployee() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setHourlyWage(10);
        request.setJobTitle(employeeFixture.jobTitle);
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail("jane.doe@mcgill.ca");

        HttpEntity<EmployeeRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/employee/update", HttpMethod.PUT, requestEntity , String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found.", response.getBody());
    }


    @Test
    @Order(10)
    public void testDeleteValidEmployee() {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/employee/delete/" + employeeFixture.email, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 =  client.getForEntity("/employee?email=" + employeeFixture.email, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals("Employee not found.", response2.getBody());
    }

    @Test
    @Order(11)
    public void testDeleteInvalidEmployee() {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/employee/delete/" + employeeFixture.email, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Employee not found.", response.getBody());
    }

    @Test
    @Order(12)
    public void testGetAllEmptyEmployees() {
        ResponseEntity<String> response3 = client.getForEntity("/employees", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
        assertEquals("There are no employees in the system", response3.getBody());
    }

    @Test
    @Order(13)
    public void testGetAllEmployees() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail(employeeFixture.email);
        request.setHourlyWage(employeeFixture.hourlyWage);
        request.setJobTitle(employeeFixture.jobTitle);
        ResponseEntity<EmployeeResponseDto> response =  client.postForEntity("/employee/create", request, EmployeeResponseDto.class);

        EmployeeRequestDto request2 = new EmployeeRequestDto();
        request2.setName("Jane");
        request2.setPassword("Jane!2002");
        request2.setEmail("jane.doe@mcgill.ca");
        request2.setJobTitle("Cashier");
        request2.setHourlyWage(27.0);
        ResponseEntity<EmployeeResponseDto> response2 =  client.postForEntity("/employee/create", request2, EmployeeResponseDto.class);

        ResponseEntity<List> response3 = client.getForEntity("/employees", List.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> employees = response3.getBody();
        assertEquals(employees.get(1).get("name"), "Jane");
        assertEquals(employees.get(1).get("password"), "Jane!2002");
        assertEquals(employees.get(1).get("email"), "jane.doe@mcgill.ca");
        assertEquals(employees.get(1).get("jobTitle"), "Cashier");
        assertEquals(employees.get(1).get("hourlyWage"), 27.0);

        assertEquals(employees.get(0).get("name"), employeeFixture.name);
        assertEquals(employees.get(0).get("password"), employeeFixture.password);
        assertEquals(employees.get(0).get("email"), employeeFixture.email);
        assertEquals(employees.get(0).get("jobTitle"), employeeFixture.jobTitle);
        assertEquals(employees.get(0).get("hourlyWage"), employeeFixture.hourlyWage);
    }

    @Test
    @Order(14)
    public void testCreateDuplicateEmployee() {

        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setName(employeeFixture.name);
        request.setPassword(employeeFixture.password);
        request.setEmail(employeeFixture.email);
        request.setHourlyWage(employeeFixture.hourlyWage);
        request.setJobTitle(employeeFixture.jobTitle);
        ResponseEntity<String> response =  client.postForEntity("/employee/create", request, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(response.getBody(), "Another account with this email already exists");


    }





    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);
    }



 }
