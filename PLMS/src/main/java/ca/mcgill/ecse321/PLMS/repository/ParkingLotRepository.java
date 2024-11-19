package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.ParkingLot;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the ParkingLot class in the context of the PLMS software system
 */
public interface ParkingLotRepository extends CrudRepository<ParkingLot, Integer>{

    /**
     * Finds the parking lot based on the ID of the parking lot
     * @param id - id of the lot
     * @return ParkingLot with id id
     */
    public ParkingLot findParkingLotById(int id);
    
}
