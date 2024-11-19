package ca.mcgill.ecse321.PLMS.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.sql.Time;
import java.util.List;
import java.util.Map;

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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.PLMS.dto.FloorRequestDto;
import ca.mcgill.ecse321.PLMS.dto.FloorResponseDto;
import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;

// Start the app for real so that we can send requests to it, but use a random port to avoid conflicts.
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// Reuse the same class for all the tests (instead of creating a new class each time).
@TestInstance(Lifecycle.PER_CLASS)
// Ensure the tests are run in the right order (e.g., POST before GET)
@TestMethodOrder(OrderAnnotation.class)
public class FloorIntegrationTests {

    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private TestRestTemplate client;
    @LocalServerPort
    private int port;

    /**
     * Creates a default valid floor object
     * @return default valid floor object
     */
    private class FixedValidFloor {

        public static final int floorNumber = 0;
        public static final boolean isMemberOnly = false;
        public static final int largeSpotCapacity = 10;
        public static final int smallSpotCapacity = 10;

        public static final boolean isMemberOnlyUpdated = true;
        public static final int largeSpotCapacityUpdated = 30;
        public static final int smallSpotCapacityUpdated = 40;

        public static final int secondFloorNumber = 2;

        public static Floor createValidFloor(){
            Floor validFloor = new Floor(floorNumber, largeSpotCapacity, smallSpotCapacity, isMemberOnly);
            return validFloor;
        }
        
    }

    /**
     * Method that transforms a floor object into a floorRequestDto
     * @param floor floor object that is going to be transformed into a floorRequestDto
     * @return the corresponding floorRequestDto
     */
    private FloorRequestDto floorToFloorRequestDto(Floor floor){
        
        FloorRequestDto floorRequestDto = new FloorRequestDto();

        floorRequestDto.setFloorNumber(floor.getFloorNumber());
        floorRequestDto.setIsMemberOnly(floor.getIsMemberOnly());
        floorRequestDto.setLargeSpotCapacity(floor.getLargeSpotCapacity());
        floorRequestDto.setSmallSpotCapacity(floor.getSmallSpotCapacity());

        return floorRequestDto;
    }

    @BeforeAll
	@AfterAll
	public void clearDatabase() {
		floorRepository.deleteAll();
		parkingLotRepository.deleteAll();
	}

    @Test
	@Order(0)
	public void testValidCreateFloor() {

        ParkingLot parkingLot = new ParkingLot(Time.valueOf("8:00:00"), Time.valueOf("20:00:00"), 15, 10, 250, 250);
        parkingLotRepository.save(parkingLot);

        Floor validFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(validFloor);

        ResponseEntity<FloorResponseDto> response = client.postForEntity("/floor", request, FloorResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        assertEquals(FixedValidFloor.floorNumber, response.getBody().getFloorNumber());
        assertEquals(FixedValidFloor.isMemberOnly, response.getBody().getIsMemberOnly());
        assertEquals(FixedValidFloor.largeSpotCapacity, response.getBody().getLargeSpotCapacity());
        assertEquals(FixedValidFloor.smallSpotCapacity, response.getBody().getSmallSpotCapacity());
    }

    @Test
    @Order(1)
    public void testGetValidFloor(){
        int floorNumber = FixedValidFloor.floorNumber;

        ResponseEntity<FloorResponseDto> response = client.getForEntity("/floor/" + floorNumber, FloorResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(FixedValidFloor.floorNumber, response.getBody().getFloorNumber());
        assertEquals(FixedValidFloor.isMemberOnly, response.getBody().getIsMemberOnly());
        assertEquals(FixedValidFloor.largeSpotCapacity, response.getBody().getLargeSpotCapacity());
        assertEquals(FixedValidFloor.smallSpotCapacity, response.getBody().getSmallSpotCapacity());
    }

    @Test
    @Order(2)
    public void testCreateDuplicateFloor(){
        //The valid floor is already in the database,
        //so creating another valid floor will be like creating a duplicate floor.
        Floor duplicatedFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(duplicatedFloor);

        ResponseEntity<String> response = client.postForEntity("/floor", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Floor with floor number: " + FixedValidFloor.floorNumber + " already exists." , response.getBody());
    }

    @Test
    @Order(3)
    public void testCreateFloorWithNullParamaters(){
        FloorRequestDto request = new FloorRequestDto();

        ResponseEntity<String> response = client.postForEntity("/floor", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Cannot have an empty floor number." , response.getBody());
        assertContains("Cannot have an empty number of small spots on a floor." , response.getBody());
        assertContains("Cannot have an empty number of large spots on a floor." , response.getBody());
        assertContains("Member only must be true or false." , response.getBody());
    }

    @Test
    @Order(4)
    public void testCreateFloorWithNegativeParameters(){
        
        Floor validFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(validFloor);

        request.setFloorNumber(-1);
        request.setLargeSpotCapacity(-1);
        request.setSmallSpotCapacity(-1);

        ResponseEntity<String> response = client.postForEntity("/floor", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("The floor number must be a non negative number." , response.getBody());
        assertContains("Cannot be a negative number of small parking spots on a floor." , response.getBody());
        assertContains("Cannot be a negative number of large parking spots on a floor." , response.getBody());
    }

    @Test
    @Order(5)
    public void testModifyFloorWithValidParameters(){

        Floor validFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(validFloor);

        request.setLargeSpotCapacity(FixedValidFloor.largeSpotCapacityUpdated);
        request.setSmallSpotCapacity(FixedValidFloor.smallSpotCapacityUpdated);
        request.setIsMemberOnly(FixedValidFloor.isMemberOnlyUpdated);

        HttpEntity<FloorRequestDto> requestEntity = new HttpEntity<>(request);

        ResponseEntity<FloorResponseDto> response = client.exchange(createURLWithPort("/floor"), HttpMethod.PUT, requestEntity, FloorResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        assertEquals(FixedValidFloor.floorNumber, response.getBody().getFloorNumber());
        assertEquals(FixedValidFloor.isMemberOnlyUpdated, response.getBody().getIsMemberOnly());
        assertEquals(FixedValidFloor.largeSpotCapacityUpdated, response.getBody().getLargeSpotCapacity());
        assertEquals(FixedValidFloor.smallSpotCapacityUpdated, response.getBody().getSmallSpotCapacity());
    }

    @Test
    @Order(6)
    public void testGetModifiedFloor(){
        int floorNumber = FixedValidFloor.floorNumber;

        ResponseEntity<FloorResponseDto> response = client.getForEntity("/floor/" + floorNumber, FloorResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(FixedValidFloor.floorNumber, response.getBody().getFloorNumber());
        assertEquals(FixedValidFloor.isMemberOnlyUpdated, response.getBody().getIsMemberOnly());
        assertEquals(FixedValidFloor.largeSpotCapacityUpdated, response.getBody().getLargeSpotCapacity());
        assertEquals(FixedValidFloor.smallSpotCapacityUpdated, response.getBody().getSmallSpotCapacity());
    }

    @Test
    @Order(7)
    public void testModifyFloorWithNullParameters(){
        FloorRequestDto request = new FloorRequestDto();

        HttpEntity<FloorRequestDto> requestEntity = new HttpEntity<>(request);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor"), HttpMethod.PUT, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Cannot have an empty floor number." , response.getBody());
        assertContains("Cannot have an empty number of small spots on a floor." , response.getBody());
        assertContains("Cannot have an empty number of large spots on a floor." , response.getBody());
        assertContains("Member only must be true or false." , response.getBody());
    }

    @Test
    @Order(8)
    public void testModifyFloorWithNegativeParameters(){
        FloorRequestDto request = new FloorRequestDto();

        request.setFloorNumber(-1);
        request.setLargeSpotCapacity(-1);
        request.setSmallSpotCapacity(-1);

        HttpEntity<FloorRequestDto> requestEntity = new HttpEntity<>(request);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor"), HttpMethod.PUT, requestEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("The floor number must be a non negative number." , response.getBody());
        assertContains("Cannot be a negative number of small parking spots on a floor." , response.getBody());
        assertContains("Cannot be a negative number of large parking spots on a floor." , response.getBody());
    }

    @Test
    @Order(11)
    public void testCreateSecondValidFloor(){
        Floor validFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(validFloor);

        request.setFloorNumber(FixedValidFloor.secondFloorNumber);

        ResponseEntity<FloorResponseDto> response = client.postForEntity("/floor", request, FloorResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        assertEquals(FixedValidFloor.secondFloorNumber, response.getBody().getFloorNumber());
        assertEquals(FixedValidFloor.isMemberOnly, response.getBody().getIsMemberOnly());
        assertEquals(FixedValidFloor.largeSpotCapacity, response.getBody().getLargeSpotCapacity());
        assertEquals(FixedValidFloor.smallSpotCapacity, response.getBody().getSmallSpotCapacity());
    }

    @Test
    @Order(12)
    public void testGetValidFloors(){

        ResponseEntity<List> response = client.getForEntity("/floor", List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Map<String, Object>> responseBody = response.getBody();
        
        assertEquals(2, responseBody.size());
        
        assertEquals(FixedValidFloor.floorNumber, responseBody.get(0).get("floorNumber"));
        assertEquals(FixedValidFloor.isMemberOnlyUpdated, responseBody.get(0).get("isMemberOnly"));
        assertEquals(FixedValidFloor.largeSpotCapacityUpdated, responseBody.get(0).get("largeSpotCapacity"));
        assertEquals(FixedValidFloor.smallSpotCapacityUpdated, responseBody.get(0).get("smallSpotCapacity"));

        assertEquals(FixedValidFloor.secondFloorNumber, responseBody.get(1).get("floorNumber"));
        assertEquals(FixedValidFloor.isMemberOnly, responseBody.get(1).get("isMemberOnly"));
        assertEquals(FixedValidFloor.largeSpotCapacity, responseBody.get(1).get("largeSpotCapacity"));
        assertEquals(FixedValidFloor.smallSpotCapacity, responseBody.get(1).get("smallSpotCapacity"));
    }

    @Test
    @Order(13)
    public void testDeleteFloorThatDoesNotExist(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor/"+Integer.MAX_VALUE), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("Floor with floor number: " + Integer.MAX_VALUE + " does not exist." , response.getBody());
    }

    @Test
    @Order(14)
    public void testDeleteFloor(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor/"+FixedValidFloor.floorNumber), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Test if the getter of all floors reads one less floor
        ResponseEntity<List> getResponse = client.getForEntity("/floor", List.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());

        List<Map<String, Object>> responseBody = getResponse.getBody();
        
        assertEquals(1, responseBody.size());
    }

    @Test
    @Order(15)
    public void testGetInvalidFloor(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor/"+FixedValidFloor.floorNumber), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("Floor with floor number: " + FixedValidFloor.floorNumber + " does not exist.", response.getBody());
    }

    @Test
    @Order(16)
    public void testDeleteLastFloor(){
        HttpEntity<String> requestEntity = new HttpEntity<>(null);

        ResponseEntity<String> response = client.exchange(createURLWithPort("/floor/"+FixedValidFloor.secondFloorNumber), HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        //Test if the getter of all floors reads one less floor
        ResponseEntity<String> getResponse = client.getForEntity("/floor", String.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        
        assertEquals("There are no floors in the system.", getResponse.getBody());
    }


    @Test
    @Order(17)
    public void testCreateFloorWithNoParkingLot(){
        //This test has to be passed only after the delete test is passed
        parkingLotRepository.deleteAll();

        Floor validFloor = FixedValidFloor.createValidFloor();
        FloorRequestDto request = floorToFloorRequestDto(validFloor);

        ResponseEntity<String> response = client.postForEntity("/floor", request, String.class);

        // assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Cannot create floor since the parking lot has not been created." , response.getBody());
    }

    private void assertContains(String expected, String actual) {
		String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
		assertTrue(actual.contains(expected), assertionMessage);
	}

    private URI createURLWithPort(String endpoint){
        String baseUrl = "http://localhost:" + port + endpoint;
        URI uri = URI.create(baseUrl);

        return uri;
    }
}
