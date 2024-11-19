package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.PLMS.model.MonthlyCustomer;
import ca.mcgill.ecse321.PLMS.model.MonthlyPass;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Testing suite for the persistence of the MonthlyPass class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class MonthlyPassRepositoryTests {
    
    @Autowired
    private MonthlyPassRepository monthlyPassRepository;
    @Autowired
    private MonthlyCustomerRepository monthlyCustomerRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        monthlyPassRepository.deleteAll();
        monthlyCustomerRepository.deleteAll();
    }    

    @Test
    public void testPersistAndLoadMonthlyPass(){
        //=-=-=-=-=-=- Create MonthlyPass -=-=-=-=-=-=//
        LocalDate startDate = Date.valueOf("2023-02-21").toLocalDate();
        LocalDate endDate = Date.valueOf("2023-04-21").toLocalDate();

        MonthlyPass monthlyPass = new MonthlyPass();

        //=-=-=-=-=-=- Create MonthlyCustomer -=-=-=-=-=-=//
        String email = "example@email.com";
        
        MonthlyCustomer monthlyCustomer = new MonthlyCustomer();
        
        //Set all parameters
        monthlyCustomer.setEmail(email);

        monthlyPass.setStartDate(startDate);
        monthlyPass.setEndDate(endDate);
        monthlyPass.setCustomer(monthlyCustomer);

        //=-=-=-=-=-=- Save object -=-=-=-=-=-=//
        monthlyCustomer = monthlyCustomerRepository.save(monthlyCustomer);
        String emailInDatabase = monthlyCustomer.getEmail();

        monthlyPass = monthlyPassRepository.save(monthlyPass);
        int id = monthlyPass.getId();

        //=-=-=-=-=-=- Read object -=-=-=-=-=-=//
        monthlyPass = monthlyPassRepository.findMonthlyPassById(id);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(monthlyPass);
        assertEquals(startDate, monthlyPass.getStartDate());
        assertEquals(endDate, monthlyPass.getEndDate());
        assertEquals(emailInDatabase, monthlyPass.getCustomer().getEmail());
    }
}
