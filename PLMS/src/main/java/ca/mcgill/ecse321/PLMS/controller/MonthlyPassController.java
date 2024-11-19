
package ca.mcgill.ecse321.PLMS.controller;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import ca.mcgill.ecse321.PLMS.service.MonthlyPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.PLMS.dto.MonthlyPassRequestDto;
import ca.mcgill.ecse321.PLMS.dto.MonthlyPassResponseDto;

import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import jakarta.validation.Valid;

/**
 * Controller class related to endpoints for CRUD operations on the monthly pass class in the context of the PLMS system
 */
 @CrossOrigin(origins = "*")
 @RestController
 public class MonthlyPassController {

     @Autowired
     private MonthlyPassService monthlyPassService;

    /**
     * Gets all monthly passes.
     *
     * @return MonthlyPassResponseDto of all monthly passes.
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MonthlyPassResponseDto of all monthly passes.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = MonthlyPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no monthly passes in the system.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/pass")
    public Iterable<MonthlyPassResponseDto> getAllMonthlyPasses(){
        return StreamSupport.stream(monthlyPassService.getAllMonthlyPasses().spliterator(), false).map(f -> new
        MonthlyPassResponseDto(f)).collect(Collectors.toList());
    }

    /**
     * Gets a monthlypass by the monthlypass id
     * 
     * @param id id of the pass
     * @return monthlypass with id id
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Monthly pass with this id does not exist.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/monthlypass/{id}")
    public ResponseEntity<MonthlyPassResponseDto> getPassById(@PathVariable int id){
        MonthlyPass monthlyPass = monthlyPassService.getMonthlyPassById(id);
        MonthlyPassResponseDto responseBody = new MonthlyPassResponseDto(monthlyPass);
        return new ResponseEntity<MonthlyPassResponseDto>(responseBody, HttpStatus.OK);
    }

    /**
     * Gets all monthly passes at the floor floorNumber (inactive passes filtered out in service layer)
     *
     * @param floorNumber floor number of the floor linked to the passes
     * @return MonthlyPassResponseDto of monthly passes with floor floorNumber
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MonthlyPassResponseDto of monthly passes with floor floorNumber", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = MonthlyPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "Possible Errors: The floor with this floor number does not exist. |OR| There are no monthly passes on this floor.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/monthlypass/floor/{floorNumber}")
    public Iterable<MonthlyPassResponseDto> getMonthlyPassesByFloor(@PathVariable int floorNumber){
        return StreamSupport.stream(monthlyPassService.getMonthlyPassesByFloor(floorNumber).spliterator(), false).map(f -> new
        MonthlyPassResponseDto(f)).collect(Collectors.toList());
    }

    /**
     * Gets all monthly passes by monthly customer
     *
     * @param email email of the account linked to the passes
     * @return MonthlyPassResponseDto of monthly passes of monthly customer with email email
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "MonthlyPassResponseDto of monthly passes of monthly customer with email email", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = MonthlyPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "Possible Errors: The account with this email does not exist. |OR| There are no monthly passes linked to this email.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/monthlypass/customer/{email}")
    public Iterable<MonthlyPassResponseDto> getMonthlyPassesByMonthlyCustomer(@PathVariable String email){
        return StreamSupport.stream(monthlyPassService.getMonthlyPassesByMonthlyCustomer(email).spliterator(), false).map(f -> new
        MonthlyPassResponseDto(f)).collect(Collectors.toList());
    }

    /**
     * Creates a monthly pass.
     *
     * @param monthlyPassRequestDto Contains numberOfMonths, spotNumber, confirmationCode, licensePlate, floorNumber, isLarge, startDate, (optinal) customerEmail
     * @return MonthlyPassResponseDto of the created monthly pass
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Possible Errors: This floor is reserved for guest passes only. |OR| This spot is currently occupied. |OR| All spots of this size on this floor are occupied.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "404", description = "Possible Errors: The floor with this floor number does not exist. |OR| There is no customer account associated with this email.", content = {@Content(mediaType = "String")})

    })
    @PostMapping("/monthlypass")
    public ResponseEntity<MonthlyPassResponseDto> createMonthlyPass(@Valid @RequestBody MonthlyPassRequestDto monthlyPassRequestDto){
        int floorNumber = monthlyPassRequestDto.getFloorNumber();
        int nrMonths = monthlyPassRequestDto.getNumberOfMonths();
        String email = monthlyPassRequestDto.getCustomerEmail();
        MonthlyPass monthlyPass = monthlyPassRequestDto.toModel();
        monthlyPass = monthlyPassService.createMonthlyPass(monthlyPass, floorNumber, nrMonths, email);
        MonthlyPassResponseDto responseBody = new MonthlyPassResponseDto(monthlyPass);
        return new ResponseEntity<MonthlyPassResponseDto>(responseBody, HttpStatus.CREATED);

     }


 }
