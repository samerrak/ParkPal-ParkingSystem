package ca.mcgill.ecse321.PLMS.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import ca.mcgill.ecse321.PLMS.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;


import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.GuestPassRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyPassRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;


@SpringBootTest
public class MonthlyPassServiceTests {

  @Mock
  private MonthlyPassRepository monthlyPassRepo;

  @Mock
  private FloorRepository floorRepo;

  @Mock
  private MonthlyCustomerRepository monthlyCustomerRepo;

  @Mock
  private GuestPassRepository guestPassRepo;

  @InjectMocks
  private MonthlyPassService monthlyPassService;

  @Test
  /**
   * Get a valid monthly pass
   */
  public void testGetValidWithoutAccountMonthlyPass(){
    double fee = 50.50;
    String spotNumber = "A24";
    String licensePlate = "123ABC123";
    LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
    LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
    boolean isLarge = true;
    String confirmationCode = "NeverGonnaGiveYouUp";
    int id = 1;
    MonthlyPass monthlyPass = new MonthlyPass();
    monthlyPass.setFee(fee);
    monthlyPass.setSpotNumber(spotNumber);
    monthlyPass.setConfirmationCode(confirmationCode);
    monthlyPass.setIsLarge(isLarge);
    monthlyPass.setStartDate(startDate);
    monthlyPass.setEndDate(endDate);
    monthlyPass.setLicensePlate(licensePlate);
    when(monthlyPassRepo.findMonthlyPassById(id)).thenReturn(monthlyPass);
    MonthlyPass output = monthlyPassService.getMonthlyPassById(id);
    assertEquals(output.getSpotNumber(), spotNumber);
    assertEquals(output.getStartDate(), startDate);
    assertEquals(output.getEndDate(), endDate);
  }

  @Test
  /**
   * No monthly passes in the system
   */
    public void testGetAllEmptyMonthlyPasses() {
        ArrayList<MonthlyPass> passes = new ArrayList<>();
        when(monthlyPassRepo.findAll()).thenReturn(passes);
        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getAllMonthlyPasses());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(),"There are no monthly passes in the system." );
    }

  @Test
  /**
   * Get a valid monthly pass associated with an account
   */
  public void testGetValidWithAccountMonthlyPass(){
    double fee = 50.50;
    String spotNumber = "A24";
    String licensePlate = "123ABC123";
    LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
    LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
    boolean isLarge = true;
    String confirmationCode = "NeverGonnaGiveYouUp";
    int id = 1;


    String email = "rick.roll@gmail.com";
    String password = "intelliJLover123";
    String name = "Samer Abdulkarim";
    MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
    monthlyCustomer.setEmail(email);
    monthlyCustomer.setPassword(password);
    monthlyCustomer.setName(name);

    MonthlyPass monthlyPass = new MonthlyPass();
    monthlyPass.setFee(fee);
    monthlyPass.setSpotNumber(spotNumber);
    monthlyPass.setConfirmationCode(confirmationCode);
    monthlyPass.setIsLarge(isLarge);
    monthlyPass.setStartDate(startDate);
    monthlyPass.setEndDate(endDate);
    monthlyPass.setLicensePlate(licensePlate);
    monthlyPass.setCustomer(monthlyCustomer);

    when(monthlyPassRepo.findMonthlyPassById(id)).thenReturn(monthlyPass);
    MonthlyPass output = monthlyPassService.getMonthlyPassById(id);
    assertEquals(output.getSpotNumber(), spotNumber);
    assertEquals(output.getStartDate(), startDate);
    assertEquals(output.getEndDate(), endDate);
    assertEquals(output.getCustomer(), monthlyCustomer);
  }

  @Test
  /**
   * Get a invalid monthly pass
   */
  public void testGetInvalidMonthlyPass(){
    final int invalidPassNumber = 42;
		  when(monthlyPassRepo.findMonthlyPassById(invalidPassNumber)).thenReturn(null);

		PLMSException e = assertThrows(PLMSException.class,
				() -> monthlyPassService.getMonthlyPassById(invalidPassNumber));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("Monthly pass with id: " + invalidPassNumber + " does not exist.", e.getMessage());
  }




    @Test
    /**
     * Get all the monthly passes.
     */
    public void testGetAllMonthlyPasses(){
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";
    
    
        String email = "rick.roll@gmail.com";
        String password = "intelliJLover123";
        String name = "Samer Abdulkarim";
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        monthlyCustomer.setEmail(email);
        monthlyCustomer.setPassword(password);
        monthlyCustomer.setName(name);
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);
        monthlyPass.setCustomer(monthlyCustomer);
        
        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
        
        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);


        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        Iterable<MonthlyPass> output = monthlyPassService.getAllMonthlyPasses();
        Iterator<MonthlyPass> i = output.iterator();
        MonthlyPass outputMonthlyPass = i.next();
        assertEquals(outputMonthlyPass.getSpotNumber(), spotNumber);
        assertEquals(outputMonthlyPass.getStartDate(), startDate);
        assertEquals(outputMonthlyPass.getEndDate(), endDate);
        assertEquals(outputMonthlyPass.getCustomer(), monthlyCustomer);

        outputMonthlyPass = i.next();
        assertEquals(outputMonthlyPass.getSpotNumber(), spotNumber2);
        assertEquals(outputMonthlyPass.getStartDate(), startDate2);
        assertEquals(outputMonthlyPass.getEndDate(), endDate2);
        assertEquals(outputMonthlyPass.getCustomer(), null);

    }

    @Test
    /**
     * Get the monthly passes for a customer
     */
    public void testGetMonthlyPassesByMonthlyCustomer(){
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
    
    
        String email = "rick.roll@gmail.com";
        String password = "intelliJLover123";
        String name = "Samer Abdulkarim";
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        monthlyCustomer.setEmail(email);
        monthlyCustomer.setPassword(password);
        monthlyCustomer.setName(name);
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);
        monthlyPass.setCustomer(monthlyCustomer);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);
        monthlyPass2.setCustomer(monthlyCustomer);

        List<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        when(monthlyCustomerRepo.findMonthlyCustomerByEmail(email)).thenReturn(monthlyCustomer);

        List<MonthlyPass> output = monthlyPassService.getMonthlyPassesByMonthlyCustomer(email);
        MonthlyPass outputMonthlyPass = output.get(0);

        assertEquals(outputMonthlyPass.getSpotNumber(), spotNumber);
        assertEquals(outputMonthlyPass.getStartDate(), startDate);
        assertEquals(outputMonthlyPass.getEndDate(), endDate);
        assertEquals(outputMonthlyPass.getCustomer(), monthlyCustomer);

        outputMonthlyPass = output.get(1);
        assertEquals(outputMonthlyPass.getSpotNumber(), spotNumber2);
        assertEquals(outputMonthlyPass.getStartDate(), startDate2);
        assertEquals(outputMonthlyPass.getEndDate(), endDate2);
        assertEquals(outputMonthlyPass.getCustomer(), monthlyCustomer);
    }

    @Test
    /**
     * No account in the system
     */
    public void testGetInvalidMonthlyPassesByMonthlyCustomer1(){
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
    
    
        String invalidEmail = "rick.roll@gmail.com";
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        when(monthlyCustomerRepo.findMonthlyCustomerByEmail(invalidEmail)).thenReturn(null);

        
        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getMonthlyPassesByMonthlyCustomer(invalidEmail));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "The account with email " + invalidEmail + " does not exist.");
    }

    @Test
    /**
     * No passes with the account
     */
    public void testGetInvalidMonthlyPassesByMonthlyCustomer2(){

        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";
        
        String email = "rick.roll@gmail.com";
        String password = "intelliJLover123";
        String name = "Samer Abdulkarim";
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        monthlyCustomer.setEmail(email);
        monthlyCustomer.setPassword(password);
        monthlyCustomer.setName(name);

        String email2 = "no.pass@gmail.com";
        String password2 = "VsCodeLover123";
        String name2 = "Karl Bridi";
        MonthlyCustomer monthlyCustomer2 = new MonthlyCustomer();
        monthlyCustomer2.setEmail(email2);
        monthlyCustomer2.setPassword(password2);
        monthlyCustomer2.setName(name2);
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);
        monthlyPass.setCustomer(monthlyCustomer);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        when(monthlyCustomerRepo.findMonthlyCustomerByEmail(email2)).thenReturn(monthlyCustomer2);

        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getMonthlyPassesByMonthlyCustomer(email2));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no monthly passes for " + monthlyCustomer2.getEmail());
        
    }

    @Test
    /**
     * Get all the monthly passes on a floor
     */
    public void testGetMonthlyPassesByFloor(){
      
      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";

      double fee2 = 50.50;
      String spotNumber2 = "A25";
      String licensePlate2 = "123ABC124";
      LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
      LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
      boolean isLarge2 = true;
      String confirmationCode2 = "NeverGonnaGiveYouUp";
      
      String email = "rick.roll@gmail.com";
      String password = "intelliJLover123";
      String name = "Samer Abdulkarim";
      MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
      monthlyCustomer.setEmail(email);
      monthlyCustomer.setPassword(password);
      monthlyCustomer.setName(name);
      
      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setStartDate(startDate);
      monthlyPass.setEndDate(endDate);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setCustomer(monthlyCustomer);

      MonthlyPass monthlyPass2 = new MonthlyPass();
      monthlyPass2.setFee(fee2);
      monthlyPass2.setSpotNumber(spotNumber2);
      monthlyPass2.setConfirmationCode(confirmationCode2);
      monthlyPass2.setIsLarge(isLarge2);
      monthlyPass2.setStartDate(startDate2);
      monthlyPass2.setEndDate(endDate2);
      monthlyPass2.setLicensePlate(licensePlate2);
      monthlyPass2.setCustomer(monthlyCustomer);

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      Floor floor2 = new Floor();
      floor2.setFloorNumber(2);

      monthlyPass.setFloor(floor);
      monthlyPass2.setFloor(floor2);
      ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
      monthlyPasses.add(monthlyPass);
      monthlyPasses.add(monthlyPass2);
      
      when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);

      ArrayList<MonthlyPass> output = (ArrayList<MonthlyPass>) monthlyPassService.getMonthlyPassesByFloor(1);
      Iterator<MonthlyPass> i = output.iterator();
      MonthlyPass outputMonthlyPass = i.next();
      assertEquals(output.size(), 1);
      assertEquals(outputMonthlyPass.getFloor().getFloorNumber(), 1);
    }

    @Test
    /**
     * 
     */
    public void testGetInvalidMonthlyPassesByFloor1(){

      double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
    
    
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        when(floorRepo.findFloorByFloorNumber(1)).thenReturn(null);

        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getMonthlyPassesByFloor(1));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "The floor with floor number " + 1 + " does not exist.");
    }

    @Test
    public void testGetInvalidMonthlyPassesByFloor2(){

      double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
    
    
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        Floor floor = new Floor();
        floor.setFloorNumber(1);
        Floor floor2 = new Floor();
        floor2.setFloorNumber(2);

        monthlyPass.setFloor(floor);
        monthlyPass2.setFloor(floor);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);
        when(floorRepo.findFloorByFloorNumber(2)).thenReturn(floor2);

        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getMonthlyPassesByFloor(2));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no monthly passes on floor " + 2);
    }

    @Test
    public void testGetMonthlyPassesByDate(){
      double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);

        ArrayList<MonthlyPass> output = (ArrayList<MonthlyPass>) monthlyPassService.getMonthlyPassesByDate(Date.valueOf("2023-02-21").toLocalDate());
        Iterator<MonthlyPass> i = output.iterator();
        MonthlyPass outputMonthlyPass = i.next();
        assertEquals(output.size(), 1);
        assertEquals(outputMonthlyPass.getStartDate(), Date.valueOf("2023-02-21").toLocalDate());
        assertEquals(outputMonthlyPass.getLicensePlate(), "123ABC123");


    }

    @Test
    public void testGetInvalidMonthlyPassesByDate(){
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-03-20").toLocalDate();
        boolean isLarge = true;
        String confirmationCode = "NeverGonnaGiveYouUp";

        double fee2 = 50.50;
        String spotNumber2 = "A25";
        String licensePlate2 = "123ABC124";
        LocalDate startDate2 = Date.valueOf("2023-02-22").toLocalDate();
        LocalDate endDate2 = Date.valueOf("2023-03-21").toLocalDate();
        boolean isLarge2 = true;
        String confirmationCode2 = "NeverGonnaGiveYouUp";
        
        MonthlyPass monthlyPass = new MonthlyPass();
        monthlyPass.setFee(fee);
        monthlyPass.setSpotNumber(spotNumber);
        monthlyPass.setConfirmationCode(confirmationCode);
        monthlyPass.setIsLarge(isLarge);
        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setLicensePlate(licensePlate);

        MonthlyPass monthlyPass2 = new MonthlyPass();
        monthlyPass2.setFee(fee2);
        monthlyPass2.setSpotNumber(spotNumber2);
        monthlyPass2.setConfirmationCode(confirmationCode2);
        monthlyPass2.setIsLarge(isLarge2);
        monthlyPass2.setStartDate(startDate2);
        monthlyPass2.setEndDate(endDate2);
        monthlyPass2.setLicensePlate(licensePlate2);

        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        monthlyPasses.add(monthlyPass2);

        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);

        PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.getMonthlyPassesByDate(Date.valueOf("2023-02-20").toLocalDate()));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "There are no monthly passes for date " + Date.valueOf("2023-02-20"));

    }

    @Test
    public void testCreateValidWithoutAccountMonthlyPass(){
      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      floor.setIsMemberOnly(true);
      floor.setLargeSpotCapacity(100);

      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setStartDate(startDate);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setFloor(floor);

      // Initialize parking lot
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLargeSpotFee(50);
        floor.setParkingLot(parkingLot);

      when(monthlyPassRepo.save(monthlyPass)).thenReturn(monthlyPass);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);

      MonthlyPass output = monthlyPassService.createMonthlyPass(monthlyPass, 1 , 2, null);

      assertNotNull(output);
      assertEquals(monthlyPass, output);
    }

    @Test
    public void testCreateValidWithAccountMonthlyPass(){
      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      floor.setIsMemberOnly(true);
      floor.setLargeSpotCapacity(100);

      String email = "rick.roll@gmail.com";
      String password = "intelliJLover123";
      String name = "Samer Abdulkarim";
      MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
      monthlyCustomer.setEmail(email);
      monthlyCustomer.setPassword(password);
      monthlyCustomer.setName(name);

      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setCustomer(monthlyCustomer);
      monthlyPass.setStartDate(startDate);

        // Initialize parking lot
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLargeSpotFee(50);
        floor.setParkingLot(parkingLot);

      when(monthlyPassRepo.save(monthlyPass)).thenReturn(monthlyPass);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);
      when(monthlyCustomerRepo.findMonthlyCustomerByEmail(email)).thenReturn(monthlyCustomer);

      MonthlyPass output = monthlyPassService.createMonthlyPass(monthlyPass, 1, 2, email);

      assertNotNull(output);
      assertEquals(monthlyPass, output);
    }

    @Test
    public void testCreateInvalidMonthlyPass2(){

      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";
      int id = 1;


      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setStartDate(startDate);

      when(monthlyPassRepo.findMonthlyPassById(id)).thenReturn(monthlyPass);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(null);

      PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.createMonthlyPass(monthlyPass, 1, 2, null));
      assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
      assertEquals(e.getMessage(), "The floor with floor number 1 does not exist.");  
    }

    @Test
    public void testCreateInvalidMonthlyPass3(){
      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";
      int id = 1;

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      floor.setIsMemberOnly(false);


      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setStartDate(startDate);

      when(monthlyPassRepo.findMonthlyPassById(id)).thenReturn(monthlyPass);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);

      PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.createMonthlyPass(monthlyPass, 1, 2, null));
      assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
      assertEquals(e.getMessage(), "Floor 1 is reserved for guest passes only.");  
    }

    @Test
    public void testCreateInvalidMonthlyPass4(){
      double fee = 50.50;
      String spotNumber = "A24";
      String licensePlate = "123ABC123";
      LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
      LocalDate endDate = Date.valueOf("2023-03-21").toLocalDate();
      boolean isLarge = true;
      String confirmationCode = "NeverGonnaGiveYouUp";
      int id = 1;

      Floor floor = new Floor();
      floor.setFloorNumber(1);
      floor.setIsMemberOnly(true);

      ParkingLot lot = new ParkingLot();
      lot.setLargeSpotFee(50);
      floor.setParkingLot(lot);

      double fee2 = 50.50;
      String spotNumber2 = "A24";
      String licensePlate2 = "123ABC124";
      boolean isLarge2 = true;
      LocalDate startDate2 = Date.valueOf("2023-02-21").toLocalDate();
      String confirmationCode2 = "NeverGonnaGiveYouUp";


      MonthlyPass monthlyPass = new MonthlyPass();
      monthlyPass.setFee(fee);
      monthlyPass.setSpotNumber(spotNumber);
      monthlyPass.setConfirmationCode(confirmationCode);
      monthlyPass.setIsLarge(isLarge);
      monthlyPass.setLicensePlate(licensePlate);
      monthlyPass.setStartDate(startDate);
      monthlyPass.setEndDate(endDate);
      monthlyPass.setFloor(floor);

      MonthlyPass monthlyPass2 = new MonthlyPass();
      monthlyPass2.setFee(fee2);
      monthlyPass2.setSpotNumber(spotNumber2);
      monthlyPass2.setConfirmationCode(confirmationCode2);
      monthlyPass2.setIsLarge(isLarge2);
      monthlyPass2.setLicensePlate(licensePlate2);
      monthlyPass2.setStartDate(startDate2);

        // Initialize parking lot
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setLargeSpotFee(50);
        floor.setParkingLot(parkingLot);

        // Attempt to create GuestPass for a spot that is not available


        ArrayList<MonthlyPass> monthlyPasses = new ArrayList<>();
        monthlyPasses.add(monthlyPass);
        when(monthlyPassRepo.findAll()).thenReturn(monthlyPasses);

      when(monthlyPassRepo.findMonthlyPassById(id)).thenReturn(monthlyPass);
      when(floorRepo.findFloorByFloorNumber(1)).thenReturn(floor);

      PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.createMonthlyPass(monthlyPass2, 1, 2, null));
      assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
      assertEquals(e.getMessage(), "Spot A24 is currently occupied");  
    }



  @Test
  void testCreateMonthlyPassExceededCapacity1() {
    int floorNumber = 1;
    int nrIncrements = 4;
    LocalDate currentDate = Date.valueOf(LocalDate.of(2023, 4, 1)).toLocalDate();
    LocalDate startDate2 = Date.valueOf(LocalDate.of(2020, 4, 1)).toLocalDate();
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
    floor.setIsMemberOnly(true);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // // Initialize guest passes
    // GuestPass guestPass1 = new GuestPass();
    // guestPass1.setSpotNumber("A24");
    // guestPass1.setIsLarge(true);
    // guestPass1.setFloor(floor);
    // guestPass1.setDate(currentDate);
    // guestPass1.setStartTime(Time.valueOf("10:00:00"));
    // guestPass1.setEndTime(Time.valueOf("14:00:00"));

    // GuestPass guestPass2 = new GuestPass();
    // guestPass2.setSpotNumber("A25");
    // guestPass2.setIsLarge(true);
    // guestPass2.setFloor(floor);
    // guestPass2.setDate(startDate2);
    // guestPass2.setStartTime(Time.valueOf("10:45:00"));
    // guestPass2.setEndTime(Time.valueOf("14:00:00"));

    // Initialize monthly passes
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
    // guestPassList.add(guestPass1);
    // guestPassList.add(guestPass2);
    monthlyPassList.add(monthlyPass2);
    monthlyPassList.add(monthlyPass1);
    when(guestPassRepo.findAll()).thenReturn(guestPassList);
    when(monthlyPassRepo.findAll()).thenReturn(monthlyPassList);

    // Test with exceeded capacity
    MonthlyPass monthlyPass3 = new MonthlyPass();
    monthlyPass3.setSpotNumber("A26");
    monthlyPass3.setIsLarge(true);
    monthlyPass3.setStartDate(currentDate);

    PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.createMonthlyPass(monthlyPass3, floorNumber, nrIncrements, null));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "All spots of this size on floor " + floorNumber +" are occupied.");
  }


  @Test
  /**
   * Test creating a small monthly pass when the capacity has been exceeded
   */
  void testCreateMonthlyPassExceededCapacity2() {
    int floorNumber = 1;
    int nrIncrements = 4;
    LocalDate currentDate = Date.valueOf(LocalDate.of(2023, 4, 1)).toLocalDate();
    LocalDate startDate2 = Date.valueOf(LocalDate.of(2020, 4, 1)).toLocalDate();

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
    floor.setIsMemberOnly(true);
    when(floorRepo.findFloorByFloorNumber(floorNumber)).thenReturn(floor);

    // // Initialize guest passes
    // GuestPass guestPass1 = new GuestPass();
    // guestPass1.setSpotNumber("A24");
    // guestPass1.setIsLarge(false);
    // guestPass1.setFloor(floor);
    // guestPass1.setDate(currentDate);
    // guestPass1.setStartTime(Time.valueOf("10:00:00"));
    // guestPass1.setEndTime(Time.valueOf("14:00:00"));

    // GuestPass guestPass2 = new GuestPass();
    // guestPass2.setSpotNumber("A25");
    // guestPass2.setIsLarge(false);
    // guestPass2.setFloor(floor);
    // guestPass2.setDate(startDate2);
    // guestPass2.setStartTime(Time.valueOf("10:45:00"));
    // guestPass2.setEndTime(Time.valueOf("14:00:00"));

    // Initialize monthly passes
    MonthlyPass monthlyPass1 = new MonthlyPass();
    monthlyPass1.setSpotNumber("A24");
    monthlyPass1.setIsLarge(false);
    monthlyPass1.setFloor(floor);
    monthlyPass1.setStartDate(currentDate);
   LocalDate endDate1 = Date.valueOf(LocalDate.of(2024, 4, 1)).toLocalDate();
    monthlyPass1.setEndDate(endDate1);

    // inactive monthly pass
    MonthlyPass monthlyPass2 = new MonthlyPass();
    monthlyPass2.setSpotNumber("A25");
    monthlyPass2.setIsLarge(false);
    monthlyPass2.setFloor(floor);
    LocalDate endDate2 = Date.valueOf(LocalDate.of(2020, 5, 1)).toLocalDate();
    monthlyPass2.setStartDate(startDate2);
    monthlyPass2.setEndDate(endDate2);

    ArrayList<GuestPass> guestPassList = new ArrayList<>();
    ArrayList<MonthlyPass> monthlyPassList = new ArrayList<>();
    // guestPassList.add(guestPass1);
    // guestPassList.add(guestPass2);
    monthlyPassList.add(monthlyPass2);
    monthlyPassList.add(monthlyPass1);
    when(guestPassRepo.findAll()).thenReturn(guestPassList);
    when(monthlyPassRepo.findAll()).thenReturn(monthlyPassList);

    // Test with exceeded capacity
    MonthlyPass monthlyPass3 = new MonthlyPass();
    monthlyPass3.setSpotNumber("A26");
    monthlyPass3.setIsLarge(false);
    monthlyPass3.setStartDate(currentDate);

    PLMSException e = assertThrows(PLMSException.class, () -> monthlyPassService.createMonthlyPass(monthlyPass3, floorNumber, nrIncrements, null));
    assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    assertEquals(e.getMessage(), "All spots of this size on floor " + floorNumber +" are occupied.");
  }
    


    











    
    
}