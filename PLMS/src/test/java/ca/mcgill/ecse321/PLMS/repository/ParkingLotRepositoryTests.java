package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ParkingLotRepositoryTests {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        parkingLotRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadParkingLot(){
        //=-=-=-=-=-=- Create Service object -=-=-=-=-=-=//

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
        int parkingLotId = parkingLot.getId();
        //--------------------------------//


        //=-=-=-=-=-=- Read service appointement -=-=-=-=-=-=//
        parkingLot = parkingLotRepository.findParkingLotById(parkingLotId);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(parkingLot);
        assertEquals(openingTime, parkingLot.getOpeningTime());
        assertEquals(closingTime, parkingLot.getClosingTime());
        assertEquals(smallSpotFee, parkingLot.getSmallSpotFee());
        assertEquals(largeSpotFee, parkingLot.getLargeSpotFee());
        assertEquals(monthlyFlatFee,parkingLot.getSmallSpotMonthlyFlatFee());
    } 
}
