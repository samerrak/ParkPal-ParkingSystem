package ca.mcgill.ecse321.PLMS.controller;

import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerRequestDto;
import ca.mcgill.ecse321.PLMS.dto.MonthlyCustomerResponseDto;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.service.MonthlyCustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.CrossOrigin;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller class related to endpoints for CRUD operations on the monthly customer model class in the context of the PLMS system
 */
@CrossOrigin(origins="*")
@RestController
public class MonthlyCustomerController {
    @Autowired
    private MonthlyCustomerService monthlyCustomerService;

    /**
     * Returns a list of all MonthlyCustomers
     * 
     * @return all MonthlyCustomers
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "all MonthlyCustomers", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = MonthlyCustomerResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no monthly customers in the system.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/customers")
    public Iterable<MonthlyCustomerResponseDto> getAllMonthlyCustomers() {
        return StreamSupport.stream(monthlyCustomerService.getAllMonthlyCustomers().spliterator(), false).map(MonthlyCustomerResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Returns the MonthlyCustomer based on their Id
     * 
     * @param email - Pass in the email argument by using  /MonthlyCustomer=?{email}
     * @return the MonthlyCustomer with Email, Password, Name
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Monthly customer not found.", content = {@Content(mediaType = "String")})
    })
    @GetMapping(value = {"/customer", "/customer/"})
    public ResponseEntity<MonthlyCustomerResponseDto> getMonthlyCustomerByEmail(@RequestParam String email) {
        return new ResponseEntity<MonthlyCustomerResponseDto>(new MonthlyCustomerResponseDto(monthlyCustomerService.getMonthlyCustomerByEmail(email)), HttpStatus.OK);
    }

    /**
     * Creates a new MonthlyCustomer
     * 
     * @param MonthlyCustomerRequest - Pass in a monthly customer dto using a JSON request
     * @return the dto response of the new MonthlyCustomer
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "409", description = "Account with this email already exists.", content = {@Content(mediaType = "String")})
    })
    @PostMapping("/customer/create")
    public ResponseEntity<MonthlyCustomerResponseDto> createMonthlyCustomer(@Valid @RequestBody MonthlyCustomerRequestDto MonthlyCustomerRequest)
    {
        MonthlyCustomer MonthlyCustomer = MonthlyCustomerRequest.toModel(); // 1. You pass in a request, validates the constraints, creates an MonthlyCustomer if they pass
        MonthlyCustomer =  monthlyCustomerService.createMonthlyCustomerAccount(MonthlyCustomer); // 2. You use the service class to check if it exists and save it
        return new ResponseEntity<MonthlyCustomerResponseDto>(new MonthlyCustomerResponseDto(MonthlyCustomer), HttpStatus.CREATED); //3. You mask the model by returning a Response
    }

    /**
     * Updates an existing MonthlyCustomer
     * 
     * @param monthlyCustomerRequest - Pass in the monthly customer dto using a JSON request
     * @return the dto response of the updated MonthlyCustomer
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Monthly customer not found.", content = {@Content(mediaType = "String")})
    })
    @PutMapping("/customer/update")
    public ResponseEntity<MonthlyCustomerResponseDto> updateMonthlyCustomer(@Valid @RequestBody MonthlyCustomerRequestDto monthlyCustomerRequest)
    {
        MonthlyCustomer monthlyCustomer = monthlyCustomerRequest.toModel();
        monthlyCustomer = monthlyCustomerService.updateMonthlyCustomer(monthlyCustomer);
        return new ResponseEntity<MonthlyCustomerResponseDto>(new MonthlyCustomerResponseDto(monthlyCustomer), HttpStatus.OK);

    }



}
