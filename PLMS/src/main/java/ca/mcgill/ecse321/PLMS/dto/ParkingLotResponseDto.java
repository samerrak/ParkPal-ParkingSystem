package ca.mcgill.ecse321.PLMS.dto;

import java.sql.Time;

import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import io.swagger.v3.oas.annotations.media.Schema;

public class ParkingLotResponseDto {
    @Schema(example= "5", description = "id of the parking lot")
    private int id;
    @Schema(example= "06:00:00", description = "The opening time of the parking lot", implementation = String.class, format = "HH:mm:ss")
    private Time openingTime;
    @Schema(example= "22:00:00", description = "The closing time of the parking lot", implementation = String.class, format = "HH:mm:ss")
    private Time closingTime;
    @Schema(example= "2.5", description = "The fee for a large spot")
    private Double largeSpotFee;
    @Schema(example= "1.25", description = "The fee for a small spot")
    private Double smallSpotFee;
    @Schema(example= "55.0", description = "The flat fee for the monthly a small spot")
    private Double smallSpotMonthlyFlatFee;
    @Schema(example= "45.25", description = "The flat fee for the monthly a large spot")
	private Double largeSpotMonthlyFlatFee;
	
	public ParkingLotResponseDto(ParkingLot p) {
		this.id = p.getId();
		this.openingTime = p.getOpeningTime();
		this.closingTime = p.getClosingTime();
        this.largeSpotFee = p.getLargeSpotFee();
        this.smallSpotFee = p.getSmallSpotFee();
        this.smallSpotMonthlyFlatFee = p.getSmallSpotMonthlyFlatFee();
		this.largeSpotMonthlyFlatFee = p.getLargeSpotMonthlyFlatFee();
	}

	public ParkingLotResponseDto() {}
	
	public int getId() {
		return id;
	}

    public Time getOpeningTime() {
		return openingTime;
	}

    public Time getClosingTime() {
		return closingTime;
	}

    public Double getLargeSpotFee() {
		return largeSpotFee;
	}

    public Double getSmallSpotFee() {
		return smallSpotFee;
	}

	public Double getSmallSpotMonthlyFlatFee() {
		return smallSpotMonthlyFlatFee;
	}

	public Double getLargeSpotMonthlyFlatFee() {
		return largeSpotMonthlyFlatFee;
	}
}
