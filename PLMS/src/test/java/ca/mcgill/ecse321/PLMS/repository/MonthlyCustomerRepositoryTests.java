package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testing suite for the persistence of the MonthlyCustomer class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class MonthlyCustomerRepositoryTests {
    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        monthlyCustomerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadMonthlyCustomer(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        String email = "example@email.com"; //BLOO
        String password = "PassWord123!";
        String name = "Kirby";
        
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        
        //Set all parameters
        monthlyCustomer.setEmail(email);
        monthlyCustomer.setPassword(password);
        monthlyCustomer.setName(name);

        //=-=-=-=-=-=- Save object -=-=-=-=-=-=//
        monthlyCustomer = monthlyCustomerRepository.save(monthlyCustomer);
        String emailInDatabase = monthlyCustomer.getEmail();

        //=-=-=-=-=-=- Read object -=-=-=-=-=-=//
        monthlyCustomer = monthlyCustomerRepository.findMonthlyCustomerByEmail(emailInDatabase);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(monthlyCustomer);
        assertEquals(email, monthlyCustomer.getEmail());
        assertEquals(password, monthlyCustomer.getPassword());
        assertEquals(name, monthlyCustomer.getName());
    }
}
