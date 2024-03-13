package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.OwnerService;

@ExtendWith(MockitoExtension.class)
public class TestOwnerService {

    @Mock
    private OwnerRepository ownerRepository;

    @InjectMocks
    private OwnerService ownerService;

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 2;

    @BeforeEach
    public void setMockOutput() {
        lenient().when(ownerRepository.findByEmail(OWNER_EMAIL)).thenReturn(new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID));
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};
		lenient().when(ownerRepository.save(any(Owner.class))).thenAnswer(returnParameterAsAnswer);
    }
    
    @Test
	public void testCreateOwner() {
        String email = "owner@sportplus.com";
        String firstName = "Paul";
        String lastName = "Dmyt";
        String password = "Ro1234";
        
        Owner owner = ownerService.createOwner(firstName, password, lastName);
		
		assertNotNull(owner);
		assertEquals(email, owner.getEmail());
        assertEquals(firstName, owner.getFirstName());
        assertEquals(lastName, owner.getLastName());
        assertEquals(password, owner.getPassword());
        
        verify(ownerRepository, times(1)).save(owner);
	}

    @Test
    public void testUpdateOwnerAndCreateWithoutInit() {
        String newPassword = "NewPass123";
        String newFirstName = "NewJohn";
        String newLastName = "NewDoe";

        Owner owner = ownerService.createOwner();
        
        Owner updatedOwner = ownerRepository.findByEmail(OWNER_EMAIL);
        
        ownerService.updateOwnerFirstName(newFirstName);
        ownerService.updateOwnerLastName(newLastName);
        ownerService.updateOwnerPassword(newPassword);

        assertNotNull(updatedOwner);
        assertEquals(owner.getEmail(), updatedOwner.getEmail());
        assertNotNull(updatedOwner.getAccountId());
        assertNotEquals(owner.getFirstName(), updatedOwner.getFirstName());
        assertNotEquals(owner.getLastName(), updatedOwner.getLastName());
        assertNotEquals(owner.getPassword(), updatedOwner.getPassword());
        assertEquals(newPassword, updatedOwner.getPassword());
        assertEquals(newFirstName, updatedOwner.getFirstName());
        assertEquals(newLastName, updatedOwner.getLastName());
        
        verify(ownerRepository, times(4)).save(any(Owner.class));
    }

    @Test
	public void testGetExistingOwner() {
		assertEquals(OWNER_EMAIL, ownerService.getOwner().getEmail());
	}

}
