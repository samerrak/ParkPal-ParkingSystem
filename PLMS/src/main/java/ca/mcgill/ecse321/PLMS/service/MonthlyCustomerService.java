package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Service class for all the business methods related to the monthly customer model class in the PLMS system
 */
@Service
public class MonthlyCustomerService {


    @Autowired
    MonthlyCustomerRepository monthlyCustomerRepository; // Repository from where the monthly customer objects are persisted
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    OwnerRepository ownerRepository;

    /**
     * Service method to fetch all existing monthly customers in the database
     * @return an arraylist of the monthly customers present in the database
     * @throws PLMSException - if no monthly customers exist in the system
     */
    @Transactional
    public Iterable<MonthlyCustomer> getAllMonthlyCustomers() {
        ArrayList<MonthlyCustomer> arrayList = (ArrayList<MonthlyCustomer>) monthlyCustomerRepository.findAll();
        if (arrayList.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no monthly customers in the system");
        return monthlyCustomerRepository.findAll(); }

    /**
     * Service method to fetch an existing monthly customer with a specific email from the database
     * @param email email of the monthly customer we want to fetch
     * @return the monthly customer requested
     * @throws PLMSException - If the monthly customer does not exist
     */
    @Transactional
    public MonthlyCustomer getMonthlyCustomerByEmail(String email) {
        MonthlyCustomer monthlyCustomer = monthlyCustomerRepository.findMonthlyCustomerByEmail(email);
        if (monthlyCustomer == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "Monthly customer not found.");
        }
        return monthlyCustomer;
    }

    @Transactional
    public MonthlyCustomer getMonthlyCustomerByEmailAndPassword(String email, String password) {
        MonthlyCustomer monthlyCustomer = getMonthlyCustomerByEmail(email);
        if (monthlyCustomer.getPassword().equals(password))
            return monthlyCustomer;
        else
            throw new PLMSException(HttpStatus.NOT_FOUND, "Please enter the correct password");
    }
    /**
     * Service method that updates the monthly customer's information in the database
     * @param monthlyCustomer updated instance to persist
     * @return updated instance
     * @throws PLMSException - If monthly customer does not exist
     */
    @Transactional
    public MonthlyCustomer updateMonthlyCustomer(MonthlyCustomer monthlyCustomer)
    {
        MonthlyCustomer customer = getMonthlyCustomerByEmail(monthlyCustomer.getEmail());
        customer.setName(monthlyCustomer.getName());
        customer.setPassword(monthlyCustomer.getPassword());
        return monthlyCustomerRepository.save(customer);
    }

    /**
     * Service method to store a created monthly customer in the database
     * @param monthlyCustomer instance to be persisted
     * @return the newly persisted instance
     * @throws PLMSException - If a monthly customer already exists
     */
    @Transactional
    public MonthlyCustomer createMonthlyCustomerAccount(MonthlyCustomer monthlyCustomer) {
        // Create the account
        if ((ownerRepository.findOwnerByEmail(monthlyCustomer.getEmail()) == null) && (employeeRepository.findEmployeeByEmail(monthlyCustomer.getEmail()) == null) && (monthlyCustomerRepository.findMonthlyCustomerByEmail(monthlyCustomer.getEmail()) == null))
            return monthlyCustomerRepository.save(monthlyCustomer);
        else
            throw new PLMSException(HttpStatus.CONFLICT, "Another account with this email already exists");

    }

}
