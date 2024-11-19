package ca.mcgill.ecse321.PLMS.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import ca.mcgill.ecse321.PLMS.dto.ParkingLotRequestDto;
import ca.mcgill.ecse321.PLMS.dto.ParkingLotResponseDto;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.service.ParkingLotService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controller class related to endpoints for CRUD operations on the parking lot class in the context of the PLMS system
 */
@CrossOrigin(origins = "*")
@RestController
public class ParkingLotController {

    @Autowired
	private ParkingLotService parkingLotService;

	/**
	 * Gets the parking lot
	 * 
	 * @return The parking lot
	 */
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404", description = "Parking Lot not found.", content = {@Content(mediaType = "String")})
      })
    @GetMapping("/parkingLot")
	public ResponseEntity<ParkingLotResponseDto> getParkingLot() {
		ParkingLot p = parkingLotService.getParkingLot();
		ParkingLotResponseDto responseBody = new ParkingLotResponseDto(p);
		return new ResponseEntity<ParkingLotResponseDto>(responseBody, HttpStatus.OK);
	}

	/**
	 * Create a parking lot (Once the parking is created, this method will return 409)
	 * 
	 * @param parkingLotRequestDto
	 * @return The created parking lot
	 */
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Possible Errors: Opening and closing times cannot be the same. |OR| Opening time cannot be after closing time.", content = {@Content(mediaType = "String")}),
        @ApiResponse(responseCode = "409", description = "Parking Lot already exists", content = {@Content(mediaType = "String")})
      })
    @PostMapping("/parkingLot/creation")
	public ResponseEntity<ParkingLotResponseDto> createParkingLot(@Valid @RequestBody ParkingLotRequestDto parkingLotRequestDto) {
		ParkingLot p = parkingLotRequestDto.toModel();
		p = parkingLotService.createParkingLot(p);
		ParkingLotResponseDto responseBody = new ParkingLotResponseDto(p);
		return new ResponseEntity<ParkingLotResponseDto>(responseBody, HttpStatus.CREATED);
	}

	/**
	 * Updating the parking information
	 * 
	 * @param parkingLotRequestDto
	 * @return The updated parking lot
	 */
	@ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Possible Errors: Opening and closing times cannot be the same. |OR| Opening time cannot be after closing time.", content = {@Content(mediaType = "String")})
      })
    @PutMapping(value = {"/parkingLot/update"})
    public ResponseEntity<ParkingLotResponseDto> updateParkingLot(@Valid @RequestBody ParkingLotRequestDto parkingLotRequestDto)
    {
        ParkingLot p = parkingLotRequestDto.toModel();
        p = parkingLotService.updateParkingLot(p);
        return new ResponseEntity<ParkingLotResponseDto>(new ParkingLotResponseDto(p), HttpStatus.OK);

    }
}
