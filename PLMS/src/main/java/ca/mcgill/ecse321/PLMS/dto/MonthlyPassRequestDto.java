package ca.mcgill.ecse321.PLMS.dto;


import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


public class MonthlyPassRequestDto {

    //=-=-=-=-=- jakarta validation of variables -=-=-=-=-=//
    @NotNull(message = "Cannot have an empty number of months.")
    @Min(value = 1, message = "Must enter a positive number of months.")
    @Schema(example= "1", description = "The number of months that the pass has been reserved for", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer numberOfMonths;

    @NotNull(message = "Cannot have an empty spot number.")
    @Schema(example= "A24", description = "The spot number")
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

    @NotNull(message = "Must specify whether the pass is for a small or large car")
    @Schema(example= "false", description = "Is the spot a spot that can hold large vehicles", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isLarge;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be equal or greater than current date")
  @Schema(example= "2024-05-05", description = "Start date of the pass", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;
    


    // this is allowed to be null, as you don't need an account to register
    @Schema(example= "user@email.com", description = "Email linked to the account of the customer (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String customerEmail;




    /**
     * Constructor for creating a monthly pass request transfer object by using the fields of 
     * a monthlypass object.
     */


    public MonthlyPass toModel(){
        MonthlyPass monthlypass = new MonthlyPass();
        monthlypass.setSpotNumber(this.spotNumber);
        monthlypass.setConfirmationCode(this.confirmationCode);
        monthlypass.setLicensePlate(this.licensePlate);
        monthlypass.setIsLarge(this.isLarge);
        monthlypass.setStartDate(this.startDate);
        return monthlypass;
    }


    public void setNumberOfMonths(Integer numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public void setSpotNumber(String spotNumber) {
        this.spotNumber = spotNumber;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setLarge(Boolean large) {
        isLarge = large;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getNumberOfMonths() {
        return numberOfMonths;
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

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public Boolean getIsLarge() {
        return isLarge;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }
}
