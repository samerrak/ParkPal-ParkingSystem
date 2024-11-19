package ca.mcgill.ecse321.PLMS.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import ca.mcgill.ecse321.PLMS.model.*;
import ca.mcgill.ecse321.PLMS.repository.MonthlyPassRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.GuestPassRepository;

@SpringBootTest
public class GuestPassServiceTests {

  @Mock
  private GuestPassRepository guestPassRepo;

  @Mock
  private FloorRepository floorRepo;

  @Mock
  private ParkingLotRepository parkingLotRepo;

  @Mock
  private MonthlyPassRepository monthlyPassRepo;

  @InjectMocks
  private GuestPassService guestPassService;
  private String spotNumber1;
  private GuestPass guestPass1;
  private GuestPass guestPass2;
  private Time starTime1;
  private Time endTime1;
  private String spotNumber2;
  private Time starTime2;
  private Time endTime2;
  private LocalDate date1;

  @BeforeEach
  public void setUp(){
    // MockitoAnnotations.initMocks(this);

    spotNumber1 = "A24";
    date1 = Date.valueOf("2023-02-21").toLocalDate();
    starTime1 = Time.valueOf("12:00:00");
    endTime1 = Time.valueOf("18:00:00");
    boolean isLarge1 = true;
    guestPass1 = new GuestPass(null, spotNumber1, null, isLarge1, date1, starTime1, endTime1);

    spotNumber2 = "A38";
    final LocalDate date2 = Date.valueOf("2023-02-21").toLocalDate();
    starTime2 = Time.valueOf("14:00:00");
    endTime2 = Time.valueOf("19:00:00");
    boolean isLarge2 = true;
    guestPass2 = new GuestPass(null, spotNumber2, null, isLarge2, date2, starTime2, endTime2);

  }

  @Test
  /**
   * Get a guest pass
   */
  public void testGetValidGuestPass(){

    int id  = 1;
    when(guestPassRepo.findGuestPassById(id)).thenReturn(guestPass1);
    GuestPass output = guestPassService.getGuestPassById(id);
    assertEquals(output.getSpotNumber(), spotNumber1);
    assertEquals(output.getStartTime(), starTime1);
    assertEquals(output.getEndTime(), endTime1);
  }

  @Test
  /**
   * No guest passes in the database
   */
    public void testGetAllEmptyGuestPasses() {
        ArrayList<GuestPass> passes = new ArrayList<GuestPass>();
        when(guestPassRepo.findAll()).thenReturn(passes);
        PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.getAllGuestPasses());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(),"There are no guest passes in the system." );
    }

  @Test
  /**
   * Test getting a guest pass which doesn't exist
   */
  public void testGetInvalidGuestPass(){
    final int invalidPassNumber = 42;
    when(guestPassRepo.findGuestPassById(invalidPassNumber)).thenReturn(null);

    PLMSException e = assertThrows(PLMSException.class,
            () -> guestPassService.getGuestPassById(invalidPassNumber));
    assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    assertEquals("Guest pass with id: " + invalidPassNumber + " does not exist.", e.getMessage());
  }


    @Test
    /**
     * Get all the guest passes from the database
     */
    public void testGetAllGuestPasses(){


      ArrayList<GuestPass> guestPasses = new ArrayList<>();
      guestPasses.add(guestPass1);
      guestPasses.add(guestPass2);

      when(guestPassRepo.findAll()).thenReturn(guestPasses);
      Iterable<GuestPass> output = guestPassService.getAllGuestPasses();
      Iterator<GuestPass> i = output.iterator();
      GuestPass outputGuestPass = i.next();
      assertEquals(outputGuestPass.getSpotNumber(), spotNumber1);
      assertEquals(outputGuestPass.getStartTime(), starTime1);
      assertEquals(outputGuestPass.getEndTime(), endTime1);

      outputGuestPass = i.next();
      assertEquals(outputGuestPass.getSpotNumber(), spotNumber2);
      assertEquals(outputGuestPass.getStartTime(), starTime2);
      assertEquals(outputGuestPass.getEndTime(), endTime2);


    }

    @Test
    /**
     * Get guest passes registered on a floor
     */
    public void testGetGuestPassesByFloor(){

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      Floor floor2 = new Floor();
      floor2.setFloorNumber(2);

      guestPass1.setFloor(floor);
      guestPass2.setFloor(floor2);

      ArrayList<GuestPass> guestPasses = new ArrayList<>();
      guestPasses.add(guestPass1);
      guestPasses.add(guestPass2);
      when(guestPassRepo.findAll()).thenReturn(guestPasses);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);

      ArrayList<GuestPass> output = (ArrayList<GuestPass>) guestPassService.getGuestPassesByFloor(1);
        Iterator<GuestPass> i = output.iterator();
        GuestPass outputGuestPass = i.next();
      assertEquals(output.size(), 1);
      assertEquals(outputGuestPass.getFloor().getFloorNumber(), 1);
    }

    @Test
    /**
     * Test getting passes on a floor that doesn't exist
     */
    public void testGetAllGuestPassesByFloorInvalid1(){
      // test for the case in which there is no floor that exists with this number
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(null);
      PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.getGuestPassesByFloor(1));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "The floor with floor number " + 1 + " does not exist.");
    }

    @Test
    /**
     * Get all passes on a floor where there are no passes.
     */
    public void testGetAllGuestPassesByFloorInvalid2(){
      guestPass2.setFloor(null);
      ArrayList<GuestPass> guestPasses = new ArrayList<>();
      guestPasses.add(guestPass2);
      when(guestPassRepo.findAll()).thenReturn(guestPasses);
      Floor floor = new Floor();
      floor.setFloorNumber(1);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);
      PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.getGuestPassesByFloor(1));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no guest passes on floor " + 1);
    }

  @Test
  /**
   * Delete a guest pass
   */
  public void testValidDeleteGuestPass(){
    int id = 1;
    when(guestPassRepo.findGuestPassById(id)).thenReturn(guestPass1);
    guestPassService.deleteGuestPassById(id);

  }

  @Test
  /**
   * Delete a pass which doesn't exist
   */
  public void testInvalidDeleteGuestPass()
  {
    final int invalidPassNumber = 42;
    when(guestPassRepo.findGuestPassById(invalidPassNumber)).thenReturn(null);
    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.deleteGuestPassById(invalidPassNumber));
    assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    assertEquals(e.getMessage(), "Guest pass with id: " + invalidPassNumber + " does not exist.");
  }

  @Test
  /**
   * Get all guest passes on a particular date.
   */
  public void testGetGuestPassesByDate(){

      LocalDate Date1 = Date.valueOf("2023-02-21").toLocalDate();
      LocalDate Date2 = Date.valueOf("2023-02-22").toLocalDate();



    guestPass1.setDate(Date1);
    guestPass2.setDate(Date2);
    ArrayList<GuestPass> guestPasses = new ArrayList<>();
    guestPasses.add(guestPass1);
    guestPasses.add(guestPass2);

    when(guestPassRepo.findAll()).thenReturn(guestPasses);

    ArrayList<GuestPass> output = (ArrayList<GuestPass>) guestPassService.getGuestPassesByDate(Date.valueOf("2023-02-21").toLocalDate());
    Iterator<GuestPass> i = output.iterator();
    GuestPass outputGuestPass = i.next();
    assertEquals(output.size(), 1);
    assertEquals(outputGuestPass.getDate(), Date.valueOf("2023-02-21").toLocalDate());
    assertEquals(outputGuestPass.getSpotNumber(), spotNumber1);


  }
  @Test
  /**
   * Get guest passes on a date where there are no guest passes
   */
  public void testInvalidGetGuestPassesByDate() {
    LocalDate Date1 = Date.valueOf("2023-02-21").toLocalDate();
    LocalDate Date2 = Date.valueOf("2023-02-22").toLocalDate();

    guestPass1.setDate(Date1);
    guestPass2.setDate(Date2);
    ArrayList<GuestPass> guestPasses = new ArrayList<>();
    guestPasses.add(guestPass1);
    guestPasses.add(guestPass2);
    when(guestPassRepo.findAll()).thenReturn(guestPasses);
    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.getGuestPassesByDate(Date.valueOf("2023-03-23").toLocalDate()));
    assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    assertEquals(e.getMessage(), "There are no guest passes for date " + Date.valueOf("2023-03-23"));

  }
  /**
   * Test case for start time after closing time
   */ 
  @Test
  void testInvalidStartTimeAfterClosingTime() {
    Time openingTime = Time.valueOf("08:00:00");
    Time closingTime = Time.valueOf("18:00:00");
    Time startTime = Time.valueOf("19:00:00");
    Time endTime = Time.valueOf("19:30:00");

    PLMSException e = assertThrows(PLMSException.class, () -> {
      guestPassService.validateGuestPassHours(startTime, endTime, openingTime, closingTime);
    });

    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass beginning after the lot closes.");
  }

  /**
   * / Test case for end time after closing time
   */
  @Test
  void testInvalidEndTimeAfterClosingTime() {
    Time openingTime = Time.valueOf("08:00:00");
    Time closingTime = Time.valueOf("18:00:00");
    Time startTime = Time.valueOf("14:00:00");
    Time endTime = Time.valueOf("19:30:00");

    PLMSException e = assertThrows(PLMSException.class, () -> {
      guestPassService.validateGuestPassHours(startTime, endTime, openingTime, closingTime);
    });

    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass ending after the lot closes.");
  }

  @Test
  /**
   * Create a guest pass
   */
  public void testCreateValidGuestPass()
  {
    int floorNumber = 1;
    int nrIncrements  = 2;
    int smallCarFee = 15;


    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(Time.valueOf("8:00:00"));
    parkingLot.setClosingTime(Time.valueOf("22:00:00"));
    parkingLot.setSmallSpotFee(smallCarFee);

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setIsMemberOnly(false);
    floor.setSmallSpotCapacity(3);
    floor.setLargeSpotCapacity(2);
    floor.setParkingLot(parkingLot);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // Initialize guest pass from dto
    GuestPass guestPass = new GuestPass();
    spotNumber1 = "A24";
    boolean isLarge = false;

    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(isLarge);

    // Change local time to within operating hours
    LocalDateTime currentTime  = LocalDateTime.parse("2023-03-25T09:00:00");
    Time endTime = Time.valueOf("9:30:00");
    when(guestPassRepo.save(guestPass)).thenReturn(guestPass);

    GuestPass createdGuestPass = guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime);
    // check to see that we've actually saved the floor in the DB
    verify(guestPassRepo, times(1)).save(guestPass);

    assertNotNull(createdGuestPass);
    assertEquals(spotNumber1, createdGuestPass.getSpotNumber());
    assertEquals(smallCarFee*nrIncrements, createdGuestPass.getFee());
    assertEquals(endTime, createdGuestPass.getEndTime());

  }

  @Test
  /**
   * Create a guest pass on a non existent floor
   */
  public void testInvalidCreateGuestPassNonExistantFloor()
  {
    int floorNumber = 1;
    int nrIncrements  = 2;
    int smallCarFee = 15;

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(Time.valueOf("8:00:00"));
    parkingLot.setClosingTime(Time.valueOf("22:00:00"));
    parkingLot.setSmallSpotFee(smallCarFee);

    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(null);

    // Initialize guest pass from dto
    GuestPass guestPass = new GuestPass();
    spotNumber1 = "A24";
    boolean isLarge = false;
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(isLarge);

    // Change local time to within operating hours
    LocalDateTime currentTime  = LocalDateTime.parse("2023-03-25T09:00:00");
    when(guestPassRepo.save(guestPass)).thenReturn(guestPass);


    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    assertEquals(e.getMessage(), "The floor with floor number " + floorNumber + " does not exist." );


  }

  @Test
  /**
   * Attempt to create a guest pass on a member only floor
   */
  void testCreateGuestPassOnMemberOnlyFloor() {
    // create a guest pass object
    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(false);
    int floorNumber = 1;

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(Time.valueOf("8:00:00"));
    parkingLot.setClosingTime(Time.valueOf("22:00:00"));

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);

    // set floor number to a member-only floor
    floor.setIsMemberOnly(true);

    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);


    int nrIncrements = 4;
    LocalDateTime currentTime = LocalDateTime.of(2023, 3, 25, 10, 0);

    // call the service method and assert that it throws an exception
    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Floor " + floorNumber + " is reserved for monthly members");
  }

  @Test
  /**
   * Exceed 12 hour max
   */
  void testInvalidCreateGuestPassWithTooManyIncrements() {
    // parameters
    int floorNumber = 1;
    LocalDateTime currentTime = LocalDateTime.of(2023, 3, 25, 10, 0);

    // create a guest pass object
    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(false);

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(Time.valueOf("8:00:00"));
    parkingLot.setClosingTime(Time.valueOf("22:00:00"));

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // Set NrIncrements greater than allowed
    int nrIncrements = 49;


    // call the service method and assert that it throws an exception
    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot reserve spot for more than 12 hours");
  }

  // Test case for start time before opening time
  @Test
  void testInvalidStartTimeBeforeOpeningTime() {
    Time openingTime = Time.valueOf("08:00:00");
    Time closingTime = Time.valueOf("18:00:00");
    Time startTime = Time.valueOf("07:00:00");
    Time endTime = Time.valueOf("07:30:00");

    PLMSException e = assertThrows(PLMSException.class, () -> {
      guestPassService.validateGuestPassHours(startTime, endTime, openingTime, closingTime);
    });

    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass beginning before the lot opens.");
  }

  @Test
  /**
   * Starting before lot opens
   */
  void testCreateGuestPassInvalidStartTime1() {
    int floorNumber = 1;
    int nrIncrements = 4;

    // Set start time before lot opening hours
    LocalDateTime currentTime = LocalDateTime.of(2023, 4, 1, 5, 0, 0);
    Time openingTime = Time.valueOf("07:00:00");
    Time closingTime = Time.valueOf("22:00:00");

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(openingTime);
    parkingLot.setClosingTime(closingTime);

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);


    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(false);

    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass beginning before the lot opens.");
  }

  @Test
  /**
   * Spills over lot closing
   */
  void testInvalidCreateGuestPassInvalidEndTime() {
    int floorNumber = 1;
    int nrIncrements = 5;

    // Set end time after lot closing hours
    LocalDateTime currentTime = LocalDateTime.of(2023, 4, 1, 21, 0, 0);
    Time openingTime = Time.valueOf("07:00:00");
    Time closingTime = Time.valueOf("22:00:00");

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(openingTime);
    parkingLot.setClosingTime(closingTime);

    // Initialize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    floor.setSmallSpotCapacity(3);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(false);

    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass ending after the lot closes.");
  }

  @Test
  /**
   * Booked after lot closes
   */
  void testCreateGuestPassInvalidStartTime2() {
    int floorNumber = 1;
    int nrIncrements = 4;

    // Set start time before lot opening hours
    LocalDateTime currentTime = LocalDateTime.of(2023, 4, 1, 23, 0, 0);
    Time openingTime = Time.valueOf("07:00:00");
    Time closingTime = Time.valueOf("22:00:00");

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(openingTime);
    parkingLot.setClosingTime(closingTime);

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);


    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber("A24");
    guestPass.setIsLarge(false);

    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Cannot have a guest pass beginning after the lot closes.");
  }
  @Test
  /**
   * Spot currently taken
   */
  void testCreateGuestPassSpotNotAvailable() {
    int floorNumber = 1;
    int nrIncrements = 4;
    String spotNumber = "A4";

    // Set start and end times within lot opening hours
    LocalDateTime currentTime = LocalDateTime.of(2023, 4, 1, 10, 0, 0);
    Time openingTime = Time.valueOf("07:00:00");
    Time closingTime = Time.valueOf("22:00:00");

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(openingTime);
    parkingLot.setClosingTime(closingTime);

    // Initailize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    floor.setSmallSpotCapacity(3);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // Set guest pass to same spot as intended
    guestPass1.setFloor(floor);
    guestPass1.setSpotNumber(spotNumber);
    guestPass1.setStartTime(Time.valueOf("10:15:00"));
    guestPass1.setEndTime(Time.valueOf("10:30:00"));
    guestPass1.setDate(currentTime.toLocalDate());

    // Attempt to create GuestPass for a spot that is not available
    GuestPass guestPass = new GuestPass();
    guestPass.setSpotNumber(spotNumber); // spot A4 does not exist on the floor
    guestPass.setIsLarge(false);
    ArrayList<GuestPass> guestPasses = new ArrayList<>();
    guestPasses.add(guestPass1);
    when(guestPassRepo.findAll()).thenReturn(guestPasses);


    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "Spot " + spotNumber + " is currently occupied");
  }


  @Test
  /**
   * exceeded capacity on floor
   */
  void testCreateGuestPassExceededCapacity() {
    int floorNumber = 1;
    int nrIncrements = 4;
    LocalDate currentDate = Date.valueOf(LocalDate.of(2023, 4, 1)).toLocalDate();
    LocalDate startDate2 = Date.valueOf(LocalDate.of(2020, 4, 1)).toLocalDate();

    LocalDateTime currentTime = LocalDateTime.of(2023, 4, 1, 10, 0, 0);
    Time openingTime = Time.valueOf("07:00:00");
    Time closingTime = Time.valueOf("22:00:00");

    // Initialize parking lot
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setOpeningTime(openingTime);
    parkingLot.setClosingTime(closingTime);

    // Initialize floor
    Floor floor = new Floor();
    floor.setFloorNumber(floorNumber);
    floor.setParkingLot(parkingLot);
    floor.setLargeSpotCapacity(1);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // Initialize guest passes
    GuestPass guestPass1 = new GuestPass();
    guestPass1.setSpotNumber("A24");
    guestPass1.setIsLarge(true);
    guestPass1.setFloor(floor);
    guestPass1.setDate(currentDate);
    guestPass1.setStartTime(Time.valueOf("10:00:00"));
    guestPass1.setEndTime(Time.valueOf("14:00:00"));

    GuestPass guestPass2 = new GuestPass();
    guestPass2.setSpotNumber("A25");
    guestPass2.setIsLarge(true);
    guestPass2.setFloor(floor);
    guestPass2.setDate(startDate2);
    guestPass2.setStartTime(Time.valueOf("10:45:00"));
    guestPass2.setEndTime(Time.valueOf("14:00:00"));

    // // Initialize monthly passes
    MonthlyPass monthlyPass1 = new MonthlyPass();
    monthlyPass1.setSpotNumber("A24");
    monthlyPass1.setIsLarge(true);
    monthlyPass1.setFloor(floor);
    monthlyPass1.setStartDate(currentDate);
    LocalDate endDate1 = Date.valueOf(LocalDate.of(2024, 4, 1)).toLocalDate();
    monthlyPass1.setEndDate(endDate1);

    // inactive monthly pass
    MonthlyPass monthlyPass2 = new MonthlyPass();
    monthlyPass2.setSpotNumber("A25");
    monthlyPass2.setIsLarge(true);
    monthlyPass2.setFloor(floor);
    LocalDate endDate2 = Date.valueOf(LocalDate.of(2020, 5, 1)).toLocalDate();
    monthlyPass2.setStartDate(startDate2);
    monthlyPass2.setEndDate(endDate2);

    ArrayList<GuestPass> guestPassList = new ArrayList<>();
    ArrayList<MonthlyPass> monthlyPassList = new ArrayList<>();
    guestPassList.add(guestPass1);
    guestPassList.add(guestPass2);
    // monthlyPassList.add(monthlyPass2);
    // monthlyPassList.add(monthlyPass1);
    when(guestPassRepo.findAll()).thenReturn(guestPassList);
    when(monthlyPassRepo.findAll()).thenReturn(monthlyPassList);

    // Test with exceeded capacity
    GuestPass guestPass3 = new GuestPass();
    guestPass3.setSpotNumber("A26");
    guestPass3.setIsLarge(true);

    PLMSException e = assertThrows(PLMSException.class, () -> guestPassService.createGuestPass(guestPass3, floorNumber, nrIncrements, currentTime));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "All spots of this size on floor " + floorNumber +" are occupied.");
  }


}
