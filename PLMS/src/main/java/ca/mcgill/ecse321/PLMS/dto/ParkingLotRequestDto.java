package ca.mcgill.ecse321.PLMS.dto;

import java.sql.Time;

import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ParkingLotRequestDto {

  @NotNull(message = "Opening time must not be null.")
  @Schema(example= "06:00:00", description = "The opening time of the parking lot", implementation = String.class, format = "HH:mm:ss", requiredMode = Schema.RequiredMode.REQUIRED)
  private Time openingTime;
  @NotNull(message = "Closing time must not be null.")
  @Schema(example= "22:00:00", description = "The closing time of the parking lot", implementation = String.class, format = "HH:mm:ss", requiredMode = Schema.RequiredMode.REQUIRED)
  private Time closingTime;

  @NotNull(message = "Large spot fee must not be null.")
  @PositiveOrZero(message = "Large spot fee must be non-negative.")
  @Schema(example= "2.5", description = "The fee for a large spot", requiredMode = Schema.RequiredMode.REQUIRED)
  private Double largeSpotFee;

  @NotNull(message = "Small spot fee must not be null.")
  @PositiveOrZero(message = "Small spot fee must be non-negative.")
  @Schema(example= "1.25", description = "The fee for a small spot", requiredMode = Schema.RequiredMode.REQUIRED)
  private Double smallSpotFee;

  @NotNull(message = "Monthly flat fee must not be null.")
  @PositiveOrZero(message = "Small spot monthly flat fee must be non-negative.")
  @Schema(example= "55.0", description = "The flat fee for the monthly a small spot", requiredMode = Schema.RequiredMode.REQUIRED)
  private Double smallSpotMonthlyFlatFee;

  @NotNull(message = "Monthly flat fee must not be null.")
  @PositiveOrZero(message = "Large spot monthly flat fee must be non-negative.")
  @Schema(example= "45.25", description = "The flat fee for the monthly a large spot", requiredMode = Schema.RequiredMode.REQUIRED)
  private Double largeSpotMonthlyFlatFee;

	public void setOpeningTime(Time openingTime) {
    this.openingTime = openingTime;
	}

    public void setClosingTime(Time closingTime) {
		this.closingTime = closingTime;
	}

    public void setLargeSpotFee(Double largeSpotFee) {
		this.largeSpotFee = largeSpotFee;
	}

    public void setSmallSpotFee(Double smallSpotFee) {
		this.smallSpotFee = smallSpotFee;
	}

    public void setSmallSpotMonthlyFlatFee(Double smallSpotMonthlyFlatFee) {
		this.smallSpotMonthlyFlatFee = smallSpotMonthlyFlatFee;
	}

  public void setLargeSpotMonthlyFlatFee(Double largeSpotMonthlyFlatFee) {
		this.largeSpotMonthlyFlatFee = largeSpotMonthlyFlatFee;
	}

    public Time getOpeningTime() { return openingTime; }

    public Time getClosingTime() { return closingTime; }

    public Double getLargeSpotFee() { return largeSpotFee;}

    public Double getSmallSpotFee() { return smallSpotFee; }

    public Double getSmallSpotMonthlyFlatFee() { return smallSpotMonthlyFlatFee; }

    public Double getLargeSpotMonthlyFlatFee() { return largeSpotMonthlyFlatFee; }

    public ParkingLot toModel() {
    ParkingLot p = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallSpotMonthlyFlatFee, largeSpotMonthlyFlatFee);
		return p;
	}
}
