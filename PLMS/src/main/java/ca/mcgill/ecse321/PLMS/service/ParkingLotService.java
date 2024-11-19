package ca.mcgill.ecse321.PLMS.service;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;

/**
 * Service class for all the business methods related to the parking lot model class in the PLMS system
 */
@Service
public class ParkingLotService {

    @Autowired
    ParkingLotRepository parkingLotRepository; // Repository from where the parking lot objects are persisted

    /**
     * Service method that fetches the unique parking lot from the database
     * @return the existing parking lot instance if present
     */
    @Transactional
    public ParkingLot getParkingLot() {
        List<ParkingLot> parkingLot = (ArrayList<ParkingLot>) parkingLotRepository.findAll();
        if (parkingLot.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "Parking Lot not found.");
        return parkingLot.get(0);
    }

    /**
     * Service method to persist a new instance of the parking lot
     * This method is meant to be successful only if this is the first instance created
     * @param parkingLot instance to be persisted
     * @return newly persisted instance if successful
     */
    @Transactional
    public ParkingLot createParkingLot(ParkingLot parkingLot) {
        validateOpeningClosingTime(parkingLot);
        if ( ((List<ParkingLot>) parkingLotRepository.findAll()).isEmpty())
            return parkingLotRepository.save(parkingLot);
        else
            throw new PLMSException(HttpStatus.CONFLICT, "Parking Lot already exists");
    }

    /**
     * Service method that updates metadata related to the parking lot as a whole
     * @param parkingLot updated instance to be persisted
     * @return the newly updated instance
     */
    @Transactional
    public ParkingLot updateParkingLot(ParkingLot parkingLot)
    {   
        validateOpeningClosingTime(parkingLot);
        ParkingLot p = getParkingLot();
        p.setClosingTime(parkingLot.getClosingTime());
        p.setOpeningTime(parkingLot.getOpeningTime());
        p.setLargeSpotFee(parkingLot.getLargeSpotFee());
        p.setSmallSpotFee(parkingLot.getSmallSpotFee());
        p.setSmallSpotMonthlyFlatFee(parkingLot.getSmallSpotMonthlyFlatFee());
        p.setLargeSpotMonthlyFlatFee(parkingLot.getLargeSpotMonthlyFlatFee());
        return parkingLotRepository.save(p);
    }

    /**
     * Service method that validates that the opening and closing time of the parking lot are acceptable
     * i.e. the opening time is before the closing time
     * @param parkingLot instance to be validated
     */
    public void validateOpeningClosingTime(ParkingLot parkingLot){
        int comparison = (parkingLot.getOpeningTime().toLocalTime()).compareTo((parkingLot.getClosingTime()).toLocalTime());
        if (comparison == 0)
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Opening and closing times cannot be the same.");
        else if(comparison > 0)
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Opening time cannot be after closing time.");

    }
}
