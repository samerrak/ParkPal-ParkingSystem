package ca.mcgill.ecse321.PLMS.integration;


import ca.mcgill.ecse321.PLMS.controller.MonthlyPassController;
import ca.mcgill.ecse321.PLMS.dto.*;
import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.GuestPassRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyPassRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
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
public class MonthlyPassIntegrationTests {

    private class MonthlyPassFixture {
        public Integer id = 1;
        //public Double fee = 50.50;
        public String spotNumber = "A24";
        public String confirmationCode = "NeverGonnaGiveYouUp";
        public String licensePlate = "12345678";
        public Boolean isLarge = true;
        public LocalDate startDate =  Date.valueOf("2024-4-1").toLocalDate();
        public Integer numberOfMonths = 2;
        public LocalDate endDate = Date.valueOf("2024-7-1").toLocalDate();
        public Integer floorNumber = 0;
        public String monthlyCustomerEmail = "samer.abdulkarim@gmail.com";

        //public void setFee(Double fee){this.fee = fee;}
        public void setSpotNumber(String spotNumber){this.spotNumber = spotNumber;}
        public void setConfirmationCode(String confirmationCode){this.confirmationCode = confirmationCode;}
        public void setLicensePlate(String licensePlate){this.licensePlate = licensePlate;}
        public void setLarge(Boolean isLarge){this.isLarge = isLarge;}
        public void setStartDate(LocalDate startDate){this.startDate = startDate;}
        public void setNumberOfMonths(Integer numberOfMonths){this.numberOfMonths = numberOfMonths;}
        public void setFloor(Integer floorNumber){this.floorNumber = floorNumber;}
        public void setMonthlyCustomerEmail(String monthlyCustomerEmail){this.monthlyCustomerEmail = monthlyCustomerEmail;}
    }

    private class FixedValidFloor {

        public static final int floorNumber = 0;
        public static final boolean isMemberOnly = true;
        public static final int largeSpotCapacity = 10;
        public static final int smallSpotCapacity = 10;

        public static final boolean isMemberOnlyUpdated = false;
        public static final int largeSpotCapacityUpdated = 30;
        public static final int smallSpotCapacityUpdated = 40;

        public static final int secondFloorNumber = 2;

        public static Floor createValidFloor(){
            Floor validFloor = new Floor(floorNumber, largeSpotCapacity, smallSpotCapacity, isMemberOnly);
            return validFloor;
        }

    }

    private class FixedValidCustomer {

        public static final String email = "samer.abdulkarim@gmail.com";
        public static final String password = "IntelliJLover!";
        public static final String name = "Samer Abdulkarim";

        public static MonthlyCustomer createValidMonthlyCustomer(){
            MonthlyCustomer validCustomer = new MonthlyCustomer(email, password, name);
            return validCustomer;
        }
    }
    


    private MonthlyPassFixture monthlyPassFixture;

    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private MonthlyPassRepository monthlyPassRepository;
    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;
    @Autowired
    private GuestPassRepository guestPassRepository;


    @Autowired
    private TestRestTemplate client;

    @BeforeAll
    public void setupTestFixture(){
        this.monthlyPassFixture = new MonthlyPassFixture();
    }

    @BeforeAll
    @BeforeEach
    @AfterEach
    @AfterAll
    public void clearDatabase() {
        floorRepository.deleteAll();
        parkingLotRepository.deleteAll();
        monthlyPassRepository.deleteAll();
        monthlyCustomerRepository.deleteAll();
    }

    private MonthlyPassRequestDto setRequest(String spotNumber, String confirmationCode,
                                             String licensePlate, Integer numberOfMonths, LocalDate startDate, Integer floorNumber, Boolean isLarge, String customerEmail) {
        ParkingLot parkingLot = new ParkingLot(Time.valueOf("8:00:00"), Time.valueOf("20:00:00"), 15, 10, 250, 250);
        MonthlyPassRequestDto request = new MonthlyPassRequestDto();

        request.setSpotNumber(spotNumber);
        request.setConfirmationCode(confirmationCode);
        request.setLicensePlate(licensePlate);
        request.setLarge(isLarge);
        request.setNumberOfMonths(numberOfMonths);
        request.setStartDate(startDate);
        request.setFloorNumber(floorNumber);
        request.setCustomerEmail(customerEmail);
        return request;
    }

    // private boolean equals(MonthlyPassResponseDto response, MonthlyPassFixture e){
    //     boolean b = response.getSpotNumber().equals(e.spotNumber);
    //     b = b & response.getConfirmationCode().equals(e.confirmationCode);
    //     b = b & response.getLicensePlate().equals(e.licensePlate);
    //     b = b & response.getLarge().equals(e.isLarge);
    //     b = b & response.getStartDate().equals(e.startDate);
    //     b = b & response.getEndDate().equals(e.endDate);
    //     b = b & response.getFloorNumber().equals(e.floorNumber);
    //     return b & response.getMonthlyCustomerEmail().equals(e.monthlyCustomerEmail);
    // }


    @Test
    @Order(0)
    public void testCreateMonthlyPassNoAccount(){
        ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
        lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
        lotrequest.setClosingTime(Time.valueOf("20:00:00"));
        lotrequest.setLargeSpotFee(15.0);
        lotrequest.setSmallSpotFee(10.0);
        lotrequest.setSmallSpotMonthlyFlatFee(250.0);
        lotrequest.setLargeSpotMonthlyFlatFee(250.0);

        ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

        FloorRequestDto floorRequest = new FloorRequestDto();
        floorRequest.setFloorNumber(0);
        floorRequest.setIsMemberOnly(true);
        floorRequest.setLargeSpotCapacity(10);
        floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

        MonthlyPassRequestDto request = setRequest(monthlyPassFixture.spotNumber, monthlyPassFixture.confirmationCode,
                monthlyPassFixture.licensePlate, monthlyPassFixture.numberOfMonths, monthlyPassFixture.startDate,
                monthlyPassFixture.floorNumber, monthlyPassFixture.isLarge, monthlyPassFixture.monthlyCustomerEmail);
        request.setCustomerEmail(null);
        ResponseEntity<MonthlyPassResponseDto> response = client.postForEntity("/monthlypass", request, MonthlyPassResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getBody().getCustomerEmail(), null);
    }

    @Test
    @Order(1)
    public void testCreateMonthlyPassAccount() {
        ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
        lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
        lotrequest.setClosingTime(Time.valueOf("20:00:00"));
        lotrequest.setLargeSpotFee(15.0);
        lotrequest.setSmallSpotFee(10.0);
        lotrequest.setSmallSpotMonthlyFlatFee(250.0);
        lotrequest.setLargeSpotMonthlyFlatFee(250.0);

        ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

        FloorRequestDto floorRequest = new FloorRequestDto();
        floorRequest.setFloorNumber(0);
        floorRequest.setIsMemberOnly(true);
        floorRequest.setLargeSpotCapacity(10);
        floorRequest.setSmallSpotCapacity(10);
        ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

        MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
        customerRequest.setEmail("samer.abdulkarim@gmail.com");
        customerRequest.setPassword("Hello!");
        customerRequest.setName("Samer Abdulkarim");

        ResponseEntity<MonthlyCustomerResponseDto> customerResponse = client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);

        MonthlyPassRequestDto request = setRequest("A25", monthlyPassFixture.confirmationCode,
                monthlyPassFixture.licensePlate, monthlyPassFixture.numberOfMonths, monthlyPassFixture.startDate,
                monthlyPassFixture.floorNumber, monthlyPassFixture.isLarge, "samer.abdulkarim@gmail.com");

        ResponseEntity<MonthlyPassResponseDto> response = client.postForEntity("/monthlypass", request, MonthlyPassResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getCustomerEmail(), "samer.abdulkarim@gmail.com");
    }

    @Test
    @Order(2)
    public void testGetMonthlyPass(){
        ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
        lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
        lotrequest.setClosingTime(Time.valueOf("20:00:00"));
        lotrequest.setLargeSpotFee(15.0);
        lotrequest.setSmallSpotFee(10.0);
        lotrequest.setSmallSpotMonthlyFlatFee(250.0);
        lotrequest.setLargeSpotMonthlyFlatFee(250.0);

        ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

        FloorRequestDto floorRequest = new FloorRequestDto();
        floorRequest.setFloorNumber(0);
        floorRequest.setIsMemberOnly(true);
        floorRequest.setLargeSpotCapacity(10);
        floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

        MonthlyPassRequestDto request = setRequest(monthlyPassFixture.spotNumber, monthlyPassFixture.confirmationCode,
                monthlyPassFixture.licensePlate, monthlyPassFixture.numberOfMonths, monthlyPassFixture.startDate,
                monthlyPassFixture.floorNumber, monthlyPassFixture.isLarge, monthlyPassFixture.monthlyCustomerEmail);
        request.setCustomerEmail(null);
        ResponseEntity<MonthlyPassResponseDto> response = client.postForEntity("/monthlypass", request, MonthlyPassResponseDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(response.getBody().getCustomerEmail(), null);
        monthlyPassFixture.id = response.getBody().getId();
        ResponseEntity<MonthlyPassResponseDto> secondResponse = client.getForEntity("/monthlypass/" + monthlyPassFixture.id, MonthlyPassResponseDto.class);
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertNotNull(response.getBody());
        //assertTrue(equals(response.getBody(), monthlyPassFixture));
    }


    @Test
    @Order(3)
    public void testGetInvalidMonthlyPass(){
        Integer invalidId = 2;
        ResponseEntity<String> response = client.getForEntity("/monthlypass/" + invalidId, String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(response.getBody(), "Monthly pass with id: " + invalidId + " does not exist.");
    }

   @Test
   @Order(3)
   public void testCreateBlankMonthlyPass() {
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertContains("Cannot have an empty number of months.", response.getBody());
       assertContains("Cannot have an empty spot number.", response.getBody());
       assertContains("Cannot have an empty license plate.", response.getBody());
       assertContains("Cannot have an empty confirmation code.", response.getBody());
       assertContains("Cannot have an empty floor number.", response.getBody());
       assertContains("Must specify whether the pass is for a small or large car", response.getBody());
       assertContains("Start date cannot be null", response.getBody());
   }

   @Test
   @Order(4)
   public void testCreateInvalidDateMonthlyPass() {
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A24");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345678");
       request.setLarge(true);
       request.setNumberOfMonths(2);
       request.setStartDate(Date.valueOf("2023-1-1").toLocalDate());
       request.setFloorNumber(0);
       request.setCustomerEmail("samer.abdulkarim@gmail.com");

       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertContains("Start date must be equal or greater than current date", response.getBody());
   }

   @Test
   @Order(5)
   public void testCreateInvalidMonthsMonthlyPass() {
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A24");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345678");
       request.setLarge(true);
       request.setNumberOfMonths(-2);
       request.setStartDate(Date.valueOf("2024-1-1").toLocalDate());
       request.setFloorNumber(1);
       request.setCustomerEmail("samer.abdulkarim@gmail.com");

       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertContains("Must enter a positive number of months.", response.getBody());
   }

   @Test
   @Order(6)
   public void testCreateInvalidFloorNumberMonthlyPass() {
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A24");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345678");
       request.setLarge(true);
       request.setNumberOfMonths(2);
       request.setStartDate(Date.valueOf("2024-1-1").toLocalDate());
       request.setFloorNumber(1);
       request.setCustomerEmail("samer.abdulkarim@gmail.com");

       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertContains("The floor with floor number 1 does not exist.", response.getBody());
   }

   @Test
   @Order(7)
   public void testCreateInvalidFloorTypeMonthlyPass() {
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A24");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345678");
       request.setLarge(true);
       request.setNumberOfMonths(2);
       request.setStartDate(Date.valueOf("2024-1-1").toLocalDate());
       request.setFloorNumber(1);
       request.setCustomerEmail("samer.abdulkarim@gmail.com");

       ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
       lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
       lotrequest.setClosingTime(Time.valueOf("20:00:00"));
       lotrequest.setLargeSpotFee(15.0);
       lotrequest.setSmallSpotFee(10.0);
       lotrequest.setSmallSpotMonthlyFlatFee(250.0);
       lotrequest.setLargeSpotMonthlyFlatFee(250.0);

       ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

       FloorRequestDto floorRequest = new FloorRequestDto();
       floorRequest.setFloorNumber(1);
       floorRequest.setIsMemberOnly(false);
       floorRequest.setLargeSpotCapacity(10);
       floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);


       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertContains("Floor 1 is reserved for guest passes only.", response.getBody());
   }

   @Test
   @Order(8)
   public void testCreateInvalidSpotMonthlyPass() {

       MonthlyPassRequestDto existingPass = new MonthlyPassRequestDto();
       existingPass.setSpotNumber("A24");
       existingPass.setConfirmationCode("NeverGonnaGiveYouUp");
       existingPass.setLicensePlate("12345679");
       existingPass.setLarge(false);
       existingPass.setNumberOfMonths(2);
       existingPass.setStartDate(Date.valueOf("2024-1-1").toLocalDate());
       existingPass.setFloorNumber(1);
       existingPass.setCustomerEmail("samer.abdulkarim@gmail.com");

       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A24");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345678");
       request.setLarge(true);
       request.setNumberOfMonths(2);
       request.setStartDate(Date.valueOf("2024-2-1").toLocalDate());
       request.setFloorNumber(1);
       request.setCustomerEmail("samer.abdulkarim@gmail.com");

       ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
       lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
       lotrequest.setClosingTime(Time.valueOf("20:00:00"));
       lotrequest.setLargeSpotFee(15.0);
       lotrequest.setSmallSpotFee(10.0);
       lotrequest.setSmallSpotMonthlyFlatFee(250.0);
       lotrequest.setLargeSpotMonthlyFlatFee(250.0);

       ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

       FloorRequestDto floorRequest = new FloorRequestDto();
       floorRequest.setFloorNumber(1);
       floorRequest.setIsMemberOnly(true);
       floorRequest.setLargeSpotCapacity(10);
       floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

       MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
       customerRequest.setEmail("samer.abdulkarim@gmail.com");
       customerRequest.setPassword("Hello!");
       customerRequest.setName("Samer Abdulkarim");

       ResponseEntity<MonthlyCustomerResponseDto> customerResponse = client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);

       ResponseEntity<String> existingPassResponse =  client.postForEntity("/monthlypass", existingPass, String.class);

       ResponseEntity<String> response =  client.postForEntity("/monthlypass", request, String.class);
       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
       assertContains("Spot A24 is currently occupied", response.getBody());
   }

   @Test
   @Order(9)
   public void testGetAllMonthlyPasses(){
       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber(monthlyPassFixture.spotNumber);
       request.setConfirmationCode(monthlyPassFixture.confirmationCode);
       request.setLicensePlate(monthlyPassFixture.licensePlate);
       request.setLarge(monthlyPassFixture.isLarge);
       request.setNumberOfMonths(monthlyPassFixture.numberOfMonths);
       request.setStartDate(monthlyPassFixture.startDate);
       request.setFloorNumber(monthlyPassFixture.floorNumber);
       request.setCustomerEmail(monthlyPassFixture.monthlyCustomerEmail);

       ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
       lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
       lotrequest.setClosingTime(Time.valueOf("20:00:00"));
       lotrequest.setLargeSpotFee(15.0);
       lotrequest.setSmallSpotFee(10.0);
       lotrequest.setSmallSpotMonthlyFlatFee(250.0);
       lotrequest.setLargeSpotMonthlyFlatFee(250.0);

       ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

       FloorRequestDto floorRequest = new FloorRequestDto();
       floorRequest.setFloorNumber(0);
       floorRequest.setIsMemberOnly(true);
       floorRequest.setLargeSpotCapacity(10);
       floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

       MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
       customerRequest.setEmail("samer.abdulkarim@gmail.com");
       customerRequest.setPassword("Hello!");
       customerRequest.setName("Samer Abdulkarim");

       ResponseEntity<MonthlyCustomerResponseDto> customerResponse = client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);
       ResponseEntity<MonthlyPassResponseDto> response =  client.postForEntity("/monthlypass", request, MonthlyPassResponseDto.class);

       MonthlyPassRequestDto request2 = new MonthlyPassRequestDto();
       request2.setSpotNumber("A25");
       request2.setConfirmationCode("NeverGonnaGiveYouUp");
       request2.setLicensePlate("12345679");
       request2.setLarge(true);
       request2.setNumberOfMonths(2);
       request2.setStartDate(Date.valueOf("2024-04-01").toLocalDate());
       request2.setFloorNumber(0);
       request2.setCustomerEmail("samer.abdulkarim@gmail.com");
       ResponseEntity<MonthlyPassResponseDto> response2 =  client.postForEntity("/monthlypass", request2, MonthlyPassResponseDto.class);

       ResponseEntity<List> response3 = client.getForEntity("/pass", List.class);
       assertEquals(HttpStatus.OK, response3.getStatusCode());
       assertNotNull(response3.getBody());
       List<Map<String, Object>> monthlyPasses = response3.getBody();

       assertEquals(monthlyPasses.get(1).get("spotNumber"), "A25");
       assertEquals(monthlyPasses.get(1).get("confirmationCode"), "NeverGonnaGiveYouUp");
       assertEquals(monthlyPasses.get(1).get("licensePlate"), "12345679");
       assertEquals(monthlyPasses.get(1).get("large"), true);
       assertEquals(monthlyPasses.get(1).get("startDate"), "2024-04-01");
       assertEquals(monthlyPasses.get(1).get("floorNumber"), 0);
       assertEquals(monthlyPasses.get(1).get("customerEmail"), "samer.abdulkarim@gmail.com");

       assertEquals(monthlyPasses.get(0).get("spotNumber"), monthlyPassFixture.spotNumber);
       assertEquals(monthlyPasses.get(0).get("confirmationCode"), monthlyPassFixture.confirmationCode);
       assertEquals(monthlyPasses.get(0).get("licensePlate"), monthlyPassFixture.licensePlate);
       assertEquals(monthlyPasses.get(0).get("large"), monthlyPassFixture.isLarge);
       assertEquals(monthlyPasses.get(1).get("startDate"), "2024-04-01");
       assertEquals(monthlyPasses.get(1).get("floorNumber"), monthlyPassFixture.floorNumber);
       assertEquals(monthlyPasses.get(1).get("customerEmail"), monthlyPassFixture.monthlyCustomerEmail);
   }

   @Test
   @Order(10)
   public void testGetMonthlyPassesByMonthlyCustomerInvalidCustomer(){

       ResponseEntity<String> response =  client.getForEntity("/monthlypass/customer/" + "thisis.awrongemail@gmail.com", String.class);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertContains("The account with email thisis.awrongemail@gmail.com does not exist.", response.getBody());
   }

   @Test
   @Order(11)
   public void testGetMonthlyPassesByMonthlyCustomerNoPassesFound(){

       MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
       customerRequest.setEmail("has.nopass@gmail.com");
       customerRequest.setPassword("Hello!");
       customerRequest.setName("Evan");

       client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);

       ResponseEntity<String> response =  client.getForEntity("/monthlypass/customer/" + "has.nopass@gmail.com", String.class);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertContains("There are no monthly passes for has.nopass@gmail.com", response.getBody());

   }

   @Test
   @Order(12)
   public void testGetMonthlyPassesByMonthlyCustomer(){

        MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
       customerRequest.setEmail("has.nopass@gmail.com");
       customerRequest.setPassword("Hello!");
       customerRequest.setName("Evan");

       client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);

        ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
       lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
       lotrequest.setClosingTime(Time.valueOf("20:00:00"));
       lotrequest.setLargeSpotFee(15.0);
       lotrequest.setSmallSpotFee(10.0);
       lotrequest.setSmallSpotMonthlyFlatFee(250.0);
       lotrequest.setLargeSpotMonthlyFlatFee(250.0);

       ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

       FloorRequestDto floorRequest = new FloorRequestDto();
       floorRequest.setFloorNumber(0);
       floorRequest.setIsMemberOnly(true);
       floorRequest.setLargeSpotCapacity(10);
       floorRequest.setSmallSpotCapacity(10);

       ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

       MonthlyPassRequestDto request = new MonthlyPassRequestDto();
       request.setSpotNumber("A26");
       request.setConfirmationCode("NeverGonnaGiveYouUp");
       request.setLicensePlate("12345679");
       request.setLarge(true);
       request.setNumberOfMonths(2);
       request.setStartDate(Date.valueOf("2024-2-1").toLocalDate());
       request.setFloorNumber(0);
       request.setCustomerEmail("has.nopass@gmail.com");

       ResponseEntity<String> passresponse = client.postForEntity("/monthlypass", request, String.class);

       ResponseEntity<List> response =  client.getForEntity("/monthlypass/customer/" + "has.nopass@gmail.com", List.class);
       assertNotNull(response.getBody());

       List<Map<String, Object>> monthlyPasses = response.getBody();

       assertEquals(monthlyPasses.size(), 1);

       assertEquals(monthlyPasses.get(0).get("spotNumber"), "A26");
       assertEquals(monthlyPasses.get(0).get("confirmationCode"), "NeverGonnaGiveYouUp");
       assertEquals(monthlyPasses.get(0).get("licensePlate"), "12345679");
       assertEquals(monthlyPasses.get(0).get("large"), true);
       assertEquals(monthlyPasses.get(0).get("startDate"), "2024-02-01");
       assertEquals(monthlyPasses.get(0).get("floorNumber"), 0);
       assertEquals(monthlyPasses.get(0).get("customerEmail"), "has.nopass@gmail.com");
   }

   @Test
   @Order(13)
   public void testGetMonthlyPassesByFloorInvalidFloor(){

       ResponseEntity<String> response =  client.getForEntity("/monthlypass/floor/" + 3, String.class);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertContains("The floor with floor number 3 does not exist.", response.getBody());
   }

   @Test
   @Order(14)
   public void testGetMonthlyPassesByFloorNoPassesFound(){


    ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
    lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
    lotrequest.setClosingTime(Time.valueOf("20:00:00"));
    lotrequest.setLargeSpotFee(15.0);
    lotrequest.setSmallSpotFee(10.0);
    lotrequest.setSmallSpotMonthlyFlatFee(250.0);
    lotrequest.setLargeSpotMonthlyFlatFee(250.0);

    ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

    FloorRequestDto floorRequest = new FloorRequestDto();
    floorRequest.setFloorNumber(3);
    floorRequest.setIsMemberOnly(true);
    floorRequest.setLargeSpotCapacity(10);
    floorRequest.setSmallSpotCapacity(10);

    ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

       ResponseEntity<String> response =  client.getForEntity("/monthlypass/floor/" + 3, String.class);
       assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
       assertContains("There are no monthly passes on floor 3", response.getBody());

   }

   @Test
   @Order(14)
   public void testGetMonthlyPassesByFloor(){

    MonthlyCustomerRequestDto customerRequest = new MonthlyCustomerRequestDto();
    customerRequest.setEmail("has.nopass@gmail.com");
    customerRequest.setPassword("Hello!");
    customerRequest.setName("Evan");

    client.postForEntity("/customer/create", customerRequest, MonthlyCustomerResponseDto.class);

     ParkingLotRequestDto lotrequest = new ParkingLotRequestDto();
    lotrequest.setOpeningTime(Time.valueOf("8:00:00"));
    lotrequest.setClosingTime(Time.valueOf("20:00:00"));
    lotrequest.setLargeSpotFee(15.0);
    lotrequest.setSmallSpotFee(10.0);
    lotrequest.setSmallSpotMonthlyFlatFee(250.0);
    lotrequest.setLargeSpotMonthlyFlatFee(250.0);

    ResponseEntity<ParkingLotResponseDto> lotresponse = client.postForEntity("/parkingLot/creation", lotrequest, ParkingLotResponseDto.class);

    FloorRequestDto floorRequest = new FloorRequestDto();
    floorRequest.setFloorNumber(3);
    floorRequest.setIsMemberOnly(true);
    floorRequest.setLargeSpotCapacity(10);
    floorRequest.setSmallSpotCapacity(10);

    ResponseEntity<FloorResponseDto> floorResponse = client.postForEntity("/floor", floorRequest, FloorResponseDto.class);

    MonthlyPassRequestDto request = new MonthlyPassRequestDto();
    request.setSpotNumber("A26");
    request.setConfirmationCode("NeverGonnaGiveYouUp");
    request.setLicensePlate("12345679");
    request.setLarge(true);
    request.setNumberOfMonths(2);
    request.setStartDate(Date.valueOf("2024-2-1").toLocalDate());
    request.setFloorNumber(3);
    request.setCustomerEmail("has.nopass@gmail.com");


       client.postForEntity("/monthlypass", request, MonthlyPassResponseDto.class);

       ResponseEntity<List> response =  client.getForEntity("/monthlypass/floor/" + 3, List.class);
       assertNotNull(response.getBody());

       List<Map<String, Object>> monthlyPasses = response.getBody();

       assertEquals(monthlyPasses.size(), 1);

       assertEquals(monthlyPasses.get(0).get("spotNumber"), "A26");
       assertEquals(monthlyPasses.get(0).get("confirmationCode"), "NeverGonnaGiveYouUp");
       assertEquals(monthlyPasses.get(0).get("licensePlate"), "12345679");
       assertEquals(monthlyPasses.get(0).get("large"), true);
       assertEquals(monthlyPasses.get(0).get("startDate"), "2024-02-01");
       assertEquals(monthlyPasses.get(0).get("floorNumber"), 3);
       assertEquals(monthlyPasses.get(0).get("customerEmail"), "has.nopass@gmail.com");


   }




    private static void assertContains(String expected, String actual) {
        String assertionMessage = String.format("Error message ('%s') contains '%s'.", actual, expected);
        assertTrue(actual.contains(expected), assertionMessage);
   }
//
//

}