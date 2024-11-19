package ca.mcgill.ecse321.PLMS;

import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import ca.mcgill.ecse321.PLMS.model.Floor;
import ca.mcgill.ecse321.PLMS.model.Owner;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;
import ca.mcgill.ecse321.PLMS.service.FloorService;
import ca.mcgill.ecse321.PLMS.service.OwnerService;
import ca.mcgill.ecse321.PLMS.service.ParkingLotService;
import ca.mcgill.ecse321.PLMS.service.ServiceService;

/**
 * Main Application class for the PLMS software system
 * It's the class that activates the whole software system when run
 * The PLMS software system implements the Spring framework
 */
@SpringBootApplication
public class PlmsApplication {

	//Initialization of the parkingLot if it is the first time it is used
	@Bean
    CommandLineRunner initDatabase(@Autowired OwnerService ownerService,@Autowired OwnerRepository ownerRepository,@Autowired ParkingLotService parkingLotService, @Autowired FloorService floorService, @Autowired ServiceService serviceService) {
        return args -> {
			if (ownerRepository.count() == 0){
				ownerService.createOwnerAccount(new Owner("admin@mail.com", "MyParking1ot$", "Admin"));
				parkingLotService.createParkingLot(new ParkingLot(new Time(0, 0, 0), new Time(23, 59, 59), 20, 10, 30, 50));
				floorService.createFloor(new Floor(1, 20, 50, false));
				for (int i=2; i<6; i++){
					if (i == 2 || i == 3){
						floorService.createFloor(new Floor(i, 0, 100, true));
					}
					else {
						floorService.createFloor(new Floor(i, 0, 100, false));
					}
				}
				serviceService.createService(new Service("Winter Tire Change",30 ,1 ));
				serviceService.createService(new Service("Car Cleaning",20 ,1.5 ));
				serviceService.createService(new Service("Oil Change",30 ,0.5 ));
			}
        };
    }
	
	/**
	 * Main method, start point of the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(PlmsApplication.class, args);
	}

}
