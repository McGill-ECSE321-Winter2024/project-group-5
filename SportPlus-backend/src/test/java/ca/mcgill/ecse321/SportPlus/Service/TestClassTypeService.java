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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TestClassTypeService {

    @Mock
    private ClassTypeRepository classTypeRepository;

    @InjectMocks
    private ClassTypeService classTypeService;

    private static final int CLASS_TYPE_ID = 1;
    private static final String CLASS_TYPE_NAME = "Yoga";
    private static final String CLASS_TYPE_DESCRIPTION = "Description";

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "password123";
    private static final int OWNER_ACCOUNTID = 2;
    
    @SuppressWarnings("null")
    @BeforeEach
    public void setMockOutput() {
        Owner owner=new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
        ClassType classType = new ClassType(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION, CLASS_TYPE_ID, false, owner);
        Mockito.lenient().when(classTypeRepository.findByName(CLASS_TYPE_NAME)).thenReturn(classType);
        Mockito.lenient().when(classTypeRepository.findByTypeId(CLASS_TYPE_ID)).thenReturn(classType);
        Mockito.lenient().when(classTypeRepository.findAll()).thenReturn(Arrays.asList(classType));
        Mockito.lenient().when(classTypeRepository.save(any(ClassType.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    @Test
    public void testFindByName() {
        String name = CLASS_TYPE_NAME;
        ClassType foundClassType = classTypeService.findByName(name);
        assertNotNull(foundClassType);
        assertEquals(name, foundClassType.getName());
    }

    @Test
    public void testFindByTypeId() {
        int typeId = CLASS_TYPE_ID;
        ClassType foundClassType = classTypeService.findByTypeId(typeId);
        assertNotNull(foundClassType);
        assertEquals(typeId, foundClassType.getTypeId());
    }

    @Test
    public void testFindAll() {
        List<ClassType> classTypes = classTypeService.getAllClassTypes();
        assertNotNull(classTypes);
        assertEquals(1, classTypes.size());
    }

    @SuppressWarnings("null")
    @Test
    public void testCreateInstructorClassType() {
        ClassType newClassType = classTypeService.instructorCreate("Pilates", "A class for core strength", null); // Replace null with actual logic if needed
        assertNotNull(newClassType);
        assertEquals("Pilates", newClassType.getName());
        verify(classTypeRepository, times(1)).save(any(ClassType.class));
    }

@Test
public void testFindByApproval() {
    boolean approved = false; // Assuming false to match your setup
    List<ClassType> foundClassTypes = classTypeService.findByApproval(approved);
    assertNotNull(foundClassTypes);
    assertEquals(1, foundClassTypes.size()); // Assuming the setup only includes one classType
    assertEquals(approved, foundClassTypes.get(0).getApproved());
}

@Test
public void testDeleteByName() {
    classTypeService.deleteByName(CLASS_TYPE_NAME);
    verify(classTypeRepository, times(1)).deleteByName(CLASS_TYPE_NAME);
}

@SuppressWarnings("null")
@Test
public void testOwnerCreate() {
    Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_ACCOUNTID);
    ClassType createdClassType = classTypeService.ownerCreate("Spinning", "High intensity bike class", owner);
    assertNotNull(createdClassType);
    assertEquals("Spinning", createdClassType.getName());
    assertTrue(createdClassType.getApproved()); // Assuming ownerCreate sets approved to true
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}

@Test
public void testApprove() {
    int typeId = CLASS_TYPE_ID;
    classTypeService.approve(typeId);
    ClassType approvedClassType = classTypeService.findByTypeId(typeId);
    assertNotNull(approvedClassType);
    assertTrue(approvedClassType.getApproved());
}

@SuppressWarnings("null")
@Test
public void testUpdateDescription() {
    String newDescription = "Updated Description";
    ClassType updatedClassType = classTypeService.updateDescription(CLASS_TYPE_ID, newDescription);
    assertNotNull(updatedClassType);
    assertEquals(newDescription, updatedClassType.getDescription());
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}

@SuppressWarnings("null")
@Test
public void testUpdateName() {
    String newName = "Updated Name";
    ClassType updatedClassType = classTypeService.updateName(CLASS_TYPE_ID, newName);
    assertNotNull(updatedClassType);
    assertEquals(newName, updatedClassType.getName());
    verify(classTypeRepository, times(1)).save(any(ClassType.class));
}
}
