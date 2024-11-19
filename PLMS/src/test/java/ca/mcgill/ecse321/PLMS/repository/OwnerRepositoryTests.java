package ca.mcgill.ecse321.PLMS.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.PLMS.model.Owner;

/**
 * Testing suite for the persistence of the Owner class in the PLMS software system
 * Tests ensure that attributes, references and the instance itself can be read and written
 * in the persistence database
 */
@SpringBootTest
public class OwnerRepositoryTests {
    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDataBase(){
        ownerRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadOwner(){
        //=-=-=-=-=-=- Create object -=-=-=-=-=-=//
        String email = "teta.saniya@teta.com";
        String password = "PasswordSoSuperSecured12345";
        String name = "Saniya";
    
        Owner owner = new Owner();
        //Set all parameters
        owner.setEmail(email);
        owner.setPassword(password);
        owner.setName(name);

        //=-=-=-=-=-=- Save Owner -=-=-=-=-=-=//
        owner = ownerRepository.save(owner);
        String ownerEmail = owner.getEmail();

        //=-=-=-=-=-=- Read owner -=-=-=-=-=-=//
        owner = ownerRepository.findOwnerByEmail(ownerEmail);

        //=-=-=-=-=-=- Asserts -=-=-=-=-=-=//
        assertNotNull(owner);
        assertEquals(email, owner.getEmail());
        assertEquals(password, owner.getPassword());
        assertEquals(name, owner.getName());
        
}
}
