package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the MonthlyCustomer class in the context of the PLMS software system
 */
public interface MonthlyCustomerRepository extends CrudRepository<MonthlyCustomer, String>{

    /**
     * Find a monthly customer by their email
     * @param email - email of monthly customer
     * @return MonthlyCustomer with email email
     */
    public MonthlyCustomer findMonthlyCustomerByEmail(String email);
    
}
