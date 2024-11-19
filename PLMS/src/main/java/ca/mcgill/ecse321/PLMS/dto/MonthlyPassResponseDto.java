package ca.mcgill.ecse321.PLMS.dto;

import java.time.LocalDate;

import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import io.swagger.v3.oas.annotations.media.Schema;


public class MonthlyPassResponseDto {

    @Schema(example = "5", description = "The id of the monthly pass")
    private Integer id;
    @Schema(example= "1.25", description = "The fee assiated to the pass")
    private Double fee;
    @Schema(example= "A24", description = "The spot number")
    private String spotNumber;
    @Schema(example= "JK95HO95T3", description = "Code that confirms the payment (will be null before payment)")
    private String confirmationCode;
    @Schema(example= "T3ST41", description = "License plate of the car linked to this pass")
    private String licensePlate;
    @Schema(example= "false", description = "Is the spot a spot that can hold large vehicles")
    private Boolean isLarge;
    @Schema(example= "2024-05-05", description = "Start date of the pass")
    private LocalDate startDate;
    @Schema(example= "2024-06-05", description = "End date of the pass")
    private LocalDate endDate;
    @Schema(example= "1", description = "Floor number where the spot is located at")
    private Integer floorNumber;
    @Schema(example= "user@email.com", description = "Email linked to the account of the customer")
    private String customerEmail;

    /**
     * Constructor for creating a monthlypass response transfer object by using the fields of 
     * a monthlypass object.
     * @param monthlypass - monthlypass to turn into a transfer object
     */

    public MonthlyPassResponseDto() {

    }
    public MonthlyPassResponseDto(MonthlyPass monthlypass){
        this.id = monthlypass.getId();
        this.fee = monthlypass.getFee();
        this.spotNumber = monthlypass.getSpotNumber();
        this.confirmationCode = monthlypass.getConfirmationCode();
        this.licensePlate = monthlypass.getLicensePlate();
        this.floorNumber = monthlypass.getFloor().getFloorNumber();
        if (monthlypass.getCustomer() != null) this.customerEmail = monthlypass.getCustomer().getEmail();
        this.isLarge = monthlypass.getIsLarge();
        this.startDate = monthlypass.getStartDate();
        this.endDate = monthlypass.getEndDate();
    }
    
    public int getId() {
        return id;
    }

    public double getFee() {
        return fee;
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

    public boolean getLarge() {
        return isLarge;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFee(double fee) {
        this.fee = fee;
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

    public void setLarge(boolean large) {
        isLarge = large;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
