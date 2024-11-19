package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.GuestPass;
import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.model.Pass;

import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testing suite for the persistence of the Pass class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class PassRepositoryTests {
    @Autowired
    private PassRepository passRepository;
    @Autowired
    private FloorRepository floorRepository;
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        passRepository.deleteAll();

        floorRepository.deleteAll();
        parkingLotRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadGuestPassInPassRepository(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        String confirmationCode = "NeverGonnaGiveYouUp";
        //=-=-=-=-=-=- Create Parking lot object -=-=-=-=-=-=//

        //Normal parameters
        Time openingTime = Time.valueOf("6:00:00");
        Time closingTime = Time.valueOf("22:00:00");
        double smallSpotFee = 3.5;
        double largeSpotFee = 4.5;
        double monthlyFlatFee = 150;
        ParkingLot parkingLot = new ParkingLot();
        //--------------------------------//

        //Set all parameters
        parkingLot.setOpeningTime(openingTime);
        parkingLot.setClosingTime(closingTime);
        parkingLot.setSmallSpotFee(smallSpotFee);
        parkingLot.setLargeSpotFee(largeSpotFee);
        parkingLot.setSmallSpotMonthlyFlatFee(monthlyFlatFee);
        parkingLot.setLargeSpotMonthlyFlatFee(monthlyFlatFee + 10);

        //Save service
        parkingLot = parkingLotRepository.save(parkingLot);

        //To create the object we need to first create a floor
        //---------  Floor  ---------//
        int floorNumber = 0;
        int largeSpotCapacity = 15;
        int smallSpotCapacity = 60;
        Floor mainFloor = new Floor();

        //Set all the attributes
        mainFloor.setFloorNumber(floorNumber);
        mainFloor.setLargeSpotCapacity(largeSpotCapacity);
        mainFloor.setSmallSpotCapacity(smallSpotCapacity);
        mainFloor.setParkingLot(parkingLot);

        //Save floor
        mainFloor = floorRepository.save(mainFloor);
        int floorId = mainFloor.getFloorNumber();
        //---------------------------//

        //Create Pass
        Pass pass = new GuestPass();

        //Set all parameters
        pass.setFee(fee);
        pass.setSpotNumber(spotNumber);
        pass.setLicensePlate(licensePlate);
        pass.setConfirmationCode(confirmationCode);

        pass.setFloor(mainFloor);

        //=-=-=-=-=-=- Save guest pass -=-=-=-=-=-=//
        passRepository.save(pass);
        int id = pass.getId();
    
        //=-=-=-=-=-=- Read guest pass -=-=-=-=-=-=//
        pass = (GuestPass) passRepository.findPassById(id);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(pass);
        assertEquals(fee, pass.getFee());
        assertEquals(spotNumber, pass.getSpotNumber());
        assertEquals(licensePlate, pass.getLicensePlate());
        assertEquals(confirmationCode, pass.getConfirmationCode());

        assertEquals(floorId, pass.getFloor().getFloorNumber());
    }

    @Test
    public void testPersistAndLoadMonthlyPassInPassRepository(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        double fee = 50.50;
        String spotNumber = "A24";
        String licensePlate = "123ABC123";
        String confirmationCode = "NeverGonnaGiveYouUp";
        //=-=-=-=-=-=- Create Parking lot object -=-=-=-=-=-=//

        //Normal parameters
        Time openingTime = Time.valueOf("6:00:00");
        Time closingTime = Time.valueOf("22:00:00");
        double smallSpotFee = 3.5;
        double largeSpotFee = 4.5;
        double monthlyFlatFee = 150;
        ParkingLot parkingLot = new ParkingLot();
        //--------------------------------//

        //Set all parameters
        parkingLot.setOpeningTime(openingTime);
        parkingLot.setClosingTime(closingTime);
        parkingLot.setSmallSpotFee(smallSpotFee);
        parkingLot.setLargeSpotFee(largeSpotFee);
        parkingLot.setSmallSpotMonthlyFlatFee(monthlyFlatFee);
        parkingLot.setLargeSpotMonthlyFlatFee(monthlyFlatFee + 10);

        //Save service
        parkingLot = parkingLotRepository.save(parkingLot);

        //To create the object we need to first create a floor
        //---------  Floor  ---------//
        int floorNumber = 0;
        int largeSpotCapacity = 15;
        int smallSpotCapacity = 60;
        Floor mainFloor = new Floor();

        //Set all the attributes
        mainFloor.setFloorNumber(floorNumber);
        mainFloor.setLargeSpotCapacity(largeSpotCapacity);
        mainFloor.setSmallSpotCapacity(smallSpotCapacity);
        mainFloor.setParkingLot(parkingLot);

        //Save floor
        mainFloor = floorRepository.save(mainFloor);
        int floorId = mainFloor.getFloorNumber();
        //---------------------------//

        //Create Pass
        Pass pass = new MonthlyPass();

        //Set all parameters
        pass.setFee(fee);
        pass.setSpotNumber(spotNumber);
        pass.setLicensePlate(licensePlate);
        pass.setConfirmationCode(confirmationCode);
        
        pass.setFloor(mainFloor);

        //=-=-=-=-=-=- Save monthly pass -=-=-=-=-=-=//
        passRepository.save(pass);
        int id = pass.getId();
    
        //=-=-=-=-=-=- Read monthly pass -=-=-=-=-=-=//
        pass = (MonthlyPass) passRepository.findPassById(id);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(pass);
        assertEquals(fee, pass.getFee());
        assertEquals(spotNumber, pass.getSpotNumber());
        assertEquals(licensePlate, pass.getLicensePlate());
        assertEquals(confirmationCode, pass.getConfirmationCode());

        assertEquals(floorId, pass.getFloor().getFloorNumber());
    }
}

