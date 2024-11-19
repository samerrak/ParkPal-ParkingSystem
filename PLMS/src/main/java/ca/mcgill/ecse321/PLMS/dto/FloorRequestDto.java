package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Floor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FloorRequestDto {
  @NotNull(message = "Cannot have an empty floor number.")
  @Min(value = 0, message = "The floor number must be a non negative number.")
  @Schema(example= "1", description = "Floor number of this floor", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer floorNumber;

  @NotNull(message = "Cannot have an empty number of small spots on a floor.")
  @Min(value = 0, message = "Cannot be a negative number of small parking spots on a floor.")
  @Schema(example= "50", description = "Number of small spots on this floor", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer smallSpotCapacity;

  @NotNull(message = "Cannot have an empty number of large spots on a floor.")
  @Min(value = 0, message = "Cannot be a negative number of large parking spots on a floor.")
  @Schema(example= "20", description = "Number of large spots on this floor", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer largeSpotCapacity;

  @NotNull(message = "Member only must be true or false.")
  @Schema(example= "false", description = "Is this floor reserved for members only", requiredMode = Schema.RequiredMode.REQUIRED)
  private Boolean isMemberOnly;

  /**
   * Method to construct a floor object from a floor request dto object
   * @return floor object with all corresponding attributes
   */
  public Floor toModel(){
    Floor floor = new Floor();
    floor.setFloorNumber(this.floorNumber);
    floor.setIsMemberOnly(this.isMemberOnly);
    floor.setSmallSpotCapacity(this.smallSpotCapacity);
    floor.setLargeSpotCapacity(this.largeSpotCapacity);
    return floor;
  }

  public void setFloorNumber(Integer floorNumber){
    this.floorNumber = floorNumber;
  }

  public void setSmallSpotCapacity(Integer smallSpotCapacity){
    this.smallSpotCapacity = smallSpotCapacity;
  }

  public void setLargeSpotCapacity(Integer largeSpotCapacity){
    this.largeSpotCapacity = largeSpotCapacity;
  }

  public void setIsMemberOnly(Boolean isMemberOnly){
    this.isMemberOnly = isMemberOnly;
  }

  // NEED TO ADD GETTERS TO REQUEST DTOs!
  public Integer getFloorNumber(){
    return this.floorNumber;
  }

  public Integer getSmallSpotCapacity(){
    return this.smallSpotCapacity;
  }

  public Integer getLargeSpotCapacity(){
    return this.largeSpotCapacity;
  }

  public Boolean getIsMemberOnly(){
    return this.isMemberOnly;
  }

}