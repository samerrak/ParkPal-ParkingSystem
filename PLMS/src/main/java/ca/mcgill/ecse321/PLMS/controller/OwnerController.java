package ca.mcgill.ecse321.PLMS.controller;

import ca.mcgill.ecse321.PLMS.dto.OwnerRequestDto;
import ca.mcgill.ecse321.PLMS.dto.OwnerResponseDto;
import ca.mcgill.ecse321.PLMS.model.Owner;
import ca.mcgill.ecse321.PLMS.service.OwnerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Controller class related to endpoints for CRUD operations on the owner model class in the context of the PLMS system
 */
@CrossOrigin(origins="*")
@RestController
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    /**
     * Returns a list of all owners
     * 
     * @return all owners
     */
    @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "All services.", content = {@Content( mediaType = "application/json",
      array = @ArraySchema(schema = @Schema(implementation = OwnerResponseDto.class)))}),
      @ApiResponse(responseCode = "404", description = "There are no owners in the system.", content = {@Content(mediaType = "String")})
      })
    @GetMapping("/owners")
    public Iterable<OwnerResponseDto> getAllOwners() {
        return StreamSupport.stream(ownerService.getAllOwners().spliterator(), false).map(OwnerResponseDto::new).collect(Collectors.toList());
    }

    /**
     * Returns the owner based on their Email
     * 
     * @param email Pass in the email argument by using /owner=?{email}
     * @return the owner with Email, Password, Name
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Owner not found.", content = {@Content(mediaType = "String")})
      })
    @GetMapping(value = {"/owner", "/owner/"})
    public ResponseEntity<OwnerResponseDto> getOwnerByEmail(@RequestParam String email) {
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(ownerService.getOwnerByEmail(email)), HttpStatus.OK);
    }

    /**
     * Creates a new owner
     * 
     * @param ownerRequest Pass in the new owner request using JSON
     * @return the dto response of the new owner
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "409", description = "Owner account with this email already exists.", content = {@Content(mediaType = "String")})
      })
    @PostMapping("/owner/create")
    public ResponseEntity<OwnerResponseDto> createOwner(@Valid @RequestBody OwnerRequestDto ownerRequest)
    {
        Owner owner = ownerRequest.toModel(); // 1. You pass in a request, validates the constraints, creates an owner if they pass
        owner =  ownerService.createOwnerAccount(owner); // 2. You use the service class to check if it exists and save it
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(owner), HttpStatus.CREATED); //3. You mask the model by returning a Response
    }

    /**
     * Updates an existing owner
     * 
     * @param ownerRequest Pass in the updated owner request using JSON
     * @return the dto response of the updated owner
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Owner not found.", content = {@Content(mediaType = "String")})
      })
    @PutMapping("/owner/update")
    public ResponseEntity<OwnerResponseDto> updateOwner(@Valid @RequestBody OwnerRequestDto ownerRequest) {

        Owner owner = ownerRequest.toModel();
        owner = ownerService.updateOwnerAccount(owner);
        return new ResponseEntity<OwnerResponseDto>(new OwnerResponseDto(owner), HttpStatus.OK);
    }

}
