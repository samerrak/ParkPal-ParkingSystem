package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.GuestPass;
import ca.mcgill.ecse321.PLMS.repository.GuestPassRepository;
import jakarta.transaction.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for all the business methods related to the guest pass model class in the PLMS system
 */
@Service
public class GuestPassService {

    @Autowired
    GuestPassRepository guestPassRepository; // Repository from where the guest pass objects are persisted

    @Autowired
    FloorRepository floorRepository; // Repository from where the floor objects are persisted

    @Autowired
    ParkingLotRepository parkingLotRepository; // Repository from where the parking lot objects are persisted

    /**
     * Service method to fetch all guest passes in the database
     * @return the arraylist of guest passes
     */
    @Transactional
    public Iterable<GuestPass> getAllGuestPasses() {
        ArrayList<GuestPass> arrayList = (ArrayList<GuestPass>) guestPassRepository.findAll();
        if (arrayList.isEmpty()){
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no guest passes in the system.");
        }
        return arrayList;
    }

    /**
     * Service method to fetch a guest pass with a specific guest pass id in the database
     * @param guestPassId id of the pass that is requested from persistence layer
     * @return the requested guest pass
     */
    @Transactional
    public GuestPass getGuestPassById(int guestPassId) {
        GuestPass guestPass = guestPassRepository.findGuestPassById(guestPassId);
        if (guestPass == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "Guest pass with id: " + guestPassId + " does not exist.");
        }
        return guestPass;
    }

    /**
     * Service method to store the created guest pass object into the database
     * @param guestPass instance of the pass to be persisted
     * @param floorNumber level of the floor on which the pass applies
     * @param nrIncrements increments of 15 minutes indicating the duration of the pass
     * @param currentTime time of purchase of the pass
     * @return the newly created guest pass (if successful)
     */
    @Transactional
    public GuestPass createGuestPass(GuestPass guestPass, int floorNumber, int nrIncrements, LocalDateTime currentTime) {
        //checks on the new object are made in the DTO
        // Get the associated floor from floor number inputted into guestPass
        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if (floor == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "The floor with floor number " + floorNumber + " does not exist.");

        }
        guestPass.setFloor(floor);
        ParkingLot parkingLot = floor.getParkingLot();

        //Check if floor is a member only
        if (floor.getIsMemberOnly()) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Floor " + floorNumber + " is reserved for monthly members");
        }
        // Check if the spot is reserved for less than 12 hours
        if (nrIncrements > 12*4){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot reserve spot for more than 12 hours");
        }

        // Extract start and end time
        LocalDateTime localDateTime = currentTime;
        Time startTime = Time.valueOf(localDateTime.toLocalTime());
        LocalDateTime localEndTime = localDateTime.plusMinutes(nrIncrements*15);
        Time endTime = Time.valueOf(localEndTime.toLocalTime());
        validateGuestPassHours(startTime, endTime, parkingLot.getOpeningTime(), parkingLot.getClosingTime());

        // Check if spot is not occupied
        if (isSpotOccupied(floor.getFloorNumber(), guestPass.getSpotNumber(),startTime, endTime, localDateTime.toLocalDate())){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Spot " + guestPass.getSpotNumber() + " is currently occupied");
        }

        // set start and end time
        guestPass.setStartTime(startTime);
        guestPass.setEndTime(endTime);
        guestPass.setDate(localDateTime.toLocalDate());

        // check to see if we've exceed the floor capacity by booking this spot.
        if(hasExceededCapacity(currentTime, localEndTime ,floorNumber, guestPass.getIsLarge())){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "All spots of this size on floor " + floorNumber +" are occupied.");
        }

        // Set fee
        if (guestPass.getIsLarge()){
            guestPass.setFee(nrIncrements*parkingLot.getLargeSpotFee());
        } else{
            guestPass.setFee(nrIncrements*parkingLot.getSmallSpotFee());

        }
        guestPass =  guestPassRepository.save(guestPass);


        // Returned created object
        return guestPass;
    }

    /**
     * Helper method to check if we have exceeded capacity on a floor based
     * on the number of guest passes and monthly passes on that floor
     * @param floorNumber - number of the floor
     * @param isLarge - size of the spot
     * @return - true if we've reached capacity for those spots, false otherwise
     */
    public boolean hasExceededCapacity(LocalDateTime currentTime, LocalDateTime endTime, int floorNumber, boolean isLarge){
        // get all the passes
        ArrayList<GuestPass> guestPasses = (ArrayList<GuestPass>) guestPassRepository.findAll();
        // number of passes on the floor
        int numberOfPasses = 0;
        // filter through the guest passes to find passes that are of the same size and same floor number
        for (GuestPass pass : guestPasses){
            if(pass.getFloor().getFloorNumber() == floorNumber && pass.getIsLarge() == isLarge && isOverlappingGuestPass(currentTime, endTime,pass.getDate(), pass.getStartTime().toLocalTime(), pass.getEndTime().toLocalTime())){

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
     * Function for checking if a pass is active right at this point in time, given the start and end times
     * and date.
     * @param date - date of the pass
     * @param startTime - start time of the pass
     * @param endTime - end time of the pass
     * @return true if the pass is currently active
     */
    public boolean isOverlappingGuestPass(LocalDateTime currentTime, LocalDateTime guestPassEndTime, LocalDate date, LocalTime startTime, LocalTime endTime) {
        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);
        return currentTime.isBefore(end) && guestPassEndTime.isAfter(start);
    }

    /**
     * Service method that deletes the guest pass with guest pass id guestPassId from the database
     * @param guestPassId id of the pass to be deleted
     */
    @Transactional
    public void deleteGuestPassById(int guestPassId) {
        //Checks for non null are made in the method already
        GuestPass guestPass = getGuestPassById(guestPassId);
        guestPassRepository.delete(guestPass);

    }

    /**
     * Service method that acquires a list of passes by floor
     * @param floorNumber level of the floor we want to get the passes from
     * @return an arraylist of the guest passes corresponding to the appropriate floor
     */
    @Transactional
    public List<GuestPass> getGuestPassesByFloor(int floorNumber) {
        List<GuestPass> guestPasses = new ArrayList<GuestPass>();
        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if (floor == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "The floor with floor number " + floorNumber + " does not exist.");
        }
        for (GuestPass pass : guestPassRepository.findAll()) {
            if (pass.getFloor() != null && pass.getFloor().equals(floor)) {
                guestPasses.add(pass);
            }
        }
        if (guestPasses.size() == 0) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no guest passes on floor " + floorNumber);
        }
        return guestPasses;
    }

    /**
     * Service that fetches the guest passes that were created in a certain day
     * @param date day on which the passes were created
     * @return a list containing guest passes created in the same day
     */
    @Transactional
    public List<GuestPass> getGuestPassesByDate(LocalDate date) {
        List<GuestPass> guestPasses = (List<GuestPass>) guestPassRepository.findAll();
        List<GuestPass> guestPassesByDate = new ArrayList<>();
        for (GuestPass guestPass : guestPasses) {
            if (guestPass.getDate().equals(date)) {
                guestPassesByDate.add(guestPass);
            }
        }
        if (guestPassesByDate.isEmpty()) {
            // null means guestPasses don't exist for that date, throw PLMS error
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no guest passes for date " + date);
        }
        return guestPassesByDate;
    }


    /**
     * Service method that validates the guest pass hours
     * @param startTime time from which the pass is valid
     * @param endTime end time for the validity of the pass
     * @param openingTime opening time of the parking lot
     * @param closingTime closing time of the parking lot
     */
    public void validateGuestPassHours(Time startTime, Time endTime, Time openingTime, Time closingTime) {

        // check to see if start time is before the opening time
        int comparisonResult1 = startTime.compareTo(openingTime);
        if (comparisonResult1 < 0) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have a guest pass beginning before the lot opens.");
        }

        // check to see if the start time is after the lot closes
        int comparisonResult2 = startTime.compareTo(closingTime);
        if (comparisonResult2 > 0) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have a guest pass beginning after the lot closes.");
        }

        // check to see if the end time is after the lot closes
        int comparisonResult3 = endTime.compareTo(closingTime);
        if (comparisonResult3 > 0) {
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot have a guest pass ending after the lot closes.");
        }

    }


    /**
     * Service method that indicates if a certain spot is occupied (so it can't be reserved by someone else in the same time)
     * @param floorNumber level floor on which the spot is
     * @param spotNumber number of the spot on the specified floor
     * @param startTime time from which we want to see if the spot is occupated
     * @param endTime end of the time interval for the spot's occupancy
     * @param date day on which we want to check the occupancy of a spot
     * @return whether or not the spot is occupied (true or false)
     */
    public boolean isSpotOccupied(int floorNumber, String spotNumber, Time startTime, Time endTime, LocalDate date) {
        try {
            List<GuestPass> guestPasses = getGuestPassesByFloor(floorNumber); // get all guest passes for the floor
            for (GuestPass guestPass : guestPasses) {
                if (guestPass.getSpotNumber().equals(spotNumber)) { // check if spot number matches
                    Time guestPassStartTime = guestPass.getStartTime();
                    Time guestPassEndTime = guestPass.getEndTime();
                    if ((((guestPassStartTime.before(endTime) && guestPassEndTime.after(startTime))) || (startTime.before(guestPassEndTime) && endTime.after(guestPassStartTime))) && date.isEqual(guestPass.getDate())) {

                        // guest pass overlaps with the specified time range
                        return true;
                    }
                }
            }
            return false; // guest pass not found

        } catch (PLMSException e){ // In case no guest passes existed prior
            return false;
        }
    }
}