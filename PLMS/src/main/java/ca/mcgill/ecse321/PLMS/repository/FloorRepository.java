package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.Floor;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the Floor class in the context of the PLMS software system
 */
public interface FloorRepository extends CrudRepository<Floor, Integer>{

    /**
     * Find the floor based on its id
     * @param id
     * @return floor with id id
     */
    public Floor findFloorByFloorNumber(int floorNumber);
    
}
