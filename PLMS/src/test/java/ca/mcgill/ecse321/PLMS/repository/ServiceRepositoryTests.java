package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.PLMS.model.Service;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceRepositoryTests {
    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        serviceRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadService(){
        //=-=-=-=-=-=- Create Service object -=-=-=-=-=-=//

        //Normal parameters
        String serviceName = "30 min Car Wash";
        int serviceCost = 30;
        double serviceLengthInHours = 0.5;
        Service service = new Service();
        //--------------------------------//

        //Set all parameters
        service.setServiceName(serviceName);
        service.setCost(serviceCost);
        service.setLengthInHours(serviceLengthInHours);

        //Save service
        service = serviceRepository.save(service);
        String serviceId = service.getServiceName();
        //--------------------------------//


        //=-=-=-=-=-=- Read service appointement -=-=-=-=-=-=//
        service = serviceRepository.findServiceByServiceName(serviceId);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(service);
        assertEquals(serviceName, service.getServiceName());
        assertEquals(serviceCost, service.getCost());
        assertEquals(serviceLengthInHours, service.getLengthInHours());
    }
}