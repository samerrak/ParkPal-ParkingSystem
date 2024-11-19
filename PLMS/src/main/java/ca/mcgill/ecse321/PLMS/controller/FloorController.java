package ca.mcgill.ecse321.PLMS.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import ca.mcgill.ecse321.PLMS.dto.FloorRequestDto;
import ca.mcgill.ecse321.PLMS.dto.FloorResponseDto;
import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.service.FloorService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Controller class related to endpoints for CRUD operations on the floor model class in the context of the PLMS system
 */
@CrossOrigin(origins = "*")
@RestController
public class FloorController {
  @Autowired
  private FloorService floorService;

  /**
   * Gets all floors.
   * 
   * @return All floors.
   */
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "All floors", content = {@Content( mediaType = "application/json",
    array = @ArraySchema(schema = @Schema(implementation = FloorResponseDto.class)))}),
    @ApiResponse(responseCode = "404", description = "There are no floors in the system.", content = {@Content(mediaType = "String")})
  })
  @GetMapping("/floor")
  public Iterable<FloorResponseDto> getAllFloors(){
    return StreamSupport.stream(floorService.getAllFloors().spliterator(), false).map(f -> new FloorResponseDto(f)).collect(Collectors.toList());
  }

  /**
   * Gets a floor by the floor number
   * 
   * @param floorNumber floor number of the floor to get
   * @return floor with floorNumber
   */
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "404", description = "Floor with this floor number does not exist.", content = {@Content(mediaType = "String")})
  })
  @GetMapping("/floor/{floorNumber}")
  public ResponseEntity<FloorResponseDto> getFloorByFloorNumber(@PathVariable int floorNumber){
    Floor floor = floorService.getFloorByFloorNumber(floorNumber);
    FloorResponseDto responseBody = new FloorResponseDto(floor);
    return new ResponseEntity<FloorResponseDto>(responseBody, HttpStatus.OK);
  }

  /**
   * Creates a floor.
   * 
   * @param floorDto Contains floor number (int), small spot capacity (int), large spot capacity (int) and an isMemberOnly parameter (boolean)
   * @return floorDto of the created floor
   */
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", description = "Cannot create floor since the parking lot has not been created.", content = {@Content(mediaType = "String")}),    
    @ApiResponse(responseCode = "404", description = "Floor with this floor number does not exist.", content = {@Content(mediaType = "String")})
  })
  @PostMapping("/floor")
  public ResponseEntity<FloorResponseDto> createFloor(@Valid @RequestBody FloorRequestDto floorDto){
    Floor floor = floorDto.toModel();
    floor = floorService.createFloor(floor);
    FloorResponseDto responseBody = new FloorResponseDto(floor);
    return new ResponseEntity<FloorResponseDto>(responseBody, HttpStatus.CREATED);
  }

  /**
   * Allows updates for all floor variables
   * 
   * @param floorDto Contains floor number (int), small spot capacity (int), large spot capacity (int) and an isMemberOnly parameter (boolean)
   * @return floor with updated values
   */
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "404", description = "Floor with this floor number does not exist.", content = {@Content(mediaType = "String")})
  })
  @PutMapping("/floor")
  public ResponseEntity<FloorResponseDto> updateFloorInfo(@RequestBody @Valid FloorRequestDto floorDto){
    Floor floor = floorDto.toModel();
    floor = floorService.updateFloor(floor);
    FloorResponseDto responseBody = new FloorResponseDto(floor);
    return new ResponseEntity<FloorResponseDto>(responseBody, HttpStatus.CREATED);
  }

  /**
   * Deletes a floor
   * 
   * @param floorNumber Floor number of the floor that will be deleted
   */
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Floor succefully deleted."),
    @ApiResponse(responseCode = "404", description = "Floor with this floor number does not exist.", content = {@Content(mediaType = "String")})
  })
  @DeleteMapping("/floor/{floorNumber}")
  public void deleteFloor(@PathVariable int floorNumber){
    floorService.deleteFloorByFloorNumber(floorNumber);
  }

}
