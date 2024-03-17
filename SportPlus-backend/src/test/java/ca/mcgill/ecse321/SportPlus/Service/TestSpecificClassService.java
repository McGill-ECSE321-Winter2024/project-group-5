package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.RegistrationRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
import ca.mcgill.ecse321.SportPlus.model.Registration;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.service.SpecificClassService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.sql.Time;

@ExtendWith(MockitoExtension.class)
public class TestSpecificClassService {

    @Mock
    private SpecificClassRepository specificClassRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Mock
    private ClassTypeRepository classTypeRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private SpecificClassService specificClassService;

    @Test
    public void testCreateSpecificClass() {
        // Arrange
        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Add one day
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Convert to java.sql.Date
        Date tomorrow = new Date(calendar.getTimeInMillis());
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("11:00:00");
        int instructorId = 1;
        int classTypeId = 1;

        Instructor mockInstructor = new Instructor();
        mockInstructor.setAccountId(instructorId);
        ClassType mockClassType = new ClassType();
        mockClassType.setTypeId(classTypeId);

        when(instructorRepository.findByAccountId(instructorId)).thenReturn(mockInstructor);
        when(classTypeRepository.findByTypeId(classTypeId)).thenReturn(mockClassType);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        SpecificClass result = specificClassService.createSpecificClass(tomorrow, startTime, endTime, instructorId,
                classTypeId);

        // Assert
        assertNotNull(result);
        assertEquals(tomorrow, result.getDate());
        assertEquals(startTime, result.getStartTime());
        assertEquals(endTime, result.getEndTime());
        assertEquals(mockInstructor, result.getSupervisor());
        assertEquals(mockClassType, result.getClassType());

        // Verify interactions
        verify(specificClassRepository).save(any(SpecificClass.class));
    }

    @Test
    public void testCreateRecurringSpecificClasses() {

        // Arrange
        // A class every Monday from 10 to 11 on April
        Date startDate = Date.valueOf("2024-04-01"); // Let's say April 1st, 2024
        Date endDate = Date.valueOf("2024-04-30"); // April 3oth, 2024
        Time startTime = Time.valueOf("10:00:00");
        Time endTime = Time.valueOf("11:00:00");
        int dayOfWeek = Calendar.MONDAY; // Assuming 1 represents Monday
        int instructorId = 1;
        int classTypeId = 1;

        Instructor mockInstructor = new Instructor();
        ClassType mockClassType = new ClassType();

        when(instructorRepository.findByAccountId(instructorId)).thenReturn(mockInstructor);
        when(classTypeRepository.findByTypeId(classTypeId)).thenReturn(mockClassType);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        List<SpecificClass> recurringClasses = specificClassService.createRecurringSpecificClasses(startDate, endDate,
                startTime, endTime, dayOfWeek, instructorId, classTypeId);

        // Assert
        assertNotNull(recurringClasses);
        assertFalse(recurringClasses.isEmpty());
        // Assuming 5 Mondays in April 2024
        assertEquals(5, recurringClasses.size());

        // Verify that save was called the correct number of times
        verify(specificClassRepository, times(5)).save(any(SpecificClass.class));
    }

    @Test
    public void testUpdateDateSpecificClass() {

        // Set up an existing SpecificClass that will be updated
        Owner owner = new Owner("john@onwer.com", "John", "password", "The Owner", 0);
        ClassType yoga = new ClassType("yoga", "Fun class", 0, true, owner);

        SpecificClass existingClass = new SpecificClass(Date.valueOf("2024-04-10"), Time.valueOf("10:00:00"),
                Time.valueOf("11:00:00"), 1, yoga);

        when(specificClassRepository.findBySessionId(anyInt())).thenReturn(existingClass);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);
        // Arrange
        int sessionId = 1; // The ID of the class to update
        Date newDate = Date.valueOf("2024-04-15"); // The new date

        // Act
        SpecificClass updatedClass = specificClassService.updateDateSpecificClass(sessionId, newDate);

        // Assert
        assertNotNull(updatedClass);
        assertEquals(newDate, updatedClass.getDate());

        // Verify that save was called with an updated class
        verify(specificClassRepository).save(existingClass);
        verify(specificClassRepository).findBySessionId(sessionId);
    }

    @Test
    public void testUpdateTimeSpecificClass() {

        // Set up an existing SpecificClass that will be updated
        Owner owner = new Owner("john@onwer.com", "John", "password", "The Owner", 0);
        ClassType yoga = new ClassType("yoga", "Fun class", 0, true, owner);

        SpecificClass existingClass = new SpecificClass(Date.valueOf("2024-04-10"), Time.valueOf("10:00:00"),
                Time.valueOf("11:00:00"), 1, yoga);

        when(specificClassRepository.findBySessionId(anyInt())).thenReturn(existingClass);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);
        // Arrange
        int sessionId = 1; // The ID of the class to update
        Time newStartTime = Time.valueOf("12:00:00"); // The new time
        Time newEndTime = Time.valueOf("13:00:00"); // The new time

        // Act
        SpecificClass updatedClass = specificClassService.updateTimeSpecificClass(sessionId, newStartTime, newEndTime);

        // Assert
        assertNotNull(updatedClass);
        assertEquals(newStartTime, updatedClass.getStartTime());
        assertEquals(newEndTime, updatedClass.getEndTime());

        // Verify that save was called with an updated class
        verify(specificClassRepository).save(existingClass);
        verify(specificClassRepository).findBySessionId(sessionId);
    }

    @Test
    public void testUpdateClassTypeSpecificClass() {

        ClassType classtype = new ClassType();
        classtype.setTypeId(1);

        SpecificClass existingClass = new SpecificClass();
        existingClass.setSessionId(1);
        existingClass.setDate(Date.valueOf("2024-04-15"));
        existingClass.setStartTime(Time.valueOf("10:00:00"));
        existingClass.setEndTime(Time.valueOf("11:00:00"));
        existingClass.setClassType(classtype);
        // Save the existing ClassType
        when(classTypeRepository.findByTypeId(anyInt())).thenReturn(classtype);

        // Setup a new ClassType to be assigned to the SpecificClass
        ClassType newClassType = new ClassType();
        newClassType.setTypeId(2); // Assume a different ClassType ID for the update

        // Mocking repository methods
        when(specificClassRepository.findBySessionId(anyInt())).thenReturn(existingClass);
        when(classTypeRepository.findByTypeId(anyInt())).thenReturn(newClassType);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        SpecificClass updatedClass = specificClassService.updateClassTypeSpecificClass(existingClass.getSessionId(),
                newClassType.getTypeId());

        // Assert
        assertNotNull(updatedClass);
        assertEquals(newClassType, updatedClass.getClassType());

        // Verify the repository's save method was called with the updated class
        verify(specificClassRepository).save(existingClass);
        verify(specificClassRepository).findBySessionId(updatedClass.getSessionId());
        verify(classTypeRepository).findByTypeId(updatedClass.getClassType().getTypeId());
    }

    @Test
    public void testAssignInstructorSpecificClass() {

        // Setup an existing SpecificClass
        SpecificClass existingClass = new SpecificClass();
        existingClass.setSessionId(1);
        existingClass.setDate(Date.valueOf("2024-04-15"));
        existingClass.setStartTime(Time.valueOf("10:00:00"));
        existingClass.setEndTime(Time.valueOf("11:00:00"));

        // Setup a mock Instructor to be assigned
        Instructor instructor = new Instructor();
        instructor.setAccountId(1); // Assume an ID for the instructor

        // Mocking repository methods
        when(specificClassRepository.findBySessionId(anyInt())).thenReturn(existingClass);
        when(instructorRepository.findByAccountId(anyInt())).thenReturn(instructor);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        SpecificClass updatedClass = specificClassService.assignInstructorSpecificClass(existingClass.getSessionId(),
                instructor.getAccountId());

        // Assert
        assertNotNull(updatedClass);
        assertEquals(instructor, updatedClass.getSupervisor());

        // Verify the repository's save method was called with the updated class
        verify(specificClassRepository).save(existingClass);
        verify(specificClassRepository).findBySessionId(updatedClass.getSessionId());
        verify(instructorRepository).findByAccountId(updatedClass.getSupervisor().getAccountId());
    }

    @Test
    public void testRemoveInstructorSpecificClass() {
        // Setup an existing SpecificClass with an instructor
        SpecificClass existingClass = new SpecificClass();
        existingClass.setSessionId(1);
        existingClass.setDate(Date.valueOf("2024-04-15"));
        existingClass.setStartTime(Time.valueOf("10:00:00"));
        existingClass.setEndTime(Time.valueOf("11:00:00"));
        existingClass.setSupervisor(new Instructor()); // Assume the class initially has an instructor

        // Mocking repository methods
        when(specificClassRepository.findBySessionId(anyInt())).thenReturn(existingClass);
        when(specificClassRepository.save(any(SpecificClass.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        SpecificClass updatedClass = specificClassService.removeInstructorSpecificClass(existingClass.getSessionId());

        // Assert
        assertNotNull(updatedClass);
        assertNull(updatedClass.getSupervisor()); // The instructor should be removed

        // Verify the repository's save method was called with the updated class
        verify(specificClassRepository).save(existingClass);
        verify(specificClassRepository).findBySessionId(existingClass.getSessionId());

    }

    @Test
    public void testGetByInstructor() {
        // Arrange
        int instructorId = 1;
        Instructor instructor = new Instructor();
        instructor.setAccountId(instructorId);
        List<SpecificClass> expectedClasses = new ArrayList<>();
        expectedClasses.add(new SpecificClass()); // Add mock SpecificClass objects as needed

        when(instructorRepository.findByAccountId(instructorId)).thenReturn(instructor);
        when(specificClassRepository.findBySupervisor(instructor)).thenReturn(expectedClasses);

        // Act
        List<SpecificClass> actualClasses = specificClassService.getByInstructor(instructorId);

        // Assert
        assertNotNull(actualClasses);
        assertEquals(expectedClasses.size(), actualClasses.size());
        assertEquals(expectedClasses, actualClasses);

        // Verify the interactions
        verify(instructorRepository).findByAccountId(instructorId);
        verify(specificClassRepository).findBySupervisor(instructor);
    }

    @Test
    public void testGetByDate() {
        // Arrange
        Date queryDate = Date.valueOf("2024-04-15");
        List<SpecificClass> expectedClasses = new ArrayList<>();
        expectedClasses.add(new SpecificClass()); // Add mock SpecificClass objects as needed, potentially with more
                                                  // details

        when(specificClassRepository.findByDate(queryDate)).thenReturn(expectedClasses);

        // Act
        List<SpecificClass> actualClasses = specificClassService.getByDate(queryDate);

        // Assert
        assertNotNull(actualClasses);
        assertEquals(expectedClasses.size(), actualClasses.size());
        assertEquals(expectedClasses, actualClasses); // This checks if the two lists contain the same elements in the
                                                      // same order

        // Verify the interaction with the repository
        verify(specificClassRepository).findByDate(queryDate);
    }

    @Test
    public void testGetByClassType() {
        // Arrange
        int classTypeId = 43;
        ClassType classType = new ClassType(); // Assuming ClassType is a valid entity in your model
        classType.setTypeId(classTypeId);
        List<SpecificClass> expectedClasses = new ArrayList<>();
        expectedClasses.add(new SpecificClass()); // Add mock SpecificClass objects as needed

        when(classTypeRepository.findByTypeId(classTypeId)).thenReturn(classType);
        when(specificClassRepository.findByClassType(classType)).thenReturn(expectedClasses);

        // Act
        List<SpecificClass> actualClasses = specificClassService.getByClassType(classTypeId);

        // Assert
        assertNotNull(actualClasses);
        assertEquals(expectedClasses.size(), actualClasses.size());
        assertEquals(expectedClasses, actualClasses); // This checks if the two lists contain the same elements in the
                                                      // same order

        // Verify interactions
        verify(classTypeRepository).findByTypeId(classTypeId);
        verify(specificClassRepository).findByClassType(classType);
    }

    @Test
    public void testGetByDateRange() {
        // Arrange
        Date startDate = Date.valueOf("2024-04-01");
        Date endDate = Date.valueOf("2024-04-30");
        List<SpecificClass> expectedClasses = new ArrayList<>();
        expectedClasses.add(new SpecificClass());  // Add mock SpecificClass objects as needed

        when(specificClassRepository.findClassesBetweenDates(startDate, endDate)).thenReturn(expectedClasses);

        // Act
        List<SpecificClass> actualClasses = specificClassService.getByDateRange(startDate, endDate);

        // Assert
        assertNotNull(actualClasses);
        assertEquals(expectedClasses.size(), actualClasses.size());
        assertEquals(expectedClasses, actualClasses);  // This checks if the two lists contain the same elements in the same order

        // Verify interaction with the repository
        verify(specificClassRepository).findClassesBetweenDates(startDate, endDate);
    }

    @Test
    public void testGetAvailable() {

        Owner owner = new Owner("john@onwer.com", "John", "password", "The Owner", 0);
        ClassType yoga = new ClassType("yoga", "Fun class", 0, true, owner);

        // Arrange
        List<SpecificClass> classesWithSupervisorAndFutureStartTime = new ArrayList<>();
        SpecificClass classMeetingCriteria = new SpecificClass(Date.valueOf("2024-04-15"), Time.valueOf("10:00:00"), Time.valueOf("11:00:00"), 0, yoga);
        classMeetingCriteria.setSupervisor(new Instructor());
        classesWithSupervisorAndFutureStartTime.add(classMeetingCriteria);
    

        List<Registration> fewerThan30Registrations = new ArrayList<>();  // Mimic fewer than 30 registrations
        for (int i = 0; i < 25; i++) {  // Example to add 25 registrations
            fewerThan30Registrations.add(new Registration());
        }

        when(specificClassRepository.findBySupervisorIsNotNullAndStartTimeAfter(any(Date.class))).thenReturn(classesWithSupervisorAndFutureStartTime);
        when(registrationRepository.findBySpecificClass(classMeetingCriteria)).thenReturn(fewerThan30Registrations);
        
        // Act
        List<SpecificClass> availableClasses = specificClassService.getAvailable();
        // Assert
        assertNotNull(availableClasses);
        assertFalse(availableClasses.isEmpty());
        assertTrue(availableClasses.contains(classMeetingCriteria));  // The class that meets the criteria is included in the result

        // Verify interactions
        verify(specificClassRepository).findBySupervisorIsNotNullAndStartTimeAfter(any(Date.class));
        verify(registrationRepository, atLeastOnce()).findBySpecificClass(any(SpecificClass.class));
    }

    @Test
    public void testGeByDateAndStartTime() {
        // Arrange
        Date queryDate = Date.valueOf("2024-04-15");
        Time queryStartTime = Time.valueOf("10:00:00");
        SpecificClass expectedClass = new SpecificClass(); // Set properties as needed
        expectedClass.setDate(queryDate);
        expectedClass.setStartTime(queryStartTime);

        when(specificClassRepository.findByDateAndStartTime(queryDate, queryStartTime)).thenReturn(expectedClass);

        // Act
        SpecificClass actualClass = specificClassService.getByDateAndStartTime(queryDate, queryStartTime);

        // Assert
        assertNotNull(actualClass);
        assertEquals(expectedClass, actualClass); // Verify the returned class is the one we expected

        // Verify interaction with the repository
        verify(specificClassRepository).findByDateAndStartTime(queryDate, queryStartTime);


    }


    @Test
    public void testGetAll() {
        // Arrange
        List<SpecificClass> expectedClasses = new ArrayList<>();
        expectedClasses.add(new SpecificClass());  // Add mock SpecificClass objects as needed
        expectedClasses.add(new SpecificClass());
        expectedClasses.add(new SpecificClass());

        when(specificClassRepository.findAll()).thenReturn(expectedClasses);

        // Act
        List<SpecificClass> actualClasses = specificClassService.getAll();

        // Assert
        assertNotNull(actualClasses);
        assertEquals(expectedClasses.size(), actualClasses.size());
        assertEquals(expectedClasses, actualClasses);  // This checks if the two lists contain the same elements in the same order

        // Verify interaction with the repository
        verify(specificClassRepository).findAll();
    }

    @Test
    public void testDeleteByClassType() {
        // Arrange
        int classTypeId = 1;
        ClassType classType = new ClassType();
        classType.setTypeId(classTypeId);

        when(classTypeRepository.findByTypeId(classTypeId)).thenReturn(classType);

        // Act
        specificClassService.deleteByClassType(classTypeId);

        // Assert
        // Verify that the deleteByClassType method was called on the repository with the correct ClassType
        verify(specificClassRepository).deleteByClassType(classType);
        verify(classTypeRepository).findByTypeId(classTypeId);  // Ensure the class type was fetched correctly
    }

}
