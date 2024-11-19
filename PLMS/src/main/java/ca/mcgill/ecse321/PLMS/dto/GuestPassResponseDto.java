package ca.mcgill.ecse321.PLMS.dto;

import java.sql.Time;
import java.time.LocalDate;

import ca.mcgill.ecse321.PLMS.model.GuestPass;
import io.swagger.v3.oas.annotations.media.Schema;


public class GuestPassResponseDto {


    @Schema(example = "5", description = "The id of the guest pass")
    private int id;
    @Schema(example= "1.25", description = "The fee assiated to the pass")
    private double fee;
    @Schema(example= "A24", description = "The spot number")
    private String spotNumber;
    @Schema(example= "JK95HO95T3", description = "Code that confirms the payment (will be null before payment)")
    private String confirmationCode;
    @Schema(example= "T3ST41", description = "License plate of the car linked to this pass")
    private String licensePlate;
    @Schema(example= "false", description = "Is the spot a spot that can hold large vehicles")
    private boolean isLarge;
    @Schema(example= "2024-05-05", description = "Date of the pass")
    private LocalDate date;
    @Schema(example= "15:00:00", description = "The starting time of the guest pass", implementation = String.class, format = "HH:mm:ss")
    private Time startTime;    
    @Schema(example= "17:00:00", description = "The time at which the guest pass ends", implementation = String.class, format = "HH:mm:ss")
    private Time endTime;
    @Schema(example= "1", description = "Floor number where the spot is located at")
    private int floorNumber;

    /**
     * Constructor for creating a guestPass response transfer object by using the fields of 
     * a guestPass object.
     * @param guestPass - guestPass to turn into a transfer object
     */

    public GuestPassResponseDto(GuestPass guestPass){
        this.id = guestPass.getId();
        this.fee = guestPass.getFee();
        this.spotNumber = guestPass.getSpotNumber();
        this.confirmationCode = guestPass.getConfirmationCode();
        this.licensePlate = guestPass.getLicensePlate();
        this.floorNumber = guestPass.getFloor().getFloorNumber();
        this.isLarge = guestPass.getIsLarge();
        this.date = guestPass.getDate();
        this.startTime = guestPass.getStartTime();
        this.endTime = guestPass.getEndTime();
    }

    public GuestPassResponseDto() {
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

    public boolean getIsLarge() {
        return isLarge;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getFloorNumber() {
        return floorNumber;
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

    public void setIsLarge(boolean large) {
        isLarge = large;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
}