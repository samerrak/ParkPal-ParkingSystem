package ca.mcgill.ecse321.PLMS.dto;


import ca.mcgill.ecse321.PLMS.model.GuestPass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GuestPassRequestDto {

    @NotNull(message = "Cannot have an empty spot number.")
    @Schema(example= "A24", description = "The spot number", requiredMode = Schema.RequiredMode.REQUIRED)
    private String spotNumber;

    @NotNull(message = "Cannot have an empty confirmation code.")
    @Schema(example= "JK95HO95T3", description = "Code that confirms the payment (will be null before payment)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmationCode;

    @NotNull(message = "Cannot have an empty license plate.")
    @Schema(example= "T3ST41", description = "License plate of the car linked to this pass", requiredMode = Schema.RequiredMode.REQUIRED)
    private String licensePlate;

    @NotNull(message = "Cannot have an empty floor number.")
    @Schema(example= "1", description = "Floor number where the spot is located at", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer floorNumber;

    @NotNull(message = "Cannot have an empty number of 15 minute increments.")
    @Min(value = 1, message = "Must enter a positive number of 15 minute increments.")
    @Schema(example= "3", description = "The number of increments of 15min associated with the pass. If you wish to stay 45min then this number would be 3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numberOfFifteenMinuteIncrements;


    @NotNull(message = "Must specify whether the pass is small or large.")
    @Schema(example= "false", description = "Is the spot a spot that can hold large vehicles", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isLarge;

    public GuestPass toModel(){
        GuestPass guestPass = new GuestPass();
        guestPass.setSpotNumber(this.spotNumber);
        guestPass.setConfirmationCode(this.confirmationCode);
        guestPass.setLicensePlate(this.licensePlate);
        guestPass.setIsLarge(this.isLarge);
        return guestPass;
    }

    public void setSpotNumber(String spotNumber){
        this.spotNumber = spotNumber;
    }

    public void setConfirmationCode(String confirmationCode){
        this.confirmationCode = confirmationCode;
    }

    public void setLicensePlate(String licensePlate){
        this.licensePlate = licensePlate;
    }

    public void setFloorNumber(Integer floorNumber){
        this.floorNumber = floorNumber;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public Integer getNumberOfFifteenMinuteIncrements() {
        return numberOfFifteenMinuteIncrements;
    }

    public void setIsLarge(Boolean isLarge) {
        this.isLarge = isLarge;
    }

    public void setNumberOfFifteenMinuteIncrements(Integer numberOfFifteenMinuteIncrements) {
        this.numberOfFifteenMinuteIncrements = numberOfFifteenMinuteIncrements;
    }

    public String getSpotNumber() {
        return spotNumber;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public Boolean getIsLarge() {
        return isLarge;
    }
}