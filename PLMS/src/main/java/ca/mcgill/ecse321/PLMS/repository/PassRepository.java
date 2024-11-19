package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.Pass;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the Pass abstract class in the context of the PLMS software system
 */
public interface PassRepository extends CrudRepository<Pass, Integer>{

  /**
	 * Find a pass by id
	 * @param id - id of the pass
	 * @return Pass found with id
	 */
    public Pass findPassById(int id);
}
