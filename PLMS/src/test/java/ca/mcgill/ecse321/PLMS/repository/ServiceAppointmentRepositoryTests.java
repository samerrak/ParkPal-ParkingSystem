package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.Service;
import ca.mcgill.ecse321.PLMS.model.ServiceAppointment;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testing suite for the persistence of the ServiceAppointment class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class ServiceAppointmentRepositoryTests {
    @Autowired
    private ServiceAppointmentRepository serviceAppointmentRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;
    @Autowired
    private ServiceRepository serviceRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        serviceAppointmentRepository.deleteAll();

        employeeRepository.deleteAll();
        monthlyCustomerRepository.deleteAll();
        serviceRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadServiceAppointment(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        //normal parameters
        LocalDate date = LocalDate.of(2023,02,21);
        Time starTime = Time.valueOf("12:00:00");
        Time endTime = Time.valueOf("18:00:00");

        //To create the object we need to first create an employee, monthly customer and service
        //----------  Employee  ----------//
        String employeeEmail = "jeff.jeff@jeff.com";
        String employeePassword = "PasswordSuperSecured12345";
        String employeeName = "Jeff";
        String employeeJobDescription = "Porter or something like that, im not sure how to describe that job but this is a job description";
        int employeeHourlyWage = 15;
        Employee employeeJeff = new Employee();
        //Set all parameters
        employeeJeff.setEmail(employeeEmail);
        employeeJeff.setPassword(employeePassword);
        employeeJeff.setName(employeeName);
        employeeJeff.setJobTitle(employeeJobDescription);
        employeeJeff.setHourlyWage(employeeHourlyWage);
        //Save employee
        employeeRepository.save(employeeJeff);
        //--------------------------------//

        //------  Monthly Customer  ------//
        String mCustomerEmail = "example@email.com";
        String mCustomerPassword = "PassWord123!";
        String mCustomerName = "Kirby";
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        //Set all parameters
        monthlyCustomer.setEmail(mCustomerEmail);
        monthlyCustomer.setPassword(mCustomerPassword);
        monthlyCustomer.setName(mCustomerName);
        //Save monthly customer
        monthlyCustomerRepository.save(monthlyCustomer);
        //--------------------------------//

        //----------  Service  -----------//
        String serviceDesciption = "Car cleaning: we will clean your car and it will look very shiny";
        int seviceCost = 30;
        double serviceLengthInHours = 0.5;
        Service service = new Service();
        //Set all parameters
        service.setServiceName(serviceDesciption);
        service.setCost(seviceCost);
        service.setLengthInHours(serviceLengthInHours);
        //Save service
        service = serviceRepository.save(service);
        String serviceId = service.getServiceName();
        //--------------------------------//

        ServiceAppointment serviceAppointment = new ServiceAppointment();
        
        //Set all parameters
        serviceAppointment.setDate(date);
        serviceAppointment.setStartTime(starTime);
        serviceAppointment.setEndTime(endTime);
        serviceAppointment.setEmployee(employeeJeff);
        serviceAppointment.setCustomer(monthlyCustomer);
        serviceAppointment.setService(service);

        //=-=-=-=-=-=- Save service appointement -=-=-=-=-=-=//
        serviceAppointment = serviceAppointmentRepository.save(serviceAppointment);
        int id = serviceAppointment.getId();

        //=-=-=-=-=-=- Read service appointement -=-=-=-=-=-=//
        serviceAppointment = serviceAppointmentRepository.findServiceAppointmentById(id);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(serviceAppointment);
        assertEquals(date, serviceAppointment.getDate());
        assertEquals(starTime, serviceAppointment.getStartTime());
        assertEquals(endTime, serviceAppointment.getEndTime());

        assertEquals(employeeEmail, serviceAppointment.getEmployee().getEmail());
        assertEquals(mCustomerEmail, serviceAppointment.getCustomer().getEmail());
        assertEquals(serviceId, serviceAppointment.getService().getServiceName());
    }
}
