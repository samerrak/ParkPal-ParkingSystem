package ca.mcgill.ecse321.PLMS.repository;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;

/**
 * DAO class in the spring framework that acts as a link between the database and java application
 * for CRUD operations of the ServiceAppointment class in the context of the PLMS software system
 */
public interface ServiceAppointmentRepository extends CrudRepository<ServiceAppointment, Integer>{

    /**
     * Find the service appointment based on id
     * @param id - id of the service appointment
     * @return service appointment with id id
     */
    public ServiceAppointment findServiceAppointmentById(int id);
    
}
