package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.repository.ParkingLotRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ParkingLotServiceTests {
    @Mock
    private ParkingLotRepository parkingLotRepository;


    @InjectMocks
    private ParkingLotService parkingLotService;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){

        parkingLotRepository.deleteAll();
    }

    
    @Test
    /**
     * Get the parking lot
     */
    public void testGetValidParkingLot(){
      // set up a mock parkingLot to be used by parkinglot repo
      final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("22:00:00");
      final double largeSpotFee = 25;
      final double smallSpotFee = 3;
      final double smallMonthlyFlatFee = 2;
      final double largeMonthlyFlatFee = 2;
      ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
      List<ParkingLot> parkingLots = new ArrayList<>();
      parkingLots.add(parkingLot);
      when(((List<ParkingLot>) parkingLotRepository.findAll())).thenReturn(parkingLots);

      ParkingLot output = parkingLotService.getParkingLot();
      assertEquals(parkingLot, output);
    }

    @Test
    public void testGetInvalidParkingLot(){
        PLMSException e = assertThrows(PLMSException.class, () -> parkingLotService.getParkingLot());
        assertEquals(e.getMessage(), "Parking Lot not found.");
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }


    @Test
    /**
     * Create a valid parking lot
     */
    public void testCreateValidParkingLot(){
        // set up a mock parkingLot to be used by parkingLot repo
        final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("22:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 10;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(parkingLotRepository.findAll()).thenReturn(parkingLots);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);
        ParkingLot output = parkingLotService.createParkingLot(parkingLot);
        assertEquals(output, parkingLot);

        verify(parkingLotRepository, times(1)).save(parkingLot);
    }

    @Test
    /**
     * Equal opening and closing times
     */
    public void testCreateInvalidEqualTimesParkingLot(){
        // set up a mock parkingLot to be used by parkingLot repo
        final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("6:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 2;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(((List<ParkingLot>) parkingLotRepository.findAll())).thenReturn(parkingLots);
        PLMSException e = assertThrows(PLMSException.class, () -> parkingLotService.createParkingLot(parkingLot));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(),"Opening and closing times cannot be the same.");
    }

    @Test
    /**
     * Opening after closing time.
     */
    public void testCreateInvalidTimesParkingLot() {
        // set up a mock parkingLot to be used by parkingLot repo
        final Time openingTime = Time.valueOf("22:00:00");
        final Time closingTime = Time.valueOf("6:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 2;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        List<ParkingLot> parkingLots = new ArrayList<>();
        when(((List<ParkingLot>) parkingLotRepository.findAll())).thenReturn(parkingLots);
        PLMSException e = assertThrows(PLMSException.class, () -> parkingLotService.createParkingLot(parkingLot));
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
        assertEquals(e.getMessage(),"Opening time cannot be after closing time.");
    }

    @Test
    /**
     * Valid update
     */
    public void testUpdateValidParkingLot() {
        final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("22:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 2;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
        parkingLotArrayList.add(parkingLot);
        when (parkingLotRepository.findAll()).thenReturn(parkingLotArrayList);
        final Time openingTime1 = Time.valueOf("7:00:00");
        final Time closingTime1 = Time.valueOf("23:00:00");
        final double largeSpotFee1 = 50;
        final double smallSpotFee1 = 10;
        final double smallMonthlyFlatFee1 = 10;
        final double largeMonthlyFlatFee1 = 20;
        ParkingLot parkingLot1 = new ParkingLot(openingTime1, closingTime1, largeSpotFee1, smallSpotFee1, smallMonthlyFlatFee1, largeMonthlyFlatFee1);
        when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot1);
        ParkingLot output = parkingLotService.updateParkingLot(parkingLot1);
        assertEquals(output, parkingLot1);
    }

    @Test
    /**
     * equal times in the update
     */
    public void testUpdateInvalidEqualTimesParkingLot() {
        final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("22:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 2;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
        parkingLotArrayList.add(parkingLot);
        when (parkingLotRepository.findAll()).thenReturn(parkingLotArrayList);
        final Time openingTime1 = Time.valueOf("6:00:00");
        final Time closingTime1 = Time.valueOf("6:00:00");
        final double largeSpotFee1 = 50;
        final double smallSpotFee1 = 10;
        final double smallMonthlyFlatFee1 = 10;
        final double largeMonthlyFlatFee1 = 20;
        ParkingLot parkingLot1 = new ParkingLot(openingTime1, closingTime1, largeSpotFee1, smallSpotFee1, smallMonthlyFlatFee1, largeMonthlyFlatFee1);
        PLMSException e = assertThrows(PLMSException.class, () -> parkingLotService.updateParkingLot(parkingLot1));
        assertEquals(e.getMessage(), "Opening and closing times cannot be the same.");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }

    @Test
    /**
     * Start time after closing time update.
     */
    public void testUpdateInvalidTimesParkingLot() {
        final Time openingTime = Time.valueOf("6:00:00");
        final Time closingTime = Time.valueOf("22:00:00");
        final double largeSpotFee = 25;
        final double smallSpotFee = 3;
        final double smallMonthlyFlatFee = 2;
        final double largeMonthlyFlatFee = 2;
        ParkingLot parkingLot = new ParkingLot(openingTime, closingTime, largeSpotFee, smallSpotFee, smallMonthlyFlatFee, largeMonthlyFlatFee);
        ArrayList<ParkingLot> parkingLotArrayList = new ArrayList<>();
        parkingLotArrayList.add(parkingLot);
        when (parkingLotRepository.findAll()).thenReturn(parkingLotArrayList);
        final Time openingTime1 = Time.valueOf("22:00:00");
        final Time closingTime1 = Time.valueOf("6:00:00");
        final double largeSpotFee1 = 50;
        final double smallSpotFee1 = 10;
        final double smallMonthlyFlatFee1 = 10;
        final double largeMonthlyFlatFee1 = 20;
        ParkingLot parkingLot1 = new ParkingLot(openingTime1, closingTime1, largeSpotFee1, smallSpotFee1, smallMonthlyFlatFee1, largeMonthlyFlatFee1);
        PLMSException e = assertThrows(PLMSException.class, () -> parkingLotService.updateParkingLot(parkingLot1));
        assertEquals(e.getMessage(), "Opening time cannot be after closing time.");
        assertEquals(e.getStatus(), HttpStatus.BAD_REQUEST);
    }





}
