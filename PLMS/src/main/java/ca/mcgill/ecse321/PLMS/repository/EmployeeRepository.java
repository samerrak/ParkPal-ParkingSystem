package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.Employee;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the Employee class in the context of the PLMS software system
 */
public interface EmployeeRepository extends CrudRepository<Employee, String>{

    /**
     * Find an employee based on their unique email
     * @param email - email of the employee
     * @return email of the employee with email
     */
    public Employee findEmployeeByEmail(String email);
    
}
