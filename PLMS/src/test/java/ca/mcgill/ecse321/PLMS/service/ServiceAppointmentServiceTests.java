package ca.mcgill.ecse321.PLMS.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import ca.mcgill.ecse321.PLMS.repository.ServiceAppointmentRepository;

@SpringBootTest
public class ServiceAppointmentServiceTests {
  @Mock
  private ServiceAppointmentRepository serviceAppointmentRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private MonthlyCustomerRepository monthlyCustomerRepository;

  @Mock
  private ParkingLotRepository parkingLotRepository;

  @InjectMocks
  private ServiceAppointmentService serviceAppointmentService;

  @Test
  /**
   * Test getting all the appointments from the database
   */
  public void testGetAllAppointments(){
    //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("18:00:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    // The parking lot repo should return a single parking lot
    ArrayList<ServiceAppointment> appts = new ArrayList<ServiceAppointment>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn((Iterable<ServiceAppointment>) appts);
    Iterable<ServiceAppointment> output = serviceAppointmentService.getAllServiceAppointments();
    assertEquals(output.iterator().next(), appt);
        
  }

  @Test
  /**
   * Test getting an appointment that in the database, based on its id.
   */
  public void testGetValidAppointment(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    int id = 4;
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("18:00:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    when(serviceAppointmentRepository.findServiceAppointmentById(4)).thenReturn(appt);
    ServiceAppointment output = serviceAppointmentService.findServiceAppointmentById(id);
    assertEquals(date, output.getDate());
    assertEquals(startTime, output.getStartTime());
    assertEquals(endTime, output.getEndTime());
    assertEquals(serviceName, output.getService().getServiceName());
  }


  @Test
  /**
   * Test getting an appointment that doesnt exist in the DB.
   */
  public void testGetInvalidAppointment(){
    // random id for testing
    int id = 4;
    when(serviceAppointmentRepository.findServiceAppointmentById(4)).thenReturn(null);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.findServiceAppointmentById(id));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("Service appointment with ID " + id + " does not exist.", e.getMessage());
  }

  @Test
  /**
   * Test getting all the appointments associated with an account.
   */
  public void testCreateValidAppointmentWithAccounts(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("12:45:00");
    // CHECK TO SEE THAT TIME CALCULATION IS CORRECT
    Time endTime2 = Time.valueOf("12:30:00");
    String eEmail = "jeff.jeff@jeff.com";
    String password = "PasswordSuperSecured12345";
    String name = "Jeff";
    String jobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
    int hourlyWage = 15;
    Employee jeff = new Employee(eEmail, password, name, jobDescription, hourlyWage);

    String mEmail = "patrick@dorsia.com";
    String mPassword = "ihavetoreturnsomevideotapes";
    String mName = "Patrick Bateman";
    MonthlyCustomer pat = new MonthlyCustomer(mEmail, mPassword, mName);


    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    //--------------------------------//

    
    // The parking lot repo should return a single parking lot
    ArrayList<ParkingLot> lot = new ArrayList<>();
    lot.add(parkingLot);
    ArrayList<Employee> employees = new ArrayList<>();
    employees.add(jeff);
    ArrayList<MonthlyCustomer> customers = new ArrayList<>();
    customers.add(pat);
    
    when(parkingLotRepository.findAll()).thenReturn(lot);
    when(employeeRepository.findAll()).thenReturn(employees);
    when(monthlyCustomerRepository.findAll()).thenReturn(customers);
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    appt.setEmployee(jeff);
    appt.setCustomer(pat);
    when(serviceAppointmentRepository.save(appt)).thenReturn(appt);

    ServiceAppointment output = serviceAppointmentService.createServiceAppointment(appt);

    assertEquals(date, output.getDate());
    assertEquals(startTime, output.getStartTime());
    assertEquals(endTime2, output.getEndTime());
    assertEquals(serviceName, output.getService().getServiceName());
    assertEquals(name, output.getEmployee().getName());
    assertEquals(mName, output.getCustomer().getName());
  }

  @Test
  /**
   * Test getting appointments when no appointments are in the DB.
   */
  public void testGetAllInvalidServiceAppointments(){
    ArrayList<ServiceAppointment> serviceAppts = new ArrayList<ServiceAppointment>();
    when(serviceAppointmentRepository.findAll()).thenReturn((Iterable<ServiceAppointment>)serviceAppts);
    PLMSException e = assertThrows(PLMSException.class,
      () -> serviceAppointmentService.getAllServiceAppointments());
    assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    assertEquals("There are no service appointments in the system", e.getMessage());
  }


  @Test
  /**
   * Test creating appointments when person booking doesn't have an account,
   * as well as booking an appointment when there's no employees.
   */
  public void testCreateValidAppointmentWithoutAccounts(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("18:00:00");
    Time endTime2 = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    //--------------------------------//

    
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));

    when(serviceAppointmentRepository.save(appt)).thenReturn(appt);
    ServiceAppointment output = serviceAppointmentService.createServiceAppointment(appt);
    assertEquals(date, output.getDate());
    assertEquals(startTime, output.getStartTime());
    assertEquals(endTime2, output.getEndTime());
    assertEquals(serviceName, output.getService().getServiceName());
    assertEquals(null, output.getCustomer());
    assertEquals(null, output.getEmployee());
  }

  @Test
  /**
   * We cannot create a service appointment before the parking lot has been made.
   */
  public void testCreateAppointmentBeforeLot(){
    // there is no parking lot, so we cannot book an appointment
    when(parkingLotRepository.findAll()).thenReturn(new ArrayList<ParkingLot>());
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("18:00:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);

    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.createServiceAppointment(appt));
		assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		assertEquals("Cannot book appointment since the parking lot has not been created yet. Please try again at a later date.", e.getMessage());
  }

  @Test
  /**
   * Test deleting an appointment that is not in the database  
   */ 
  public void testInvalidAppointmentDeletion(){
    int id = 4;
    when(serviceAppointmentRepository.findById(id)).thenReturn(null);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.deleteServiceAppointmentById(id));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("Service appointment with ID " + id + " does not exist.", e.getMessage());
  }

  @Test
  /**
   * Test updating an appointment that's in the database
   */
  public void testValidAppointmentUpdate(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate firstDate = LocalDate.of(2023,02,21);
    LocalDate secondDate = LocalDate.of(2024,02,21);
    Time firstStartTime = Time.valueOf("18:00:00");
    Time firstEndTime = Time.valueOf("18:30:00");
    Time secondStartTime = Time.valueOf("12:00:00");
    Time secondEndTime = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(firstDate, firstStartTime, firstEndTime, service);
    // test the time calculation
    ServiceAppointment appt2 = new ServiceAppointment(secondDate, secondStartTime, null, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    int id = 10;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));

    when(serviceAppointmentRepository.findServiceAppointmentById(id)).thenReturn(appt);
    when(serviceAppointmentRepository.save(appt)).thenReturn(appt);
    ServiceAppointment output = serviceAppointmentService.updateServiceAppointment(appt2, id);

    assertEquals(secondDate, output.getDate());
    assertEquals(secondStartTime, output.getStartTime());
    assertEquals(secondEndTime, output.getEndTime());
  }

  @Test
  /**
   * Test get all appointments that are scheduled for a day.
   */
  public void testGetAllAppointmentsOnDate(){
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate firstDate = LocalDate.of(2023,02,21);
    LocalDate secondDate = LocalDate.of(2024,02,21);
    Time firstStartTime = Time.valueOf("18:00:00");
    Time firstEndTime = Time.valueOf("18:30:00");
    Time secondStartTime = Time.valueOf("12:00:00");
    Time secondEndTime = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(firstDate, firstStartTime, firstEndTime, service);
    // test the time calculation
    ServiceAppointment appt2 = new ServiceAppointment(secondDate, secondStartTime, secondEndTime, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
    // return both appointments for find all
    ArrayList<ServiceAppointment> testData = new ArrayList<ServiceAppointment>();
    testData.add(appt);
    testData.add(appt2);
    when(serviceAppointmentRepository.findAll()).thenReturn((Iterable<ServiceAppointment>) testData);
    Iterable<ServiceAppointment> appts = serviceAppointmentService.getAllServiceAppointmentsByDate(firstDate);
    Iterator<ServiceAppointment> it = appts.iterator();
    ServiceAppointment output = it.next();
    assertEquals(firstDate, output.getDate());
    assertEquals(firstStartTime, output.getStartTime());
    assertEquals(firstEndTime, output.getEndTime());
    // ensure there is only one appointment on this date
    assertFalse(it.hasNext());
  }

  @Test
  /**
   * Test getting appointments on a day for which there are no appointments scheduled
   */
  public void testGetAllAppointmentsOnInvalidDate(){
    LocalDate invalidDate = LocalDate.of(2024,02,21);
    //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("18:00:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    // The parking lot repo should return a single parking lot
    ArrayList<ServiceAppointment> appts = new ArrayList<ServiceAppointment>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.getAllServiceAppointmentsByDate(invalidDate));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("There are no appointments on date " + invalidDate, e.getMessage());
  }

  @Test
  /**
   * Test getting all service appointments that are scheduled for an employee's account.
   */
  public void testGetAllAppointmentsByEmployee(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("12:30:00");
    String email = "jeff.jeff@jeff.com";
    String password = "PasswordSuperSecured12345";
    String name = "Jeff";
    String jobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
    int hourlyWage = 15;
    Employee jeff = new Employee(email, password, name, jobDescription, hourlyWage);
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    appt.setEmployee(jeff);
    ArrayList<ServiceAppointment> appts = new ArrayList<>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    Iterable<ServiceAppointment> outputAppts = serviceAppointmentService.getAllServiceAppointmentsByEmployee(email);
    Iterator<ServiceAppointment> it = outputAppts.iterator();
    ServiceAppointment output = it.next();
    assertEquals(date, output.getDate());
    assertEquals(startTime, output.getStartTime());
    assertEquals(endTime, output.getEndTime());
    assertEquals(email, output.getEmployee().getEmail());
    // ensure there is only one appointment on this date
    assertFalse(it.hasNext());

  }

  @Test
  /**
   * Test getting all the appointments scheduled by a customer's account.
   */
  public void testGetAllAppointmentsByMonthlyCustomer(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    String mEmail = "patrick@dorsia.com";
    String mPassword = "ihavetoreturnsomevideotapes";
    String mName = "Patrick Bateman";
    MonthlyCustomer pat = new MonthlyCustomer(mEmail, mPassword, mName);
    appt.setCustomer(pat);
    ArrayList<ServiceAppointment> appts = new ArrayList<>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    Iterable<ServiceAppointment> outputAppts = serviceAppointmentService.getAllServiceAppointmentsByMonthlyCustomer(mEmail);
    Iterator<ServiceAppointment> it = outputAppts.iterator();
    ServiceAppointment output = it.next();
    assertEquals(date, output.getDate());
    assertEquals(startTime, output.getStartTime());
    assertEquals(endTime, output.getEndTime());
    assertEquals(mEmail, output.getCustomer().getEmail());
    // ensure there is only one appointment on this date
    assertFalse(it.hasNext());

  }

  @Test
  /**
   * Trying to schedule on an invalid time (cannot book before the lot has opened)
   */
  public void testCreateInvalidStartTime1(){
    // test for creating appointment before lot opens
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate date = LocalDate.of(2023,02,21);
    // before operating hours
    Time startTime = Time.valueOf("5:00:00");
    Time endTime = Time.valueOf("5:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.createServiceAppointment(appt));
		assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		assertEquals("Cannot have an appointment beginning before the lot opens.", e.getMessage());
  }

  @Test
  /**
   * Trying to schedule on an invalid time (cannot book after the lot has closed)
   */
  public void testCreateInvalidStartTime2(){
    // test for creating appointment before lot opens
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate date = LocalDate.of(2023,02,21);
    // before operating hours
    Time startTime = Time.valueOf("23:00:00");
    Time endTime = Time.valueOf("23:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.createServiceAppointment(appt));
		assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		assertEquals("Cannot have an appointment beginning after the lot closes.", e.getMessage());
  }

  @Test
  /**
   * Trying to schedule on an invalid time (having the end time after the lot closes)
   */
  public void testCreateInvalidEndTime(){
    String serviceName = "2 hour Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 2;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate date = LocalDate.of(2023,02,21);
    // before operating hours
    Time startTime = Time.valueOf("21:00:00");
    Time endTime = Time.valueOf("23:00:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);

    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.createServiceAppointment(appt));
		assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
		assertEquals("Cannot have an appointment ending after the lot closes.", e.getMessage());
  }

  @Test
  /**
   * Scheduling a service appointment when there is no employee available.
   * We simply just schedule anyways, and employee can be assigned later.
   */
  public void testCreateWithSchedulingConflict(){
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    LocalDate firstDate = LocalDate.of(2023,02,21);
    LocalDate secondDate = LocalDate.of(2023,02,21);
    Time firstStartTime = Time.valueOf("17:00:00");
    Time firstEndTime = Time.valueOf("17:30:00");
    Time secondStartTime = Time.valueOf("17:15:00");
    Time secondEndTime = Time.valueOf("17:45:00");
    String eEmail = "jeff.jeff@jeff.com";
    String password = "PasswordSuperSecured12345";
    String name = "Jeff";
    String jobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
    int hourlyWage = 15;
    Employee jeff = new Employee(eEmail, password, name, jobDescription, hourlyWage);
    ArrayList<Employee> employees = new ArrayList<>();
    employees.add(jeff);
    when(employeeRepository.findAll()).thenReturn(employees);
    ServiceAppointment appt = new ServiceAppointment(firstDate, firstStartTime, firstEndTime, service);
    appt.setEmployee(jeff);
    // test the time calculation
    ServiceAppointment appt2 = new ServiceAppointment(secondDate, secondStartTime, secondEndTime, service);

    ArrayList<ServiceAppointment> appts = new ArrayList<>();
    appts.add(appt);
    appts.add(appt2);
    Time openingTime = Time.valueOf("6:00:00");
    Time closingTime = Time.valueOf("22:00:00");
    double smallSpotFee = 3.5;
    double largeSpotFee = 4.5;
    double smallSpotMonthlyFlatFee = 150;
    double largeSpotMonthlyFlatFee = 150;
    ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, smallSpotFee, largeSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
    // The parking lot repo should return a single parking lot
    when(parkingLotRepository.findAll()).thenReturn(Collections.singletonList(parkingLot));
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    when(serviceAppointmentRepository.save(appt2)).thenReturn(appt2);
    ServiceAppointment output = serviceAppointmentService.createServiceAppointment(appt2);
    assertNull(output.getEmployee());
  }

  @Test
  /**
   * Get all the appointments assigned to an employee
   */
  public void testGetAllInvalidAppointmentsByEmployee(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    String eEmail = "jeff.jeff@jeff.com";
    String password = "PasswordSuperSecured12345";
    String name = "Jeff";
    String jobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
    int hourlyWage = 15;
    Employee jeff = new Employee(eEmail, password, name, jobDescription, hourlyWage);
    ArrayList<Employee> employees = new ArrayList<>();
    employees.add(jeff);
    ArrayList<ServiceAppointment> appts = new ArrayList<>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    when(employeeRepository.findAll()).thenReturn(employees);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.getAllServiceAppointmentsByEmployee(eEmail));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("There are no service appointments for employee " + eEmail, e.getMessage());
  }

  @Test
  /**
   * No appointments for the customer
   */
  public void testGetAllInvalidAppointmentsByMonthlyCustomer(){
    // //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
    String serviceName = "30 min Car Wash";
    int serviceCost = 30;
    double serviceLengthInHours = 0.5;
    Service service = new Service(serviceName, serviceCost, serviceLengthInHours);
    //normal parameters
    LocalDate date = LocalDate.of(2023,02,21);
    Time startTime = Time.valueOf("12:00:00");
    Time endTime = Time.valueOf("12:30:00");
    ServiceAppointment appt = new ServiceAppointment(date, startTime, endTime, service);
    String mEmail = "patrick@dorsia.com";
    String mPassword = "ihavetoreturnsomevideotapes";
    String mName = "Patrick Bateman";
    MonthlyCustomer pat = new MonthlyCustomer(mEmail, mPassword, mName);
    ArrayList<MonthlyCustomer> customers = new ArrayList<>();
    customers.add(pat);
    ArrayList<ServiceAppointment> appts = new ArrayList<>();
    appts.add(appt);
    when(serviceAppointmentRepository.findAll()).thenReturn(appts);
    when(monthlyCustomerRepository.findAll()).thenReturn(customers);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.getAllServiceAppointmentsByMonthlyCustomer(mEmail));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("There are no service appointments for customer " + mEmail, e.getMessage());
  }

  @Test
  /**
   * Attempt to update an appointment that doesn't exist
   */
  public void testInvalidAppointmentUpdate(){
    when(serviceAppointmentRepository.findServiceAppointmentById(10)).thenReturn(null);
    PLMSException e = assertThrows(PLMSException.class,
				() -> serviceAppointmentService.updateServiceAppointment(new ServiceAppointment(), 10));
		assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
		assertEquals("Service appointment is not found.", e.getMessage());
  }
}
