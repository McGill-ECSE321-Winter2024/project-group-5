package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.service.ClassTypeService;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TestClassTypeService {

    @Mock
    private ClassTypeRepository classTypeRepository;

    @InjectMocks
    private ClassTypeService classTypeService;

    private static final int CLASS_TYPE_ID = 0;
    private static final String CLASS_TYPE_NAME = "Yoga";
    private static final String CLASS_TYPE_DESCRIPTION = "Description";

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 2;

    @SuppressWarnings("null")
    // Sets up common mock responses before each test method is executed.
    @BeforeEach
    public void setMockOutput() {
        // Preparing a mock Owner and ClassType object for the mock repository methods.
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType classType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, false, owner);

        // Configuring the mock repository to return predefined objects for certain method calls.
        Mockito.lenient().when(classTypeRepository.findByName(CLASS_TYPE_NAME)).thenReturn(classType);
        Mockito.lenient().when(classTypeRepository.findByTypeId(CLASS_TYPE_ID)).thenReturn(classType);
        Mockito.lenient().when(classTypeRepository.findAll()).thenReturn(Arrays.asList(classType));
        Mockito.lenient().when(classTypeRepository.findByApproved(false)).thenReturn(Arrays.asList(classType));
        Mockito.lenient().when(classTypeRepository.save(any(ClassType.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    // Tests the service's ability to find a ClassType by its name.
    @Test
    public void testFindByName() {
        String name = CLASS_TYPE_NAME;
        ClassType foundClassType = classTypeService.findByName(name);
        assertNotNull(foundClassType); // Verifies that the result is not null.
        assertEquals(name, foundClassType.getName()); // Checks if the returned ClassType has the expected name.
    }

    // Tests the service's ability to find a ClassType by its type ID.
    @Test
    public void testFindByTypeId() {
        int typeId = CLASS_TYPE_ID;
        ClassType foundClassType = classTypeService.findByTypeId(typeId);
        assertNotNull(foundClassType); // Verifies that the result is not null.
        assertEquals(CLASS_TYPE_NAME, foundClassType.getName()); // Checks if the returned ClassType has the expected name.
        assertEquals(typeId, foundClassType.getTypeId()); // Verifies the ClassType has the correct ID.
    }

    // Tests retrieving all ClassTypes from the service.
    @Test
    public void testFindAll() {
        List<ClassType> classTypes = classTypeService.getAllClassTypes();
        assertNotNull(classTypes); // Ensures the returned list is not null.
        assertEquals(1, classTypes.size()); // Assumes the setup includes only one ClassType for testing.
    }

    // Tests creating a ClassType via instructor role.
    @SuppressWarnings("null")
    @Test
    public void testCreateInstructorClassType() {
        ClassType newClassType = classTypeService.instructorCreate("Pilates", "A class for core strength", null);
        assertNotNull(newClassType); // Verifies the ClassType creation was successful.
        assertEquals("Pilates", newClassType.getName()); // Checks if the created ClassType has the correct name.
        verify(classTypeRepository, times(1)).save(any(ClassType.class)); // Ensures the save method was called once.
    }

    // Tests finding ClassTypes based on approval status.
    @Test
    public void testFindByApproval() {
        boolean approved = false;
        List<ClassType> foundClassTypes = classTypeService.findByApproval(approved);
        assertNotNull(foundClassTypes); // Ensures the list is not null.
        assertEquals(1, foundClassTypes.size()); // Checks if one ClassType is returned as set up.
        assertEquals(approved, foundClassTypes.get(0).getApproved()); // Verifies the approval status matches.
    }

    // Tests the deletion of a ClassType by its name.
    @Test
    public void testDeleteByName() {
        classTypeService.deleteByName(CLASS_TYPE_NAME);
        verify(classTypeRepository, times(1)).deleteByName(CLASS_TYPE_NAME); // Checks if the delete operation was called once
    }

    // Test case for verifying the functionality of creating a ClassType by an owner.
@SuppressWarnings("null")
@Test
public void testOwnerCreate() {
    // Setup: Create a mock owner instance with predefined values.
    Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
    
    // Action: Invoke the ownerCreate method to create a new ClassType with given attributes and the mock owner.
    ClassType createdClassType = classTypeService.ownerCreate("Spinning", "High intensity bike class", owner);
    
    // Assertion: Verify that the created ClassType is not null, ensuring it was created successfully.
    assertNotNull(createdClassType);
    
    // Assertion: Check that the name of the created ClassType matches the expected name, verifying correct data handling.
    assertEquals("Spinning", createdClassType.getName());
    
    // Assertion: Ensure the created ClassType is marked as approved, as expected when created by an owner.
    assertTrue(createdClassType.getApproved());
    
    // Verification: Confirm that the save method on the repository was called exactly once, ensuring the ClassType was persisted.
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}

// Test case for the approval process of a ClassType.
@Test
public void testApprove() {
    // Setup: Define a class type ID for testing.
    int typeId = CLASS_TYPE_ID;
    
    // Action: Call the approve method on the service with the test type ID to approve the ClassType.
    classTypeService.approve(typeId);
    
    // Action & Assertion: Retrieve the ClassType by its ID and verify it is not null and indeed marked as approved.
    ClassType approvedClassType = classTypeService.findByTypeId(typeId);
    assertNotNull(approvedClassType); // Check the object is not null, indicating it was found.
    assertTrue(approvedClassType.getApproved()); // Check that the class type is marked as approved, as expected after the operation.
}

// Test case for updating the description of an existing ClassType.
@SuppressWarnings("null")
@Test
public void testUpdateDescription() {
    // Setup: Define a new description for the ClassType.
    String newDescription = "Updated Description";
    
    // Action: Update the description of a ClassType identified by CLASS_TYPE_ID with the new description.
    ClassType updatedClassType = classTypeService.updateDescription(CLASS_TYPE_ID, newDescription);
    
    // Assertion: Verify the returned ClassType object is not null, indicating the operation was successful.
    assertNotNull(updatedClassType);
    
    // Assertion: Confirm that the description of the ClassType matches the new description provided.
    assertEquals(newDescription, updatedClassType.getDescription());
    
    // Verification: Ensure the save method on the repository was invoked once, confirming changes were persisted.
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}

// Test case for updating the name of an existing ClassType.
@SuppressWarnings("null")
@Test
public void testUpdateName() {
    // Setup: Define a new name for the ClassType.
    String newName = "Updated Name";
    
    // Action: Update the name of a ClassType identified by CLASS_TYPE_ID with the new name.
    ClassType updatedClassType = classTypeService.updateName(CLASS_TYPE_ID, newName);
    
    // Assertion: Verify the returned ClassType object is not null, indicating the operation was successful.
    assertNotNull(updatedClassType);
    
    // Assertion: Confirm that the name of the ClassType matches the new name provided.
    assertEquals(newName, updatedClassType.getName());
    
    // Verification: Ensure the save method on the repository was invoked once, confirming changes were persisted.
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}
}