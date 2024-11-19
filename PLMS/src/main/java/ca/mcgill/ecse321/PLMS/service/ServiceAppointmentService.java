package ca.mcgill.ecse321.PLMS.service;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import ca.mcgill.ecse321.PLMS.repository.ServiceAppointmentRepository;
import jakarta.transaction.Transactional;

/**
 * Service class for all the business methods related to the service appointment model class in the PLMS system
 */
@Service
public class ServiceAppointmentService {
  
  @Autowired
  ServiceAppointmentRepository serviceAppointmentRepo; // Repository from where the service appointment objects are persisted
  @Autowired
  ParkingLotRepository parkingLotRepository; // Repository from where the parking lot objects are persisted
  @Autowired
  EmployeeRepository employeeRepository; // Repository from where the employee account objects are persisted

  /**
   * Creates a service appointment. Checks for scheduling issues and assigns employee
   * if there is one before saving in database
   * @param serviceAppointment - appt to schedule
   * @return - scheduled appt
   */
  @Transactional
  public ServiceAppointment createServiceAppointment(ServiceAppointment serviceAppointment){
    // first calculate the end time of the service appointment by using the length of the appointment
    LocalTime localEndTime = findEndTime(serviceAppointment.getStartTime().toLocalTime(), serviceAppointment);
    serviceAppointment.setEndTime(Time.valueOf(localEndTime));
    
    // check for the parking lot in the database, if it doesn't exist yet we cannot create the appointment
    ParkingLot lot = parkingLotAddedToDatabase();

    // check to see if the hours are within lot hours of operation
    validateAppointmentHours(serviceAppointment.getStartTime(), serviceAppointment.getEndTime(), lot.getOpeningTime(), lot.getClosingTime());

    // we randomly assign employees, if there are any.
    serviceAppointment.setEmployee(findEmployeeToAssignToAppointment(serviceAppointment));

    ServiceAppointment appointment = serviceAppointmentRepo.save(serviceAppointment);
    return appointment;
  }

  /**
   * Find the end time of the appointment based on the length of the appointment
   * @param startTime - start time of the appointment 
   * @param serviceAppointment - appointment
   * @return - the end time of the appointment
   */
  public LocalTime findEndTime(LocalTime startTime, ServiceAppointment serviceAppointment){
    LocalTime localStartTime = serviceAppointment.getStartTime().toLocalTime();
    int hours = (int) serviceAppointment.getService().getLengthInHours();
    int minutes = (int) ((serviceAppointment.getService().getLengthInHours() - hours) * 60);
    LocalTime localEndTime = localStartTime.plusHours(hours).plusMinutes(minutes);
    return localEndTime;
  }

  /**
   * Get all the service appointments in the entire database
   * @return an arraylist of service appointments if successful
   */
  @Transactional
  public Iterable<ServiceAppointment> getAllServiceAppointments(){
    ArrayList<ServiceAppointment> arrayList = (ArrayList<ServiceAppointment>) serviceAppointmentRepo.findAll();
        if (arrayList.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no service appointments in the system");
    return serviceAppointmentRepo.findAll();
  }

  /**
   * Get a service appointment based on the id.
   * @param id - id of the appointment
   * @return appt if it exists
   */
  @Transactional
  public ServiceAppointment findServiceAppointmentById(int id){
    ServiceAppointment appointment = serviceAppointmentRepo.findServiceAppointmentById(id);
    if (appointment == null){
      throw new PLMSException(HttpStatus.NOT_FOUND, "Service appointment with ID " + id + " does not exist.");
    }
    return appointment;
  }

  /**
   * Find all the service appointments that are scheduled for a particular date.
   * @param date - date for which we want to find appointments
   * @return all appointments on that date, if there are any
   */
  @Transactional
  public Iterable<ServiceAppointment> getAllServiceAppointmentsByDate(LocalDate date){
    // first find all the appointments
    // iterate over them and add them to a new iterable list
    // return that list
    Iterable<ServiceAppointment> appointments = getAllServiceAppointments();
    ArrayList<ServiceAppointment> appointmentsOnDate = new ArrayList<ServiceAppointment>();
    for(ServiceAppointment appt : appointments){
      if (date.equals(appt.getDate())){
        // date matches, add it
        appointmentsOnDate.add(appt);
      }
    }
    if (appointmentsOnDate.isEmpty()){
      throw new PLMSException(HttpStatus.NOT_FOUND, "There are no appointments on date " + date);
    }
    // return the list
    return appointmentsOnDate;
  }

  /**
   * Find the all the service appointments for an employee
   * @param employeeEmail - employee for which we want to find appointments
   * @return all appointments for this employee, if there are any
   */
  @Transactional
  public Iterable<ServiceAppointment> getAllServiceAppointmentsByEmployee(String employeeEmail){
    Iterable<ServiceAppointment> appointments = getAllServiceAppointments();
    ArrayList<ServiceAppointment> appointmentsForEmployee = new ArrayList<ServiceAppointment>();
    for(ServiceAppointment appt : appointments){
      // check to see if employee is null
      if (appt.getEmployee() != null){
        // check to see if the employee's email matches the request email
        if(appt.getEmployee().getEmail().equals(employeeEmail)){
          appointmentsForEmployee.add(appt);
        }
      }
    }

    if (appointmentsForEmployee.isEmpty()){
      // null means service appointment doesn't exist, throw PLMS error
      throw new PLMSException(HttpStatus.NOT_FOUND, "There are no service appointments for employee " + employeeEmail);
    }
    // return the list
    return appointmentsForEmployee;
  }

  /**
   * Find all the service appointments for a monthly customer, if there are any
   * @param monthlyCustomerEmail - email of customer we want to find appointments for
   * @return all appointments with the customer
   */
  @Transactional
  public Iterable<ServiceAppointment> getAllServiceAppointmentsByMonthlyCustomer(String monthlyCustomerEmail){
    Iterable<ServiceAppointment> appointments = getAllServiceAppointments();
    ArrayList<ServiceAppointment> appointmentsForCustomer = new ArrayList<ServiceAppointment>();
    for(ServiceAppointment appt : appointments){
      // check to see if employee is null
      if (appt.getCustomer() != null){
        // check to see if the employee's email matches the request email
        if(appt.getCustomer().getEmail().equals(monthlyCustomerEmail)){
          appointmentsForCustomer.add(appt);
        }
      }
    }

    if (appointmentsForCustomer.isEmpty()){
      // null means service appointment doesn't exist, throw PLMS error
      throw new PLMSException(HttpStatus.NOT_FOUND, "There are no service appointments for customer " + monthlyCustomerEmail);
    }
    // return the list
    return appointmentsForCustomer;
  }

  /**
   * Delete a service appointment by an id.
   * @param id id of the service appointment to be deleted
   */
  @Transactional
  public void deleteServiceAppointmentById(int id){
    // first get the service appointment by id
    // ensure the appointment exists; throw error saying it doesn't exist if its not there as we want the user to be notified of this case
    // once ensured its not null, delete the appointmentment from DB
    ServiceAppointment serviceAppointmentToDelete = serviceAppointmentRepo.findServiceAppointmentById(id);
    if (serviceAppointmentToDelete == null){
      // null means service appointment doesn't exist, throw PLMS error
      throw new PLMSException(HttpStatus.NOT_FOUND, "Service appointment with ID " + id + " does not exist.");
    }
    
    // the appointment exists, so delete it
    serviceAppointmentRepo.deleteById(id);
  }

  /**
   * Update a service appointment's attributes, based on the id of the appt.
   * @param serviceAppointment - appointment to update
   * @param id - id of the appt
   * @return updated appt
   */
  @Transactional
  public ServiceAppointment updateServiceAppointment(ServiceAppointment serviceAppointment, int id){
    // first calculate the end time of the service appointment by using the length of the appointment
    ServiceAppointment updatedAppointment = serviceAppointmentRepo.findServiceAppointmentById(id);
    if (updatedAppointment == null){
      throw new PLMSException(HttpStatus.NOT_FOUND, "Service appointment is not found.");
    }

    updatedAppointment.setStartTime(serviceAppointment.getStartTime());
    LocalTime localEndTime = findEndTime(serviceAppointment.getStartTime().toLocalTime(), serviceAppointment);
    updatedAppointment.setEndTime(Time.valueOf(localEndTime));
    updatedAppointment.setDate(serviceAppointment.getDate());
    updatedAppointment.setId(id);
    updatedAppointment.setService(serviceAppointment.getService());
    // check for the parking lot in the database, if it doesn't exist yet we cannot create the appointment
    ParkingLot lot = parkingLotAddedToDatabase();

    // check to see if the hours are within lot hours of operation
    validateAppointmentHours(updatedAppointment.getStartTime(), updatedAppointment.getEndTime(), lot.getOpeningTime(), lot.getClosingTime());
    

    // we randomly assign employees, if there are any.
    updatedAppointment.setEmployee(findEmployeeToAssignToAppointment(updatedAppointment));
    ServiceAppointment appointment = serviceAppointmentRepo.save(updatedAppointment);
    return appointment;
  }

  /**
   * Service method to update the employee responsable for a service appointment
   * @param employee new employee responsable fro the service appointment
   * @param id service appointment's id
   * @return instance of the updated service appointment
   */
  @Transactional
  public ServiceAppointment updateEmployeeEmailServiceAppointment(Employee employee, int id){
    // first calculate the end time of the service appointment by using the length of the appointment
    ServiceAppointment updatedAppointment = serviceAppointmentRepo.findServiceAppointmentById(id);
    if (updatedAppointment == null){
      throw new PLMSException(HttpStatus.NOT_FOUND, "Service appointment is not found.");
    }
    
    // we randomly assign employees, if there are any.
    if (employee != null) updatedAppointment.setEmployee(checkForConflictInEmployeeScheedule(updatedAppointment, employee.getEmail(), employee));
    ServiceAppointment appointment = serviceAppointmentRepo.save(updatedAppointment);
    return appointment;
  }

  /**
   * Service method to check for conflicts in employee schedule
   * @param appointment appointment that is compared with the rest for overlapping checking
   * @return
   */
  public Employee checkForConflictInEmployeeScheedule(ServiceAppointment appointment, String employeeEmail, Employee employee){
    
    ArrayList<ServiceAppointment> allAppts = (ArrayList<ServiceAppointment>) serviceAppointmentRepo.findAll();
    
    // get all the appointments for the current employee
    ArrayList<ServiceAppointment> appointmentsForEmployee = new ArrayList<ServiceAppointment>();
    for(ServiceAppointment appt : allAppts){
      if(appt.getEmployee() != null && appt.getEmployee().getEmail().equals(employeeEmail) && appt.getId() != appointment.getId()) appointmentsForEmployee.add(appt);
    }

    if(appointmentsForEmployee.isEmpty()) {
      return employee;
  }
    // now check for scheduling conflicts in all of their appointments
    boolean hasConflictingAppointment = false;
    for(ServiceAppointment appt : appointmentsForEmployee){
      hasConflictingAppointment = isConflicting(appointment, appt);
    }
    if (!hasConflictingAppointment) return employee;
    throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot change the employee because requested employee already has an appointment during the time frame of this appointment.");
  }

  /**
   * Method to find assigned employee to service appointment.
   * Employees are assigned based on prior scheduling conflicts. We also allow for 
   * appointments to have a null employee, as the user should be allowed to have access
   * to scheduling appointments, regardless of whether employees are there. Marwan stated that scheduling is
   * not a priority (we simply booked appointments and just assign an employee)
   * @return - null if no employees to schedule, or an employee with no scheduling conflicts
   */
  public Employee findEmployeeToAssignToAppointment(ServiceAppointment appointment){
    ArrayList<Employee> employees = (ArrayList<Employee>)employeeRepository.findAll();
    // we won't restrict a user from booking an appointment if there aren't any employees
    if (employees.size() == 0){
      return null;
    }
    ArrayList<ServiceAppointment> allAppts = (ArrayList<ServiceAppointment>) serviceAppointmentRepo.findAll();
    // iterate over employees to find the first employee that has no scheduling conflict for the current appointment
    for (Employee employee : employees){
      // get all the appointments for the current employee
      ArrayList<ServiceAppointment> appointmentsForEmployee = new ArrayList<>();
      for(ServiceAppointment appt : allAppts){
        if(appt.getEmployee() != null && appt.getEmployee().getEmail().equals(employee.getEmail())) appointmentsForEmployee.add(appt);
      }
      // now check for scheduling conflicts in all of their appointments
      boolean hasConflictingAppointment = false;
      for(ServiceAppointment appt : appointmentsForEmployee){
        hasConflictingAppointment = isConflicting(appointment, appt);
      }
      if (!hasConflictingAppointment) return employee;

    }
    
    return null;
  }

  /**
   * Function to check if two appointments have time overlap. Used for scheduling employees.
   * @param appt1 first appointment
   * @param appt2 second appointment
   * @return whether or not there is a conflict between appointments
   */
  public boolean isConflicting(ServiceAppointment appt1, ServiceAppointment appt2) {
    LocalDateTime start1 = LocalDateTime.of(appt1.getDate(), appt1.getStartTime().toLocalTime());
    LocalDateTime end1 = LocalDateTime.of(appt1.getDate(), appt1.getEndTime().toLocalTime());

    LocalDateTime start2 = LocalDateTime.of(appt2.getDate(), appt2.getStartTime().toLocalTime());
    LocalDateTime end2 = LocalDateTime.of(appt2.getDate(), appt2.getEndTime().toLocalTime());
    
    return start1.isBefore(end2) && end1.isAfter(start2) || (start2.isBefore(end1) && end2.isAfter(start1));
}
  /**
   * Helper method to find the single parking lot object
   * stored in the database. Throws exception if it isn't there.
   * @return - parking lot object, if it exists
   */
  public ParkingLot parkingLotAddedToDatabase(){
    Iterable<ParkingLot> lots = parkingLotRepository.findAll();
    // if no lot in the system, we cannot book an appointment
    if (!lots.iterator().hasNext()){
        throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot book appointment since the parking lot has not been created yet. Please try again at a later date.");
    }
    // return the parking lot
    return lots.iterator().next();
  }

  /**
   * Helper function to throw PLMS exceptions related to scheduling errors.
   * Appointments must fall within the hours of operation of the lot.
   * @param apptStartTime
   * @param apptEndTime
   * @param openingTime
   * @param closingTime
   */
  public void validateAppointmentHours(Time apptStartTime, Time apptEndTime, Time openingTime, Time closingTime){

    // check to see if start time is before the opening time
    int comparisonResult1 = apptStartTime.compareTo(openingTime);
    if (comparisonResult1 < 0) {
      throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have an appointment beginning before the lot opens.");
    } 

    // check to see if the start time is after the lot closes
    int comparisonResult2 = apptStartTime.compareTo(closingTime);
    if (comparisonResult2 > 0) {
      throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have an appointment beginning after the lot closes.");
    } 

    // check to see if the end time is after the lot closes
    int comparisonResult3 = apptEndTime.compareTo(closingTime);
    if (comparisonResult3 > 0) {
      throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have an appointment ending after the lot closes.");
    } 

  }
}
