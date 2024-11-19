package ca.mcgill.ecse321.PLMS.controller;

import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.PLMS.dto.ServiceAppointmentRequestDto;
import ca.mcgill.ecse321.PLMS.dto.ServiceAppointmentResponseDto;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import ca.mcgill.ecse321.PLMS.service.EmployeeService;
import ca.mcgill.ecse321.PLMS.service.MonthlyCustomerService;
import ca.mcgill.ecse321.PLMS.service.ServiceAppointmentService;
import ca.mcgill.ecse321.PLMS.service.ServiceService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Controller class related to endpoints for CRUD operations on the service appointments model class in the context of the PLMS system
 */
@RestController
@CrossOrigin(origins="*")
public class ServiceAppointmentController {
    
    @Autowired
    private ServiceAppointmentService serviceAppointmentService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MonthlyCustomerService monthlyCustomerService;


     /**
      * Gets all upcoming and previously booked service appointments
      * 
      * @return All service appointments
      */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All service appointments", content = {@Content( mediaType = "application/json",
          array = @ArraySchema(schema = @Schema(implementation = ServiceAppointmentResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no service appointments in the system.", content = {@Content(mediaType = "String")})
      })
      @GetMapping("/serviceAppointment")
      public Iterable<ServiceAppointmentResponseDto> getAllServiceAppointments(){
        return StreamSupport.stream(serviceAppointmentService.getAllServiceAppointments().spliterator(), false)
        .map(s -> new ServiceAppointmentResponseDto(s))
        .collect(Collectors.toList());
      }

      /**
       * Gets a service appointment by id
       * 
       * @param id The id of the service appointement to look up.
       * @return The service appointement with the given id.
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Service appointment with this ID does not exist.", content = {@Content(mediaType = "String")})
      })
      @GetMapping("/serviceAppointment/{id}")
      public ResponseEntity<ServiceAppointmentResponseDto> getServiceAppointmentById(@PathVariable int id) {
        ServiceAppointment serviceAppointment = serviceAppointmentService.findServiceAppointmentById(id);
        ServiceAppointmentResponseDto responseBody = new ServiceAppointmentResponseDto(serviceAppointment);
        return new ResponseEntity<ServiceAppointmentResponseDto>(responseBody, HttpStatus.OK);
      }

      /**
       * Gets all the service appointments scheduled on a date
       * 
       * @param date The date at which you want the find all the ServiceAppointments form: YYYY-MM-DD
       * @return The service appointments at the specified date.
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The service appointments at the specified date.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ServiceAppointmentResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no appointments on this date.", content = {@Content(mediaType = "String")})
      })
      @GetMapping("/serviceAppointment/date/{date}")
      public Iterable<ServiceAppointmentResponseDto> getAllServiceAppointmentsByDate(@PathVariable LocalDate date) {
        return StreamSupport.stream(serviceAppointmentService.getAllServiceAppointmentsByDate(date).spliterator(), false)
        .map(s -> new ServiceAppointmentResponseDto(s))
        .collect(Collectors.toList());
      }

      /**
       * Get all the service appointments assigned to an employee based on their account's email
       *  
       * @param email The email of the employee you want to check for
       * @return The service appointments related to the employee
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The service appointments related to the employee.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ServiceAppointmentResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no service appointments for this employee.", content = {@Content(mediaType = "String")})
      })
      @GetMapping("/serviceAppointment/employee/{email}")
      public Iterable<ServiceAppointmentResponseDto> getServiceAppointmentByEmployee(@PathVariable String email) {
        return StreamSupport.stream(serviceAppointmentService.getAllServiceAppointmentsByEmployee(email).spliterator(), false)
        .map(s -> new ServiceAppointmentResponseDto(s))
        .collect(Collectors.toList());
      }

      /**
       * Gets all the service appointments ever booked by a monthly customer
       * 
       * @param email The email of the customer you want to check for
       * @return The service appointments related to the customer
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The service appointments related to the customer.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = ServiceAppointmentResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no service appointments for this customer.", content = {@Content(mediaType = "String")})
      })
      @GetMapping("/serviceAppointment/customer/{email}")
      public Iterable<ServiceAppointmentResponseDto> getServiceAppointmentByCustomer(@PathVariable String email) {
        return StreamSupport.stream(serviceAppointmentService.getAllServiceAppointmentsByMonthlyCustomer(email).spliterator(), false)
        .map(s -> new ServiceAppointmentResponseDto(s))
        .collect(Collectors.toList());
      }

      /**
       * Creates a new service appointment with the desired service, date, start time and end time
       * 
       * @param serviceAppointmentRequestDto Contains <b>service name</b> (String), <b>date</b> (Date format: YYYY-MM-DD), <b>start time</b> (Time format: HH:mm:ss) and an optional <b>user email</b> (String)
       * @return The service appointment object created
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Service with this name is not found.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "400",
        description = "Possible Errors: Cannot book appointment since the parking lot has not been created yet. Please try again at a later date. |OR| Cannot have an appointment beginning before the lot opens. |OR| Cannot have an appointment beginning after the lot closes. |OR| Cannot have an appointment ending after the lot closes.",
        content = {@Content(mediaType = "String")})})
      @PostMapping("/serviceAppointment")
      public ResponseEntity<ServiceAppointmentResponseDto> createServiceAppointment(@Valid @RequestBody ServiceAppointmentRequestDto serviceAppointmentRequestDto){
        //Converting 
        Service service = serviceService.getServiceByServiceName(serviceAppointmentRequestDto.getServiceName());
        
        MonthlyCustomer monthlyCustomer = null;
        if(serviceAppointmentRequestDto.getUserEmail() != null) monthlyCustomer = monthlyCustomerService.getMonthlyCustomerByEmail(serviceAppointmentRequestDto.getUserEmail());
        
        ServiceAppointment serviceAppointment = serviceAppointmentRequestDto.toModel(service, monthlyCustomer);
        serviceAppointment = serviceAppointmentService.createServiceAppointment(serviceAppointment);
        ServiceAppointmentResponseDto responseBody = new ServiceAppointmentResponseDto(serviceAppointment);
        
        return new ResponseEntity<ServiceAppointmentResponseDto>(responseBody, HttpStatus.CREATED);
      }
      
      /**
       * Creates a new service appointment with the desired service, date, start time and end time
       * 
       * @param id Id of the the service appointment
       * @param serviceAppointmentRequestDto Contains service name (String), date (Date format: YYYY-MM-DD), start time (Time format: HH:mm:ss) and an optional user email (String)
       * @return The service appointment object created
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Service appointment is not found.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "400",
        description = "Possible Errors:Cannot have an appointment beginning before the lot opens. |OR| Cannot have an appointment beginning after the lot closes. |OR| Cannot have an appointment ending after the lot closes.",
        content = {@Content(mediaType = "String")})})
      @PutMapping("/serviceAppointment/{id}")
      public ResponseEntity<ServiceAppointmentResponseDto> updateServiceAppointment(@PathVariable int id, @Valid @RequestBody ServiceAppointmentRequestDto serviceAppointmentRequestDto){
        //Converting 
        Service service = serviceService.getServiceByServiceName(serviceAppointmentRequestDto.getServiceName());
        
        MonthlyCustomer monthlyCustomer = null;
        
        ServiceAppointment serviceAppointment = serviceAppointmentRequestDto.toModel(service, monthlyCustomer);
        serviceAppointment = serviceAppointmentService.updateServiceAppointment(serviceAppointment, id);
        ServiceAppointmentResponseDto responseBody = new ServiceAppointmentResponseDto(serviceAppointment);
        return new ResponseEntity<ServiceAppointmentResponseDto>(responseBody, HttpStatus.OK);
      }

      /**
       * Update the employee associated with a service appointment
       * 
       * @param id Id of the service appointment - this is a pathVariable
       * @param employeeEmail The new employee email of the service appointment
       * @return The updated service appointment
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Possible Errors: Employee not found. |OR| Service appointment is not found.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "400", description = "Cannot change the employee because requested employee already has an appointment during the time frame of this appointment.", content = {@Content(mediaType = "String")})})
      @PutMapping("/serviceAppointment/employeeEmail/{id}")
      public ResponseEntity<ServiceAppointmentResponseDto> updateEmployeeEmailServiceAppointment(@PathVariable int id, @RequestParam String employeeEmail){
        Employee employee = null;
        if(!employeeEmail.isEmpty()) employee = employeeService.getEmployeeByEmail(employeeEmail);
        
        ServiceAppointment serviceAppointment = serviceAppointmentService.updateEmployeeEmailServiceAppointment(employee, id);
        ServiceAppointmentResponseDto responseBody = new ServiceAppointmentResponseDto(serviceAppointment);
        return new ResponseEntity<ServiceAppointmentResponseDto>(responseBody, HttpStatus.OK);
      }
      
      /**
       * Deletes a service appointment based on id
       * 
       * @param id Id of the service appointment that will be deleted
       */
      @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service appointment succefully deleted."),
        @ApiResponse(responseCode = "404", description = "Service appointment with this ID does not exist.", content = {@Content(mediaType = "String")})})
        @DeleteMapping("/serviceAppointment/{id}")
        public void deleteServiceAppointment(@PathVariable int id){
          serviceAppointmentService.deleteServiceAppointmentById(id);
        }
}
