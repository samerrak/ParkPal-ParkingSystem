package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Employee;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * Service class for all the business methods related to the employee model class in the PLMS system
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository; // Repository from where the employee objects are persisted
    @Autowired
    MonthlyCustomerRepository monthlyCustomerRepository;
    @Autowired
    OwnerRepository ownerRepository;


    /**
     * Service method to fetch all existing employees in the database
     * @throws PLMSException - if no employees exist in the system
     */
    @Transactional
    public Iterable<Employee> getAllEmployees(){
        ArrayList<Employee> arrayList = (ArrayList<Employee>) employeeRepository.findAll();
        if (arrayList.isEmpty())
            throw new PLMSException(HttpStatus.NOT_FOUND, "There are no employees in the system");
        return employeeRepository.findAll(); }

    /**
     * Service method to fetch an existing employee with a specific email from the database
     * @param email email of the employee
     * @throws PLMSException - If employee does not exist
     */
    @Transactional
    public Employee getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findEmployeeByEmail(email);
        if (employee == null) {
            throw new PLMSException(HttpStatus.NOT_FOUND, "Employee not found.");
        }
        return employee;
    }

    @Transactional
    public Employee getEmployeeByEmailAndPassword(String email, String password) {
        Employee employee = getEmployeeByEmail(email);
        if (employee.getPassword().equals(password))
            return employee;
        else
            throw new PLMSException(HttpStatus.NOT_FOUND, "Please enter the correct password");
    }

    /**
     * Service method that updates the employee's information in the database
     * @param employee updated instance of the employee to be persisted
     * @throws PLMSException - If employee does not exist
     */
    @Transactional
    public Employee updateEmployee(Employee employee)
    {
        Employee e = getEmployeeByEmail(employee.getEmail());
        if(employee.getHourlyWage() <= 0){
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Hourly wage must be positive.");
        }
        e.setPassword(employee.getPassword());
        e.setHourlyWage(employee.getHourlyWage());
        e.setJobTitle(employee.getJobTitle());
        e.setName(employee.getName());
        return employeeRepository.save(e);
    }

    /**
     * Service method to store a created employee in the database
     * @param employee instance of the employee to be persisted
     * @throws PLMSException - If an employee already exists
     */
    @Transactional
    public Employee createEmployeeAccount(Employee employee) {
        if(employee.getHourlyWage() <= 0)
            throw new PLMSException(HttpStatus.BAD_REQUEST, "Hourly wage must be positive.");
        if ((ownerRepository.findOwnerByEmail(employee.getEmail()) == null) && (employeeRepository.findEmployeeByEmail(employee.getEmail()) == null) && (monthlyCustomerRepository.findMonthlyCustomerByEmail(employee.getEmail()) == null))
            return employeeRepository.save(employee);
        else
            throw new PLMSException(HttpStatus.CONFLICT, "Another account with this email already exists");
    }

    /**
     * Service method to delete an employee from the database
     * @param email email of the employee to be deleted from persistence layer
     */
    @Transactional
    public void deleteEmployeeAccount(String email) {
        employeeRepository.delete(getEmployeeByEmail(email));
    }
}
