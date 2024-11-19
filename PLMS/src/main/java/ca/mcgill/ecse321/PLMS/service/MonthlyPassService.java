package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.*;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyPassRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for all the business methods related to the monthly customer model class in the PLMS system
 */
@Service
public class MonthlyPassService {

    @Autowired
    MonthlyPassRepository monthlyPassRepository; // Repository from where the monthly pass objects are persisted

    @Autowired
    FloorRepository floorRepository; // Repository from where the floor objects are persisted

    @Autowired
    ParkingLotRepository parkingLotRepository; // Repository from where the parking lot objects are persisted

    @Autowired
    MonthlyCustomerRepository monthlyCustomerRepository; // Repository from where the monthly customer objects are persisted

    /**
     * Service method to fetch all monthly passes in the database
     * @return an arraylist of monthly passes present in the database
     */
    @Transactional
    public Iterable<MonthlyPass> getAllMonthlyPasses() {
        ArrayList<MonthlyPass> arrayList = (ArrayList<MonthlyPass>) monthlyPassRepository.findAll();
        if (arrayList.isEmpty()){
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no monthly passes in the system.");
        }
        return arrayList;
    }

    /**
     * Service method to fetch a monthly pass with a specific id in the database
     * @param monthlyPassId id of the monthly pass to be fetched
     * @return monthly pass corresponding to the id passed in parameters
     */
    @Transactional
    public MonthlyPass getMonthlyPassById(int monthlyPassId) {
        MonthlyPass monthlyPass = monthlyPassRepository.findMonthlyPassById(monthlyPassId);
        if (monthlyPass == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "Monthly pass with id: " + monthlyPassId + " does not exist.");
        }
        return monthlyPass;
    }

    /**
     * Service method to store the created monthly pass object into the database
     * @param monthlyPass instance to be persisted
     * @param floorNumber floor on which the monthly pass is activated
     * @param nrMonths duration of the monthly pass
     * @param email monthly customer's email that is purchasing the monthly pass
     * @return
     */
    @Transactional
    public MonthlyPass createMonthlyPass(MonthlyPass monthlyPass, int floorNumber, int nrMonths, String email) {
        // Get the associated floor from floor number inputted into monthly pass


        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if (floor == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "The floor with floor number " + floorNumber + " does not exist.");
        }
        ParkingLot parkingLot = floor.getParkingLot();

        // Check if floor is a guest only
        if (!floor.getIsMemberOnly()) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Floor " + floorNumber + " is reserved for guest passes only.");
        }

        // Get start date and find end date
        LocalDate startDate = monthlyPass.getStartDate();
        LocalDate localEndDate = startDate.plusMonths(nrMonths);
        // Convert back to Date
        monthlyPass.setEndDate(localEndDate);

        // Check if the spot is not occupied
        if (isSpotOccupied(floor.getFloorNumber(), monthlyPass.getSpotNumber(), monthlyPass.getStartDate(), localEndDate)) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Spot " + monthlyPass.getSpotNumber() + " is currently occupied");
        }

        // check to see if we've exceed the floor capacity by booking this spot.
        if(hasExceededCapacity(startDate, localEndDate ,floorNumber, monthlyPass.getIsLarge())){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "All spots of this size on floor " + floorNumber +" are occupied.");
        }

        if (email != null) {
            MonthlyCustomer monthlyCustomer = monthlyCustomerRepository.findMonthlyCustomerByEmail(email);
            if (monthlyCustomer == null)
                throw new PLMSException(HttpStatus.NOT_FOUND, "There is no customer account associated with this email");
            else
                monthlyPass.setCustomer(monthlyCustomer);
        }


        // Set fee and increment floor counter
        if (monthlyPass.getIsLarge()) {
            monthlyPass.setFee(parkingLot.getLargeSpotFee() * nrMonths);
        } else {
            monthlyPass.setFee(parkingLot.getSmallSpotFee() * nrMonths);
        }
        monthlyPass.setFloor(floor);
        // Create object
        monthlyPass = monthlyPassRepository.save(monthlyPass);

        // Returned created object
        return monthlyPass;
    }

    /**
     * Helper method to check if we have exceeded capacity on a floor based
     * on the number of guest passes and monthly passes on that floor
     * @param floorNumber - number of the floor
     * @param isLarge - size of the spot
     * @return - true if we've reached capacity for those spots, false otherwise
     */
    public boolean hasExceededCapacity(LocalDate newPassStartDate, LocalDate newPassEndDate, int floorNumber, boolean isLarge){
        // get all the passes
        ArrayList<MonthlyPass> monthlyPasses = (ArrayList<MonthlyPass>) monthlyPassRepository.findAll();
        // number of passes on the floor
        int numberOfPasses = 0;
        for (MonthlyPass pass : monthlyPasses){
            if(pass.getFloor().getFloorNumber() == floorNumber && pass.getIsLarge() == isLarge && isOverlappingMonthlyPass(newPassStartDate, newPassEndDate, pass.getStartDate(), pass.getEndDate())){

                numberOfPasses += 1;
            }
        }

        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if(isLarge){
            return numberOfPasses >= floor.getLargeSpotCapacity();
        }else{
            return numberOfPasses >= floor.getSmallSpotCapacity();
        }
    }

    /**
     * Checks to see overlap between two monthly passes
     * @param newPassStartDate first pass beginning
     * @param newPassEndDate first pass ending
     * @param otherPassStartDate second pass beginning
     * @param otherPassEndDate second pass ending
     * @return whether or not the passes overlap
     */
    public boolean isOverlappingMonthlyPass(LocalDate newPassStartDate, LocalDate newPassEndDate,LocalDate otherPassStartDate, LocalDate otherPassEndDate) {
        return (newPassStartDate.isBefore(otherPassEndDate) && newPassEndDate.isAfter(otherPassStartDate)) || (otherPassStartDate.isBefore(newPassEndDate) && otherPassEndDate.isAfter(newPassStartDate));
    }

    /**
     * Service method that returns monthly passes on a specified floor
     * @param floorNumber level of the floor from which to fetch the monthly passes
     * @return an arraylist of the monthly passes requested
     */
    @Transactional
    public List<MonthlyPass> getMonthlyPassesByFloor(int floorNumber) {
        List<MonthlyPass> monthlyPasses = new ArrayList<>();
        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if (floor == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "The floor with floor number " + floorNumber + " does not exist.");
        }
        for (MonthlyPass pass : monthlyPassRepository.findAll()) {
            if (pass.getFloor() != null && pass.getFloor().equals(floor)) {
                monthlyPasses.add(pass);
            }
        }
        if (monthlyPasses.size() == 0) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no monthly passes on floor " + floorNumber);
        }
        return monthlyPasses;
    }
    
    /**
     * Service method that fetches the monthly passes of a monthly customer
     * @param email monthly customer's email related to the account
     * @return arraylist containing the requested monthly passes
     */
    @Transactional
    public List<MonthlyPass> getMonthlyPassesByMonthlyCustomer(String email) {
        List<MonthlyPass> monthlyPassesbyMonthlyCustomer  = new ArrayList<MonthlyPass>();
        List<MonthlyPass> monthlyPasses = (List<MonthlyPass>) monthlyPassRepository.findAll();

        MonthlyCustomer monthlyCustomer = monthlyCustomerRepository.findMonthlyCustomerByEmail(email);

        // Check if monthly customer exists
        if (monthlyCustomer == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "The account with email " + email + " does not exist.");
        }

        // Loop through all monthly passes in the system
        for (MonthlyPass monthlyPass : monthlyPasses) {
            // Check if the monthly pass belongs to the monthly customer
            if(monthlyPass.getCustomer() != null){
                if (monthlyPass.getCustomer().equals(monthlyCustomer)) {
                    monthlyPassesbyMonthlyCustomer.add(monthlyPass);
                }
            }  
        }
        if (monthlyPassesbyMonthlyCustomer.isEmpty()) {
            // null means monthlyPasses don't exist for that date, throw PLMS error
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no monthly passes for " + monthlyCustomer.getEmail());
        }

        return monthlyPassesbyMonthlyCustomer;
    }

    /**
     * Return all of the monthly passes that are active on a given date.
     * @param date - date we want to search for
     * @return - all passes active on that date
     */
    @Transactional
    public List<MonthlyPass> getMonthlyPassesByDate(LocalDate date) {
        List<MonthlyPass> monthlyPasses = (List<MonthlyPass>) monthlyPassRepository.findAll();
        List<MonthlyPass> monthlyPassesByDate = new ArrayList<>();
        for (MonthlyPass monthlyPass : monthlyPasses) {
            if (monthlyPass.getStartDate().compareTo(date) <= 0 && monthlyPass.getEndDate().compareTo(date) >= 0) {
                monthlyPassesByDate.add(monthlyPass);
            }
        }
        if (monthlyPassesByDate.isEmpty()) {
            // null means monthlyPasses don't exist for that date, throw PLMS error
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no monthly passes for date " + date);
        }
        return monthlyPassesByDate;
    }

    /**
     * Service method that determines if a certain spot is occupied for a period of time
     * @param floorNumber level floor that is considered in the parking lot
     * @param spotNumber number of the spot considered
     * @param startDate start of the interval of time considered
     * @param endDate end of the interval of time considered
     * @return whether or not the spot is occupied for the specified interval of time
     */
    public boolean isSpotOccupied(int floorNumber, String spotNumber, LocalDate startDate, LocalDate endDate) {
        try {
            List<MonthlyPass> monthlyPasses = getMonthlyPassesByFloor(floorNumber); // get all monthly passes for the floor
            for (MonthlyPass monthlyPass : monthlyPasses) {
                if (monthlyPass.getSpotNumber().equals(spotNumber)) { // check if spot number matches
                    LocalDate passStartDate = monthlyPass.getStartDate();
                    LocalDate passEndDate = monthlyPass.getEndDate();
                    if ((passStartDate.isBefore(endDate) && passEndDate.isAfter(startDate)) || startDate.isEqual(passStartDate) || (passEndDate.isEqual(endDate))) {
                        // monthly pass overlaps with the specified date range
                        return true;
                    }
                }
            }
            return false; // no overlapping monthly passes found

        } catch (PLMSException e) { // In case no guest passes existed prior
            return false;
        }

    }

}

