package ca.mcgill.ecse321.PLMS.integration;

import ca.mcgill.ecse321.PLMS.dto.*;
import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.GuestPassRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Reuse the same class for all the tests (instead of creating a new class each time).
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// Ensure the tests are run in the right order (e.g., POST before GET)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GuestPassIntegrationTests {

    private class GuestPassFixture {
        public Integer id;
        public final static String spotNumber = "A24";
        public final static String confirmationCode = "NeverGonnaGiveYouUp";
        public final static String licensePlate = "12345678";
        public final static Boolean isLarge = true;
        public final static Integer floorNumber = 0;
        public final static Integer nrFifteenMinuteIntervals = 3;

        // Second Guest Pass
        public final static String spotNumber2 = "B24";

        public static GuestPassRequestDto createValidGuestPass(){
            GuestPassRequestDto request = new GuestPassRequestDto();

            request.setSpotNumber(spotNumber);
            request.setConfirmationCode(confirmationCode);
            request.setLicensePlate(licensePlate);
            request.setIsLarge(isLarge);
            request.setFloorNumber(floorNumber);
            request.setNumberOfFifteenMinuteIncrements(nrFifteenMinuteIntervals);

            return request;
        }

    }

    private class FixedValidFloor {

        public static final int floorNumber = 0;
        public static final boolean isMemberOnly = false;
        public static final int largeSpotCapacity = 10;
        public static final int smallSpotCapacity = 10;

        public static final boolean isMemberOnly2 = true;
        public static final int largeSpotCapacity2 = 30;
        public static final int smallSpotCapacity2 = 40;

        public static final int floorNumber2 = 2;

        public static Floor createValidFloor() {
            Floor validFloor = new Floor(floorNumber, largeSpotCapacity, smallSpotCapacity, isMemberOnly);
            return validFloor;
        }

    }
//     private boolean equals(GuestPassResponseDto response, GuestPassFixture e){
//         boolean b = response.getSpotNumber().equals(e.spotNumber);
//         b = b & response.getConfirmationCode().equals(e.confirmationCode);
//         b = b & response.getLicensePlate().equals(e.licensePlate);
//         b = b & response.getIsLarge().equals(e.isLarge);
//         b = b & response.getStart().equals(e.startDate);
//         b = b & response.getEndDate().equals(e.endDate);
//         b = b & response.getFloorNumber().equals(e.floorNumber);
//         return b & response.getMonthlyCustomerEmail().equals(e.monthlyCustomerEmail);
//     }

    private GuestPassFixture guestPassFixture;

    private ParkingLot parkingLot;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private GuestPassRepository guestPassRepository;

    @Autowired
    private TestRestTemplate client;


    @BeforeAll
    @AfterAll
    public void clearDatabase() {
        floorRepository.deleteAll();
        parkingLotRepository.deleteAll();
        guestPassRepository.deleteAll();
    }

    @BeforeAll
    public void setupTestFixture(){
        this.guestPassFixture = new GuestPassFixture();
    }

    @BeforeEach
    public void setUp(){
        ParkingLot parkingLot = new ParkingLot(Time.valueOf("0:00:00"), Time.valueOf("23:59:59"), 15, 10, 250, 250);
        parkingLotRepository.save(parkingLot);
        Floor validFloor = FixedValidFloor.createValidFloor();
        validFloor.setParkingLot(parkingLot);
        floorRepository.save(validFloor);
    }



    @Test
    @Order(0)
    public void testCreateGuestPass(){
//        parkingLot = new ParkingLot(Time.valueOf("0:00:00"), Time.valueOf("23:59:59"), 15, 10, 250, 250);
//        parkingLotRepository.save(parkingLot);
//        Floor validFloor = FixedValidFloor.createValidFloor();
//        validFloor.setParkingLot(parkingLot);
//        floorRepository.save(validFloor);

        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
        ResponseEntity<GuestPassResponseDto> response = client.postForEntity("/guestPass", request, GuestPassResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(GuestPassFixture.floorNumber, response.getBody().getFloorNumber());
        assertEquals(GuestPassFixture.isLarge, response.getBody().getIsLarge());
        assertEquals(GuestPassFixture.spotNumber, response.getBody().getSpotNumber());
        // Add more assertions?

        guestPassFixture.id =  response.getBody().getId();
    }


    @Test
    @Order(1)
    public void testGetGuestPass(){
        ResponseEntity<GuestPassResponseDto> response = client.getForEntity("/guestPass/" + guestPassFixture.id, GuestPassResponseDto.class);
//        ResponseEntity<String> response = client.getForEntity("/guestPass/" + guestPassFixture.id, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }


    @Test
    @Order(2)
    public void testGetInvalidGuestPass(){
        Integer invalidId = 2;
        ResponseEntity<String> response = client.getForEntity("/guestPass/" + invalidId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Guest pass with id: " + invalidId + " does not exist.");
    }

    @Test
    @Order(3)
    public void testCreateBlankGuestPass() {
        GuestPassRequestDto request = new GuestPassRequestDto();
        ResponseEntity<String> response =  client.postForEntity("/guestPass", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertContains("Cannot have an empty number of months.", response.getBody());
        assertContains("Cannot have an empty spot number.", response.getBody());
        assertContains("Cannot have an empty license plate.", response.getBody());
        assertContains("Cannot have an empty confirmation code.", response.getBody());
        assertContains("Cannot have an empty floor number.", response.getBody());
        assertContains("Must specify whether the pass is small or large.", response.getBody());
//        assertContains("Start date cannot be null", response.getBody());
        assertContains("Cannot have an empty number of 15 minute increments.", response.getBody());
    }

    @Test
    @Order(4)
    public void testCreateInvalidNrIncrementsGuestPass() {
        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
        // Set a negative number as increments
        request.setNumberOfFifteenMinuteIncrements(-1);

        ResponseEntity<String> response =  client.postForEntity("/guestPass", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Must enter a positive number of 15 minute increments.", response.getBody());
    }

    @Test
    @Order(5)
    public void testCreateInvalidFloorNumberGuestPass() {
        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();

        // set a floor number for a floor that does not exist
        request.setFloorNumber(1);

        ResponseEntity<String> response =  client.postForEntity("/guestPass", request, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("The floor with floor number 1 does not exist.", response.getBody());
    }

    @Test
    @Order(7)
    public void testCreateInvalidFloorTypeGuestPass() {
        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
        // Create a member only floor
        Floor validFloor2 = new Floor(FixedValidFloor.floorNumber2, FixedValidFloor.largeSpotCapacity2, FixedValidFloor.smallSpotCapacity2, FixedValidFloor.isMemberOnly2);
        validFloor2.setParkingLot(parkingLot);
        floorRepository.save(validFloor2);


        request.setFloorNumber(FixedValidFloor.floorNumber2);

//        ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

        ResponseEntity<String> response =  client.postForEntity("/guestPass", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertContains("Floor " + FixedValidFloor.floorNumber2 + " is reserved for monthly members", response.getBody());
    }
    @Test
    @Order(8)
    public void testCreateInvalidSpotGuestPass() {

        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();

//        ResponseEntity<GuestPassResponseDto> response = client.postForEntity("/guestPass", request, GuestPassResponseDto.class);
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Create the same pass again, however spot in this case is already occupied
        request = GuestPassFixture.createValidGuestPass();
        ResponseEntity<String> response2 = client.postForEntity("/guestPass", request, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response2.getStatusCode());
        assertContains("Spot A24 is currently occupied", response2.getBody());

    }

    @Test
    @Order(9)
    public void testGetAllGuestPasses(){

//        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
//        ResponseEntity<GuestPassResponseDto> response = client.postForEntity("/guestPass", request, GuestPassResponseDto.class);

        GuestPassRequestDto request2 = GuestPassFixture.createValidGuestPass();
        request2.setSpotNumber(GuestPassFixture.spotNumber2);
        ResponseEntity<GuestPassResponseDto> response2 = client.postForEntity("/guestPass", request2, GuestPassResponseDto.class);

        ResponseEntity<List> response3 = client.getForEntity("/guestPass/", List.class);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> guestPasses = response3.getBody();

        assertEquals(guestPasses.get(0).get("spotNumber"), GuestPassFixture.spotNumber);
        assertEquals(guestPasses.get(0).get("confirmationCode"), GuestPassFixture.confirmationCode);
        assertEquals(guestPasses.get(0).get("licensePlate"), GuestPassFixture.licensePlate);
        assertEquals(guestPasses.get(0).get("isLarge"), GuestPassFixture.isLarge);
        assertEquals(guestPasses.get(0).get("floorNumber"), GuestPassFixture.floorNumber);

        assertEquals(guestPasses.get(1).get("spotNumber"), GuestPassFixture.spotNumber2);

    }

    @Test
    @Order(10)
    public void testGetMonthlyPassesByFloorInvalidFloor(){

        ResponseEntity<String> response =  client.getForEntity("/guestPass/floor/" + 3, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("The floor with floor number 3 does not exist.", response.getBody());
    }

    @Test
    @Order(11)
    public void testGetGuestPassesByFloorNoPassesFound(){

        Floor validFloor2 = new Floor(FixedValidFloor.floorNumber2, FixedValidFloor.largeSpotCapacity2, FixedValidFloor.smallSpotCapacity2, FixedValidFloor.isMemberOnly2);
        validFloor2.setParkingLot(parkingLot);
        floorRepository.save(validFloor2);

        ResponseEntity<String> response =  client.getForEntity("/guestPass/floor/" + FixedValidFloor.floorNumber2, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("There are no guest passes on floor " + FixedValidFloor.floorNumber2, response.getBody());
    }

    @Test
    @Order(12)
    public void testGetGuestPassesByFloor() {
//        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
//        ResponseEntity<GuestPassResponseDto> response = client.postForEntity("/guestPass", request, GuestPassResponseDto.class);
//
//        GuestPassRequestDto request2 = GuestPassFixture.createValidGuestPass();
//        request2.setSpotNumber(GuestPassFixture.spotNumber2);
//        ResponseEntity<GuestPassResponseDto> response2 = client.postForEntity("/guestPass", request2, GuestPassResponseDto.class);


        ResponseEntity<List> response3 = client.getForEntity("/guestPass/floor/" + FixedValidFloor.floorNumber, List.class);
        assertNotNull(response3.getBody());

        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> guestPasses = response3.getBody();

        assertEquals(guestPasses.get(0).get("spotNumber"), GuestPassFixture.spotNumber);
        assertEquals(guestPasses.get(0).get("confirmationCode"), GuestPassFixture.confirmationCode);
        assertEquals(guestPasses.get(0).get("licensePlate"), GuestPassFixture.licensePlate);
        assertEquals(guestPasses.get(0).get("isLarge"), GuestPassFixture.isLarge);
        assertEquals(guestPasses.get(0).get("floorNumber"), GuestPassFixture.floorNumber);

        assertEquals(guestPasses.get(1).get("spotNumber"), GuestPassFixture.spotNumber2);
        assertEquals(guestPasses.get(1).get("floorNumber"), GuestPassFixture.floorNumber);

    }
    @Test
    @Order(13)
    public void testGetGuestPassesByDateNoPassesFound(){


        ResponseEntity<String> response =  client.getForEntity("/guestPass/date/" + LocalDate.now().plusDays(96), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertContains("There are no guest passes for date " + LocalDate.now().plusDays(96) , response.getBody());
    }
    @Test
    @Order(14)
    public void testGetGuestPassesByDate() {
//        GuestPassRequestDto request = GuestPassFixture.createValidGuestPass();
//        ResponseEntity<GuestPassResponseDto> response = client.postForEntity("/guestPass", request, GuestPassResponseDto.class);
//
//        GuestPassRequestDto request2 = GuestPassFixture.createValidGuestPass();
//        request2.setSpotNumber(GuestPassFixture.spotNumber2);
//        ResponseEntity<GuestPassResponseDto> response2 = client.postForEntity("/guestPass", request2, GuestPassResponseDto.class);


        ResponseEntity<List> response3 = client.getForEntity("/guestPass/date/" + LocalDate.now(), List.class);
        assertNotNull(response3.getBody());

        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody());
        List<Map<String, Object>> guestPasses = response3.getBody();

        assertEquals(guestPasses.get(0).get("spotNumber"), GuestPassFixture.spotNumber);
        assertEquals(guestPasses.get(0).get("confirmationCode"), GuestPassFixture.confirmationCode);
        assertEquals(guestPasses.get(0).get("licensePlate"), GuestPassFixture.licensePlate);
        assertEquals(guestPasses.get(0).get("isLarge"), GuestPassFixture.isLarge);
        assertEquals(guestPasses.get(0).get("floorNumber"), GuestPassFixture.floorNumber);

        assertEquals(guestPasses.get(1).get("spotNumber"), GuestPassFixture.spotNumber2);
        assertEquals(guestPasses.get(1).get("floorNumber"), GuestPassFixture.floorNumber);

    }
    @Test
    @Order(15)
    public void testDeleteFirstGuestPassWithId() {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/guestPass/" + guestPassFixture.id, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<String> response2 =  client.getForEntity("/guestPass/" + guestPassFixture.id, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
        assertEquals(response2.getBody(),  "Guest pass with id: " + guestPassFixture.id + " does not exist.");
    }

    @Test
    @Order(16)
    public void testDeleteAgainGuestPassWithId() {
        HttpEntity<String> requestEntity = new HttpEntity<>(null);
        ResponseEntity<String> response = client.exchange("/guestPass/" + guestPassFixture.id, HttpMethod.DELETE, requestEntity, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(),  "Guest pass with id: " + guestPassFixture.id + " does not exist.");
    }

    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);


    }


}