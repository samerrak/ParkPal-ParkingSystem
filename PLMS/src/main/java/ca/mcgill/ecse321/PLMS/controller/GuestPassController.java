package ca.mcgill.ecse321.PLMS.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.PLMS.dto.GuestPassRequestDto;
import ca.mcgill.ecse321.PLMS.dto.GuestPassResponseDto;
import ca.mcgill.ecse321.PLMS.model.GuestPass;
import ca.mcgill.ecse321.PLMS.service.GuestPassService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controller class related to endpoints for CRUD operations on the guest pass model class in the context of the PLMS system
 */
@CrossOrigin(origins = "*")
@RestController
public class GuestPassController {

    @Autowired
    private GuestPassService guestPassService;

    /**
     * Gets all guest passes.
     *
     * @return All guest passes.
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All guest passes.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = GuestPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no guest passes in the system.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/guestPass/")
    public Iterable<GuestPassResponseDto> getAllGuestPasses(){
        return StreamSupport.stream(guestPassService.getAllGuestPasses().spliterator(), false).map(f -> new
        GuestPassResponseDto(f)).collect(Collectors.toList());
    }

    /**
     * Gets a guestpass by the guestpass id
     *
     * @param id id of the guest pass to get
     * @return GuestPassResponseDto of guestpass with id id
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Guest pass with this id does not exist.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/guestPass/{id}")
    public ResponseEntity<GuestPassResponseDto> getGuestPassById(@PathVariable int id){
    GuestPass guestPass = guestPassService.getGuestPassById(id);
    GuestPassResponseDto responseBody = new GuestPassResponseDto(guestPass);
    return new ResponseEntity<GuestPassResponseDto>(responseBody, HttpStatus.OK);
    }

    /**
     * Gets all guest passes at the floor floorNumber (inactive passes filtered out in service layer)
     *
     * @param floorNumber the floor number associated with the passes you are looking at
     * @return GuestPassResponseDto of guest passes with floor floorNumber
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "GuestPassResponseDto of guest passes with floor floorNumber.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = GuestPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "Possible Errors: The floor with this floor number does not exist. |OR| There are no guest passes on this floor.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/guestPass/floor/{floorNumber}")
    public Iterable<GuestPassResponseDto> getGuestPassesByFloor(@PathVariable int floorNumber){
        return StreamSupport.stream(guestPassService.getGuestPassesByFloor(floorNumber).spliterator(), false).map(f -> new
        GuestPassResponseDto((GuestPass) f)).collect(Collectors.toList());
    }

    /**
     * Gets all guest passes active on a date
     *
     * @param date date (LocalDate, Format: "YYYY-MM-DD")
     * @return GuestPassResponseDto of guest passes with date date
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "GuestPassResponseDto of guest passes with date date.", content = {@Content( mediaType = "application/json",
        array = @ArraySchema(schema = @Schema(implementation = GuestPassResponseDto.class)))}),
        @ApiResponse(responseCode = "404", description = "There are no guest passes for this date.", content = {@Content(mediaType = "String")})
    })
    @GetMapping("/guestPass/date/{date}")
    public Iterable<GuestPassResponseDto> getGuestPassesByDate(@PathVariable LocalDate date){
        return StreamSupport.stream(guestPassService.getGuestPassesByDate(date).spliterator(), false).map(f -> new
        GuestPassResponseDto((GuestPass) f)).collect(Collectors.toList());
    }

    /**
     * Creates a guest pass.
     *
     * @param guestPassRequestDto Contains spot number (String), confirmation code (String), license plate (String), floor number (int), number of fifteen minute increments (int) and an isLarge parameter (boolean)
     * @return GuestPassResponseDto of the created guest pass
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Possible Errors: This floor is reserved for monthly members. |OR| Cannot reserve spot for more than 12 hours. |OR| This spot is currently occupied. |OR| All spots of this size on this floor are occupied.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "404", description = "The floor with this floor number does not exist.", content = {@Content(mediaType = "String")})
    })
    @PostMapping("/guestPass")
    public ResponseEntity<GuestPassResponseDto> createGuestPass(@Valid @RequestBody GuestPassRequestDto guestPassRequestDto){
        GuestPass guestPass = guestPassRequestDto.toModel();
        int floorNumber = guestPassRequestDto.getFloorNumber();
        int nrIncrements = guestPassRequestDto.getNumberOfFifteenMinuteIncrements();
        // Find local time here to make the tests time-independent
        LocalDateTime currentTime = LocalDateTime.now();
        guestPass = guestPassService.createGuestPass(guestPass, floorNumber, nrIncrements, currentTime);
        GuestPassResponseDto responseBody = new GuestPassResponseDto(guestPass);
        return new ResponseEntity<GuestPassResponseDto>(responseBody, HttpStatus.CREATED);

    }

    /**
     * Deletes a guest pass
     * 
     * @param id id of the guest pass to delete
     */
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Guest pass succefully deleted."),
        @ApiResponse(responseCode = "404", description = "Guest pass with this id does not exist.", content = {@Content(mediaType = "String")})
    })
    @DeleteMapping("/guestPass/{id}")
    public void deleteGuestPass(@PathVariable int id){
        guestPassService.deleteGuestPassById(id);
    }

  
}
