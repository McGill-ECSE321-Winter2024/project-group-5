package ca.mcgill.ecse321.SportPlus.Service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.ClassType;
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

}
