package ca.mcgill.ecse321.PLMS.service;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.PLMS.repository.ServiceRepository;
import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Service;

/**
 * Service class for all the business methods related to the service model class in the PLMS system
 */
@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository; // Repository from where the service objects are persisted


    /**
     * Service method to store the created service object into the database
     * @param service instance to be persisted
     * @return persisted instance if successful
     */
    @Transactional
    public Service createService(Service service){
        //checks on the new object are made in the DTO
        //check if the service already exists
        if (serviceRepository.findServiceByServiceName(service.getServiceName()) != null){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Service with service name: " + service.getServiceName() + " already exists.");
        }
        //create object
        service = serviceRepository.save(service);
        //returned created object
        return service;
    }

    /**
     * Service method that updates a floor object in the database
     * @param service updated instance to be persisted
     * @return persisted instance
     */
    @Transactional
    public Service updateService(Service service){
        //check if the service exists (the service has to exist to edit it)
        Service s = getServiceByServiceName(service.getServiceName());
        s.setServiceName(service.getServiceName());
        s.setCost(service.getCost());
        s.setLengthInHours(service.getLengthInHours());

        // save the changes to the database
        return serviceRepository.save(s);
    }



    /**
	 * Returns the service information for the service with the given
	 * name.
	 *
	 * Throws a PLMSException if there is no service with the
	 * given name.
	 *
	 * @param serviceName
	 *            Name of an existing service (primary key)
	 * @return service information for the service
	 */
    @Transactional
    public Service getServiceByServiceName(String serviceName){
        Service service = serviceRepository.findServiceByServiceName(serviceName);

        if(service == null){
            throw new PLMSException(HttpStatus.NOT_FOUND, "Service with name " + serviceName + " does not exists.");
        }
        return service;
    }

    /**
	 * Deletes the service with the given serviceName.
	 *
	 * Throws a PLMSException if there is no service with the
	 * given name.
	 *
	 * @param serviceName
	 *            Name of an existing service (primary key)
	 */
    @Transactional
    public void deleteServiceByServiceName(String serviceName){
        Service serviceToDelete = serviceRepository.findServiceByServiceName(serviceName);

        if(serviceToDelete == null){
            throw new PLMSException(HttpStatus.NOT_FOUND, "Service with name " + serviceName + " does not exists.");
        }

        serviceRepository.deleteById(serviceName);
    }

    /**
	 * Returns the service information for all services.
	 *
	 * @return List of all services
	 */
    @Transactional
    public Iterable<Service> getAllServices(){
        ArrayList<Service> arrayList = (ArrayList<Service>) serviceRepository.findAll();
        if (arrayList.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no services in the system");
        return serviceRepository.findAll();
    }

}
