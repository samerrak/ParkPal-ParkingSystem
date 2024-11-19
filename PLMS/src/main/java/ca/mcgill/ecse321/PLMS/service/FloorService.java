package ca.mcgill.ecse321.PLMS.service;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.FloorRepository;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import jakarta.transaction.Transactional;

/**
 * Service class for all the business methods related to the floor model class in the PLMS system
 */
@Service
public class FloorService {

    @Autowired
    FloorRepository floorRepository; // Repository from where the floor objects are persisted

    @Autowired
    ParkingLotRepository parkingLotRepository; // Repository from where the parking lot objects are persisted

    /**
     * Service method to fetch all floors in the database
     * @return an arraylist of all the floor objects in the database
     */
    @Transactional
    public Iterable<Floor> getAllFloors(){
        ArrayList<Floor> arrayList = (ArrayList<Floor>) floorRepository.findAll();
        if (arrayList.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no floors in the system.");
       return arrayList;
    }

    /**
     * Service method to fetch a floor with a specific floor number in the database
     * @param floorNumber denoting the level of the floor in the parking lot
     * @return the floor instance corresponding to the number given as a parameter
     */
    @Transactional
    public Floor getFloorByFloorNumber(int floorNumber){
        Floor floor = floorRepository.findFloorByFloorNumber(floorNumber);
        if (floor == null){
            throw new PLMSException(HttpStatus.NOT_FOUND, "Floor with floor number: " + floorNumber + " does not exist.");
        }
        return floor;
    }

    /**
     * Service method to store the created floor object into the database
     * @param floor the instance of the floor to be persisted
     * @return the created floor instance
     */
    @Transactional
    public Floor createFloor(Floor floor){
        //checks on the new object are made in the DTO
        //check if the floor already exists
        if (floorRepository.findFloorByFloorNumber(floor.getFloorNumber()) != null){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Floor with floor number: " + floor.getFloorNumber() + " already exists.");
        }

        // check for the parking lot in the database, if it doesn't exist yet we cannot create the floor
        Iterable<ParkingLot> lots = parkingLotRepository.findAll();
        if (lots == null || !lots.iterator().hasNext()){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Cannot create floor since the parking lot has not been created.");
        }
        ParkingLot lot = lots.iterator().next();
        floor.setParkingLot(lot);

        //create object
        floor = floorRepository.save(floor);
        //returned created object
        return floor;
    }

    /**
     * Service method that updates a floor object in the database
     * @param floor updated instance to be persisted
     * @return old version of the floor that has been updated
     */
    @Transactional
    public Floor updateFloor(Floor floor){
        //check if the floor exists (the floor has to exist to edit it)
        Floor existingFloor = getFloorByFloorNumber(floor.getFloorNumber());

        // update the properties of the existing Floor entity
        existingFloor.setIsMemberOnly(floor.getIsMemberOnly());
        existingFloor.setLargeSpotCapacity(floor.getLargeSpotCapacity());
        existingFloor.setSmallSpotCapacity(floor.getSmallSpotCapacity());
        // save the changes to the database
        existingFloor = floorRepository.save(existingFloor);
        return existingFloor;
    }

    /**
     * Service method that deletes the floor with floor number floorNumber from the database
     * @param floorNumber level number of the floor to be deleted
     */
    @Transactional
    public void deleteFloorByFloorNumber(int floorNumber){
        //Checks for non null are made in the method already
        Floor floor = getFloorByFloorNumber(floorNumber);
        floorRepository.delete(floor);
    }



}
