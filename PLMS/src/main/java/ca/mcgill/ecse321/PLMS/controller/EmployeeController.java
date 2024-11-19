package ca.mcgill.ecse321.PLMS.controller;

import ca.mcgill.ecse321.PLMS.dto.EmployeeRequestDto;
import ca.mcgill.ecse321.PLMS.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller class related to endpoints for CRUD operations on the employee model class in the context of the PLMS system
 */
@CrossOrigin(origins="*")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * Returns a list of all Employees
     * 
     * @return all Employees
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "all Employees", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = EmployeeResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no employees in the system.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/employees")
    public Iterable<EmployeeResponseDto> getAllEmployees() {
        return StreamSupport.stream(employeeService.getAllEmployees().spliterator(), false).map(EmployeeResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Returns the Employee based on their Email
     * 
     * @param email - Pass in the email argument by using /employee={?email}
     * @return the Employee with Email, Password, Name
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Employee not found.", content = {@Content(mediaType = "String")})
    })
    @GetMapping(value = {"/employee", "/employee/"})
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmail(@RequestParam String email) {
        return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employeeService.getEmployeeByEmail(email)), HttpStatus.OK);
    }

    /**
     * Creates a new Employee
     * 
     * @param EmployeeRequest - Pass in a employee dto using a JSON request
     * @return the dto response of the new Employee
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Hourly wage must be positive.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "409", description = "Employee account with this email already exists.", content = {@Content(mediaType = "String")})
    })
    @PostMapping("/employee/create")
    public ResponseEntity<EmployeeResponseDto> createEmployee(@Valid @RequestBody EmployeeRequestDto EmployeeRequest)
    {
        Employee Employee = EmployeeRequest.toModel(); // 1. You pass in a request, validates the constraints, creates an Employee if they pass
        Employee =  employeeService.createEmployeeAccount(Employee); // 2. You use the service class to check if it exists and save it
        EmployeeResponseDto responseBody = new EmployeeResponseDto(Employee);
        return new ResponseEntity<EmployeeResponseDto>(responseBody, HttpStatus.CREATED); //3. You mask the model by returning a Response
    }

    /**
     * Updates an existing employee
     * 
     * @param employeeRequest - Pass in the monthly customer dto using a JSON request
     * @return the dto response of the updated Employee
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Hourly wage must be positive.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "404", description = "Employee not found.", content = {@Content(mediaType = "String")})
    })
    @PutMapping(value = {"/employee/update"})
    public ResponseEntity<EmployeeResponseDto> updateEmployee(@Valid @RequestBody EmployeeRequestDto employeeRequest)
    {
       Employee employee = employeeRequest.toModel();
       employee = employeeService.updateEmployee(employee);
       return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employee), HttpStatus.OK);

    }

    /**
     * Deletes an existing employee
     *
     * @param email - email of an existing email
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Employee succefully deleted."),
        @ApiResponse(responseCode = "404", description = "Employee not found.", content = {@Content(mediaType = "String")})
    })
    @DeleteMapping("/employee/delete/{email}")
    public void deleteEmployee(@PathVariable String email) {
        employeeService.deleteEmployeeAccount(email);
    }



}
