package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.MonthlyPass;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the MonthlyPass class in the context of the PLMS software system
 */
public interface MonthlyPassRepository extends CrudRepository<MonthlyPass, Integer>{

    /**
     * Find a monthly pass based on id
     * @param id - id of monthly pass
     * @return MonthlyPass of id id
     */
    public MonthlyPass findMonthlyPassById(int id);


}

