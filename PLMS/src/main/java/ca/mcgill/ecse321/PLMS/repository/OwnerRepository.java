package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.Owner;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the Owner class in the context of the PLMS software system
 */
public interface OwnerRepository extends CrudRepository<Owner, String>{

    /**
     * Find owner based on email
     * @param email 
     * @return owner with email email
     */
    public Owner findOwnerByEmail(String email);
    
}
