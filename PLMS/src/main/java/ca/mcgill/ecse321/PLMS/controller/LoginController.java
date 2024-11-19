package ca.mcgill.ecse321.PLMS.controller;


import ca.mcgill.ecse321.PLMS.dto.EmployeeResponseDto;
import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerResponseDto;
import ca.mcgill.ecse321.PLMS.dto.OwnerResponseDto;
import ca.mcgill.ecse321.PLMS.service.EmployeeService;
import ca.mcgill.ecse321.PLMS.service.MonthlyCustomerService;
import ca.mcgill.ecse321.PLMS.service.OwnerService;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controller class related to endpoints for logging in (or verification of the account credentials), in the context of the PLMS system
 */
@CrossOrigin(origins = "*")
@RestController
public class LoginController {

    @Autowired
    private OwnerService ownerService;
    @Autowired
    private MonthlyCustomerService monthlyCustomerService;
    @Autowired
    private EmployeeService employeeService;

    /**
     * Controller Method that allows the verfication of the email and password of an owner, customer or an employee.
     * 
     * @param user Either "Owner", "Employee" or "Customer" depending on what account you want to log into
     * @param email Email of the account
     * @param password Password of the account
     * @return
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Owner not found. |OR| Monthly customer not found. |OR| Employee not found. |OR| Please enter the correct password", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/login/{user}")
    public ResponseEntity<?> getUser(@PathVariable String user, @RequestParam String email, @RequestParam String password){
        if (user.equals("Owner"))
            return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(ownerService.getOwnerByEmailAndPassword(email, password)), HttpStatus.OK);
        if (user.equals("Employee"))
            return new ResponseEntity<EmployeeResponseDto>(new EmployeeResponseDto(employeeService.getEmployeeByEmailAndPassword(email, password)), HttpStatus.OK);
        else
            return new ResponseEntity<MonthlyCustomerResponseDto>(new MonthlyCustomerResponseDto(monthlyCustomerService.getMonthlyCustomerByEmailAndPassword(email, password)), HttpStatus.OK);

    }
}
