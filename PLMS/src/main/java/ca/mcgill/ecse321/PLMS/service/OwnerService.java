package ca.mcgill.ecse321.PLMS.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Owner;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;
import java.util.Iterator;

/**
 * Service class for all the business methods related to the owner account model class in the PLMS system
 */
@Service
public class OwnerService {

    @Autowired
    OwnerRepository ownerRepository; // Repository from where the owner account objects are persisted
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    MonthlyCustomerRepository monthlyCustomerRepository;

    /**
     * Service method to fetch all existing owners in the database
     * @return all the owners from persistence layer
     * @throws PLMSException - if no owners exist in the system
     */
    @Transactional
    public Iterable<Owner> getAllOwners() {
        Iterable<Owner> owners = ownerRepository.findAll();
        Iterator<Owner> iterator = owners.iterator();
        if (!iterator.hasNext())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no owners in the system");
        return ownerRepository.findAll(); }

    /**
     * Service method to fetch an existing owner with a specific email from the database
     * @param email owner's email linked to their account
     * @return the owner corresponding to the provided email
     * @throws PLMSException - If the owner does not exist
     */
    @Transactional
    public Owner getOwnerByEmail(String email) {
        Owner owner = ownerRepository.findOwnerByEmail(email);
        if (owner == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "Owner not found.");
        }
        return owner;
    }

    @Transactional
    public Owner getOwnerByEmailAndPassword(String email, String password) {
        Owner owner = getOwnerByEmail(email);
        if (owner.getPassword().equals(password))
            return owner;
        else
            throw new PLMSException(HttpStatus.NOT_FOUND, "Please enter the correct password");
    }

    /**
     * Service method that updates the owner's information in the database
     * @param owner updated instance of the owner
     * @return the updated instance
     * @throws PLMSException - If owner does not exist
     */
    @Transactional
    public Owner updateOwnerAccount(Owner owner)
    {
        Owner o = getOwnerByEmail(owner.getEmail());
        o.setPassword(owner.getPassword());
        o.setName(owner.getName());
        return ownerRepository.save(o);

    }

    /**
     * Service method to store a created owner in the database
     * @param owner instance to be persisted
     * @return the persisted instance if successful
     * @throws PLMSException - If an owner already exists
     */
    @Transactional
	public Owner createOwnerAccount(Owner owner) {
        // Register the owner account into database
        if ((ownerRepository.findOwnerByEmail(owner.getEmail()) == null) && (employeeRepository.findEmployeeByEmail(owner.getEmail()) == null) && (monthlyCustomerRepository.findMonthlyCustomerByEmail(owner.getEmail()) == null))
		    return ownerRepository.save(owner);
        else
            throw new PLMSException(HttpStatus.CONFLICT, "Another account with this email already exists");

    }


}
