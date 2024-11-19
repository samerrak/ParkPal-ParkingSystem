package ca.mcgill.ecse321.PLMS.integration;


import ca.mcgill.ecse321.PLMS.dto.OwnerRequestDto;
import ca.mcgill.ecse321.PLMS.dto.OwnerResponseDto;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;
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
public class OwnerIntegrationTests {
    private class OwnerFixture {
        public String name = "John";
        public String email = "john.doe@mcgill.ca";
        public String password = "Samer+2003";

        public void setEmail(String email) { this.email = email; }
        public void setPassword(String password) { this.password = password; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public String getPassword() { return  password; }
        public String getName() { return name; }
    }

    private OwnerFixture ownerFixture;

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture() { this.ownerFixture = new OwnerFixture(); }

    @BeforeAll
    @AfterAll
    public void clearDatabase() { ownerRepository.deleteAll();}

    private OwnerRequestDto setRequest(String email, String name, String password) {
        OwnerRequestDto request = new OwnerRequestDto();
        request.setName(name);
        request.setPassword(password);
        request.setEmail(email);
        return request;
    }

    private boolean equals(OwnerResponseDto response, OwnerFixture e)
    {
        boolean b = response.getEmail().equals(e.email);
        b = b & response.getName().equals(e.name);
        return  b & response.getPassword().equals(e.password);
    }

    @Test
    @Order(0)
    public void testGetAllEmptyOwners() {
        ResponseEntity<String> response3 = client.getForEntity("/owners", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
        assertEquals(response3.getBody(),  "There are no owners in the system");
    }


    @Test
    @Order(1)
    public void testCreateOwner() {
        OwnerRequestDto request = new OwnerRequestDto();
        request.setName(ownerFixture.name);
        request.setPassword(ownerFixture.password);
        request.setEmail(ownerFixture.email);

        ResponseEntity<OwnerResponseDto> response =  client.postForEntity("/owner/create", request, OwnerResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), ownerFixture));
    }

    @Test
    @Order(2)
    public void testGetOwner() {
        ResponseEntity<OwnerResponseDto> response = client.getForEntity("/owner?email=" + ownerFixture.email, OwnerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(equals(response.getBody(), ownerFixture));
    }

    @Test
    @Order(3)
    public void testGetInvalidOwner() {
        String not_created = "jane.doe@mcgill.ca";
        ResponseEntity<String> response =  client.getForEntity("/owner?email=" + not_created, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Owner not found.");
    }


    @Test
    @Order(4)
    public void testCreateBlankOwner() {
        OwnerRequestDto request = new OwnerRequestDto();
        ResponseEntity<String> response =  client.postForEntity("/owner/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email cannot be blank.", response.getBody());
        assertContains("Password cannot be blank.", response.getBody());
        assertContains("Name cannot be blank.", response.getBody());
    }



    @Test
    @Order(5)
    public void testCreateInvalidOwner() {
        OwnerRequestDto request = new OwnerRequestDto();
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail("john");
        ResponseEntity<String> response =  client.postForEntity("/owner/create", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Email must follow this format xxx@email.address", response.getBody());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
    }

    @Test
    @Order(6)
    public void testUpdateValidOwner() {
        OwnerRequestDto request = new OwnerRequestDto();

        request.setName("Johnny");
        request.setPassword("John!Doe");
        request.setEmail(ownerFixture.email);
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<OwnerResponseDto> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity , OwnerResponseDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getName(), "Johnny");
        assertEquals(response.getBody().getEmail(), ownerFixture.email);
        assertEquals(response.getBody().getPassword(),"John!Doe");
        ownerFixture.setName("Johnny");
        ownerFixture.setPassword("John!Doe");

    }

    @Test
    @Order(7)
    public void testUpdateInvalidOwner() {
        OwnerRequestDto request = new OwnerRequestDto();
        request.setName("12");
        request.setPassword("dddddddddddddd");
        request.setEmail(ownerFixture.email);
        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity , String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Password contains at least one uppercase, lowercase and special character [!@#$%^+=]", response.getBody());
        assertContains("Password must have 5-13 character", response.getBody());
        assertContains("Name can only have letters", response.getBody());
    }



    @Test
    @Order(8)
    public void testUpdateInvalidEmailOwner() {
        OwnerRequestDto request = new OwnerRequestDto();

        request.setName(ownerFixture.name);
        request.setPassword(ownerFixture.password);
        request.setEmail("jane.doe@mcgill.ca");

        HttpEntity<OwnerRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = client.exchange("/owner/update", HttpMethod.PUT, requestEntity , String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Owner not found.");
    }



    @Test
    @Order(9)
    public void testGetAllOwners() {
        OwnerRequestDto request2 = new OwnerRequestDto();
        request2.setName("Jane");
        request2.setPassword("Jane!2002");
        request2.setEmail("jane.doe@mcgill.ca");
        ResponseEntity<OwnerResponseDto> response2 =  client.postForEntity("/owner/create", request2, OwnerResponseDto.class);

        ResponseEntity<List> response3 = client.getForEntity("/owners", List.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> owners = response3.getBody();
        assertEquals(owners.get(1).get("name"), "Jane");
        assertEquals(owners.get(1).get("password"), "Jane!2002");
        assertEquals(owners.get(1).get("email"), "jane.doe@mcgill.ca");

        assertEquals(owners.get(0).get("name"), ownerFixture.name);
        assertEquals(owners.get(0).get("password"), ownerFixture.password);
        assertEquals(owners.get(0).get("email"), ownerFixture.email);
    }

    @Test
    @Order(10)
    public void testCreateDuplicateOwner() {
        OwnerRequestDto request2 = new OwnerRequestDto();
        request2.setName("Jane");
        request2.setPassword("Jane!2002");
        request2.setEmail("jane.doe@mcgill.ca");
        ResponseEntity<String> response2 = client.postForEntity("/owner/create", request2, String.class);
        assertEquals(response2.getStatusCode(), HttpStatus.CONFLICT);
        assertEquals(response2.getBody(), "Another account with this email already exists");
    }


    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);
    }



}
