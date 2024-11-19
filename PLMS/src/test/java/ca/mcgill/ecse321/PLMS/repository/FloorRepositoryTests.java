package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;

/**
 * Testing suite for the persistence of the Floor class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class FloorRepositoryTests {

    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        floorRepository.deleteAll();

        parkingLotRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadFloor(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        int floorNumber = 0;
        int largeSpotCapacity = 15;
        int smallSpotCapacity = 60;

        //To create the object we need to first create a parking lot
        //------  Parking lot  ------//
        Time openingTime = Time.valueOf("8:00:00");
        Time closingTime = Time.valueOf("20:00:00");
        double largeSpotFee = 10.10;
        double smallSpotFee = 5.5;
        double monthlyFlatFee = 50.50;
        ParkingLot parkingLot = new ParkingLot();
        //Set all parameters
        parkingLot.setOpeningTime(openingTime);
        parkingLot.setClosingTime(closingTime);
        parkingLot.setLargeSpotFee(largeSpotFee);
        parkingLot.setSmallSpotFee(smallSpotFee);
        parkingLot.setSmallSpotMonthlyFlatFee(monthlyFlatFee);
        parkingLot.setLargeSpotMonthlyFlatFee(monthlyFlatFee + 10);
        //Save parking lot
        parkingLot = parkingLotRepository.save(parkingLot);
        int parkingLotId = parkingLot.getId();
        //---------------------------//

        Floor mainFloor = new Floor();
        
        //Set all the attributes
        mainFloor.setFloorNumber(floorNumber);
        mainFloor.setLargeSpotCapacity(largeSpotCapacity);
        mainFloor.setSmallSpotCapacity(smallSpotCapacity);

        mainFloor.setParkingLot(parkingLot);


        //=-=-=-=-=-=- Save floor -=-=-=-=-=-=//
        mainFloor = floorRepository.save(mainFloor);
        int id = mainFloor.getFloorNumber();

        //=-=-=-=-=-=- Read floor -=-=-=-=-=-=//
        mainFloor = floorRepository.findFloorByFloorNumber(id);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(mainFloor);
        assertEquals(floorNumber, mainFloor.getFloorNumber());
        assertEquals(largeSpotCapacity, mainFloor.getLargeSpotCapacity());
        assertEquals(smallSpotCapacity, mainFloor.getSmallSpotCapacity());

        assertEquals(parkingLotId, mainFloor.getParkingLot().getId());
    }
}
