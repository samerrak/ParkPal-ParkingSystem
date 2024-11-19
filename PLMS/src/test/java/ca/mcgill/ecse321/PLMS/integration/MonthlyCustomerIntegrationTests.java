package ca.mcgill.ecse321.PLMS.integration;


import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerRequestDto;
import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerResponseDto;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
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
public class MonthlyCustomerIntegrationTests {
    private class MonthlyCustomerFixture {
        public String name = "John";
        public String email = "john.doe@mcgill.ca";
        public String password = "Samer+2003";

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getName() {
            return name;
        }
    }

    private MonthlyCustomerFixture monthlyCustomerFixture;

    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;
    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture() {
        this.monthlyCustomerFixture = new MonthlyCustomerFixture();
    }

    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        monthlyCustomerRepository.deleteAll();
    }

    private MonthlyCustomerRequestDto setRequest(String email, String name, String password) {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();
        request.setName(name);
        request.setPassword(password);
        request.setEmail(email);
        return request;
    }

    private boolean equals(MonthlyCustomerResponseDto response, MonthlyCustomerFixture e) {
        boolean b = response.getEmail().equals(e.email);
        b = b & response.getName().equals(e.name);
        return b & response.getPassword().equals(e.password);
    }

    @Test
    @Order(0)
    public void testGetAllEmptyMonthlyCustomers() {
        ResponseEntity<String> response3 = client.getForEntity("/customers", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
        assertEquals(response3.getBody(), "There are no monthly customers in the system");
    }


    @Test
    @Order(1)
    public void testCreateMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();
        request.setName(monthlyCustomerFixture.name);
        request.setPassword(monthlyCustomerFixture.password);
        request.setEmail(monthlyCustomerFixture.email);

        ResponseEntity<MonthlyCustomerResponseDto> response = client.postForEntity("/customer/create", request, MonthlyCustomerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), monthlyCustomerFixture));
    }

    @Test
    @Order(2)
    public void testGetMonthlyCustomer() {
        ResponseEntity<MonthlyCustomerResponseDto> response = client.getForEntity("/customer?email=" + monthlyCustomerFixture.email, MonthlyCustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), monthlyCustomerFixture));
    }

    @Test
    @Order(3)
    public void testGetInvalidMonthlyCustomer() {
        String not_created = "jane.doe@mcgill.ca";
        ResponseEntity<String> response = client.getForEntity("/customer?email=" + not_created, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Monthly customer not found.");
    }


    @Test
    @Order(4)
    public void testCreateBlankMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();
        ResponseEntity<String> response = client.postForEntity("/customer/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email cannot be blank.", response.getBody());
        assertContains("Password cannot be blank.", response.getBody());
        assertContains("Name cannot be blank.", response.getBody());
    }


    @Test
    @Order(5)
    public void testCreateInvalidMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail("john");
        ResponseEntity<String> response = client.postForEntity("/customer/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email must follow this format xxx@email.address", response.getBody());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
    }

    @Test
    @Order(6)
    public void testUpdateValidMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();

        request.setName("Johnny");
        request.setPassword("John!Doe");
        request.setEmail(monthlyCustomerFixture.email);
        HttpEntity<MonthlyCustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<MonthlyCustomerResponseDto> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, MonthlyCustomerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), "Johnny");
        assertEquals(response.getBody().getEmail(), monthlyCustomerFixture.email);
        assertEquals(response.getBody().getPassword(), "John!Doe");
        monthlyCustomerFixture.setName("Johnny");
        monthlyCustomerFixture.setPassword("John!Doe");

    }

    @Test
    @Order(7)
    public void testUpdateInvalidMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail(monthlyCustomerFixture.email);
        HttpEntity<MonthlyCustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
    }


    @Test
    @Order(8)
    public void testUpdateInvalidEmailMonthlyCustomer() {
        MonthlyCustomerRequestDto request = new MonthlyCustomerRequestDto();

        request.setName(monthlyCustomerFixture.name);
        request.setPassword(monthlyCustomerFixture.password);
        request.setEmail("jane.doe@mcgill.ca");

        HttpEntity<MonthlyCustomerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/customer/update", HttpMethod.PUT, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Monthly customer not found.");
    }


    @Test
    @Order(9)
    public void testGetAllMonthlyCustomers() {

        MonthlyCustomerRequestDto request2 = new MonthlyCustomerRequestDto();
        request2.setName("Jane");
        request2.setPassword("Jane!2002");
        request2.setEmail("jane.doe@mcgill.ca");
        ResponseEntity<MonthlyCustomerResponseDto> response2 = client.postForEntity("/customer/create", request2, MonthlyCustomerResponseDto.class);

        ResponseEntity<List> response3 = client.getForEntity("/customers", List.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> MonthlyCustomers = response3.getBody();
        assertEquals(MonthlyCustomers.get(1).get("name"), "Jane");
        assertEquals(MonthlyCustomers.get(1).get("password"), "Jane!2002");
        assertEquals(MonthlyCustomers.get(1).get("email"), "jane.doe@mcgill.ca");

        assertEquals(MonthlyCustomers.get(0).get("name"), monthlyCustomerFixture.name);
        assertEquals(MonthlyCustomers.get(0).get("password"), monthlyCustomerFixture.password);
        assertEquals(MonthlyCustomers.get(0).get("email"), monthlyCustomerFixture.email);
    }

    @Test
    @Order(10)
    public void testCreateDuplicateCustomer() {
        MonthlyCustomerRequestDto request2 = new MonthlyCustomerRequestDto();
        request2.setName("Jane");
        request2.setPassword("Jane!2002");
        request2.setEmail("jane.doe@mcgill.ca");
        ResponseEntity<String> response2 = client.postForEntity("/customer/create", request2, String.class);
        assertEquals(response2.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(response2.getBody(), "Another account with this email already exists");


    }




    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);
    }

}


