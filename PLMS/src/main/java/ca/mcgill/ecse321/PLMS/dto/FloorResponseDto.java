package ca.mcgill.ecse321.PLMS.dto;

import ca.mcgill.ecse321.PLMS.model.Floor;
import io.swagger.v3.oas.annotations.media.Schema;

public class FloorResponseDto {
  // attributes
  @Schema(example= "1", description = "Floor number of this floor")
  private int floorNumber;
  @Schema(example= "false", description = "Is this floor reserved for members only")
  private boolean isMemberOnly;
  @Schema(example= "50", description = "Number of small spots on this floor")
  private int smallSpotCapacity;
  @Schema(example= "20", description = "Number of large spots on this floor")
  private int largeSpotCapacity;

  /**
   * Default constructor
   */
  public FloorResponseDto(){}
  
  /**
   * Constructor for creating a floor transfer object by using the fields of 
   * a floor object.
   * @param floor - floor to turn into a transfer object
   */
  public FloorResponseDto(Floor floor){
    this.floorNumber = floor.getFloorNumber();
    this.isMemberOnly = floor.getIsMemberOnly();
    this.smallSpotCapacity = floor.getSmallSpotCapacity();
    this.largeSpotCapacity = floor.getLargeSpotCapacity();
  }

  // getters

  public int getFloorNumber(){
    return this.floorNumber;
  }

  public boolean getIsMemberOnly(){
    return this.isMemberOnly;
  }

  public int getSmallSpotCapacity() {
      return smallSpotCapacity;
  }

  public int getLargeSpotCapacity(){
    return this.largeSpotCapacity;
  }


  // need to add setters to responses!

  public void setFloorNumber(int floorNumber){
    this.floorNumber = floorNumber;
  }

  public void setIsMemberOnly(boolean isMemberOnly) {
      this.isMemberOnly = isMemberOnly;
  }

  public void setSmallSpotCapacity(int smallSpotCapacity){
    this.smallSpotCapacity = smallSpotCapacity;
  }

  public void setLargeSpotCapacity(int largeSpotCapacity){
    this.largeSpotCapacity = largeSpotCapacity;
  }



}

