package ca.mcgill.ecse321.PLMS.service;

import ca.mcgill.ecse321.PLMS.exception.PLMSException;
import ca.mcgill.ecse321.PLMS.model.Owner;
import ca.mcgill.ecse321.PLMS.repository.EmployeeRepository;
import ca.mcgill.ecse321.PLMS.repository.MonthlyCustomerRepository;
import ca.mcgill.ecse321.PLMS.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OwnerServiceTests {

    @Mock
    private OwnerRepository ownerRepository;
    @Mock
    private MonthlyCustomerRepository monthlyCustomerRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private OwnerService ownerService;


    @Test
    /**
     * Get all owners of the parking lot
     */
    public void testGetAllOwners() {
        final String email = "john.doe@mcgill.ca";
        final String password = "JohnDoe2002";
        final String name = "John Doe";
        final Owner john = new Owner(email, password, name);

        final String email1 = "jane.doe@mcgill.ca";
        final String password1 = "JaneDoe2002";
        final String name1 = "Jane Doe";
        final Owner jane = new Owner(email1, password1, name1);

        ArrayList<Owner> customers = new ArrayList<>();
        customers.add(john);
        customers.add(jane);


        when(ownerRepository.findAll()).thenReturn(customers);
        Iterable<Owner> output = ownerService.getAllOwners();
        Iterator<Owner> i = output.iterator();
        assertEquals(i.next(), john);
        assertEquals(i.next(), jane);
    }

    @Test
    /**
     * No owners in db
     */
    public void testGetAllEmptyOwners() {
        ArrayList<Owner> customers = new ArrayList<>();
        when(ownerRepository.findAll()).thenReturn(customers);
        PLMSException e = assertThrows(PLMSException.class, () -> ownerService.getAllOwners());
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(),"There are no owners in the system" );
    }


    @Test
    /**
     * Get the owner by their email
     */
    public void testGetOwnerByValidEmail()
    {
        final String email = "john.doe@mcgill.ca";
        final String password = "JohnDoe2002";
        final String name = "John Doe";
        final Owner john = new Owner(email, password, name);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(john);

        Owner output = ownerService.getOwnerByEmail(email);

        assertEquals(output, john);

    }

    @Test
    /**
     * Get an owner that doesn't exist
     */
    public void testGetOwnerByInvalidEmail()
    {
        final String email = "jane.doe@mcgill.ca";
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        PLMSException e = assertThrows(PLMSException.class, () -> ownerService.getOwnerByEmail(email));
        assertEquals(e.getMessage(), "Owner not found."); //
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    /**
     * Create a valid owner
     */
    public void testCreateValidOwnerAccount()
    {
        final String email = "john.doe@mcgill.ca";
        final String password = "JohnDoe2002";
        final String name = "John Doe";
        final Owner john = new Owner(email, password, name);
        when(ownerRepository.save(john)).thenReturn(john);

        Owner output = ownerService.createOwnerAccount(john);

        assertNotNull(output);
        assertEquals(john, output);
        verify(ownerRepository, times(1)).save(john);

    }

    @Test
    /**
     * Owner account has been registered before
     */
    public void testCreateInvalidOwnerAccount()
    {
        final String email = "john.doe@mcgill.ca";
        final String password = "JohnDoe2002";
        final String name = "John Doe";
        final Owner john = new Owner(email, password, name);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(john);

        final String password2 = "JaneDoe2002";
        final String name2 = "Jane Doe";
        final Owner jane = new Owner(email, password2, name2);
        PLMSException e = assertThrows(PLMSException.class, () -> ownerService.createOwnerAccount(jane));
        assertEquals(e.getStatus(), HttpStatus.CONFLICT);
        assertEquals(e.getMessage(), "Another account with this email already exists");


    }

    @Test
    /**
     * Update owner that doesn't exist
     */
    public void testInvalidUpdateOwnerAccount()
    {
        final String email = "john.doe@mcgill.ca";
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(null);

        final String password2 = "JaneDoe2002";
        final String name2 = "Jane Doe";
        final Owner jane = new Owner(email, password2, name2);
        PLMSException e = assertThrows(PLMSException.class, () -> ownerService.updateOwnerAccount(jane));
        assertEquals(e.getStatus(), HttpStatus.NOT_FOUND);
        assertEquals(e.getMessage(), "Owner not found.");
    }

    @Test
    /**
     * Valid update of owner account
     */
    public void testValidUpdateOwnerAccount()
    {
        final String email = "john.doe@mcgill.ca";
        final String password = "JohnDoe2002";
        final String name = "John Doe";
        final Owner john = new Owner(email, password, name);
        when(ownerRepository.findOwnerByEmail(email)).thenReturn(john);

        final String password2 = "JaneDoe2002";
        final String name2 = "Jane Doe";
        final Owner jane = new Owner(email, password2, name2);

        when(ownerRepository.save(john)).thenReturn(jane);
        Owner output = ownerService.updateOwnerAccount(jane);

        assertEquals(output, jane);

    }
}

