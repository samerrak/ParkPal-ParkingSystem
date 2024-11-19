package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.GuestPass;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the GuestPass class in the context of the PLMS software system
 */
public interface GuestPassRepository extends CrudRepository<GuestPass, Integer>{

    /**
     * Find the guest pass based on its id
     * @param id - id of guest pass
     * @return guestpass with id id
     */
    public GuestPass findGuestPassById(int id);
}
