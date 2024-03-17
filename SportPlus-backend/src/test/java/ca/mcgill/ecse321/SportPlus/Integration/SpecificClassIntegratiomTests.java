package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.same;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.SpecificClassRepository;
import ca.mcgill.ecse321.SportPlus.dto.RecurringSpecificClassRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.RecurringSpecificClassesResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.SpecificClassRequestsDto;
import ca.mcgill.ecse321.SportPlus.dto.SpecificClassResponseDto;
import ca.mcgill.ecse321.SportPlus.dao.InstructorRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.model.Instructor;
import ca.mcgill.ecse321.SportPlus.model.Owner;
import ca.mcgill.ecse321.SportPlus.model.SpecificClass;
import ca.mcgill.ecse321.SportPlus.model.ClassType;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Calendar;
import java.sql.Time;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SpecificClassIntegratiomTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private SpecificClassRepository specificClassRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        specificClassRepository.deleteAll();
        instructorRepository.deleteAll();
        classTypeRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    private static int CLASS_TYPE = 1;
    private static int INSTRUCTOR_ID = 1;
    private static final Date DATE = Date.valueOf("2024-04-16");
    private static final Time START_TIME = Time.valueOf("11:00:00");
    private static final Time END_TIME = Time.valueOf("12:00:00");

    @BeforeEach
    public void setup() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        // Assuming you have entities and repositories for Instructor and
        // ClassType
        Owner owner = new Owner("email@owner.com", "Johm", "password", "theOwner", 0);
        ownerRepository.save(owner);

        ClassType classType = new ClassType("yoga", "Fun Class", 0, true, owner);
        classTypeRepository.save(classType);

        Instructor instructor = new Instructor("Supervisor", "Bob", "password", "lastname", 0);
        instructorRepository.save(instructor);

        INSTRUCTOR_ID = instructor.getAccountId();

        CLASS_TYPE = classType.getTypeId();

    }

    @Test
    public void testCreateSpecificClass() {
        SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
        requestDto.setDate(DATE);
        requestDto.setStartTime(START_TIME);
        requestDto.setEndTime(END_TIME);
        requestDto.setInstructorId(INSTRUCTOR_ID);
        requestDto.setClassTypeId(CLASS_TYPE);

        ResponseEntity<SpecificClassResponseDto> response = client.postForEntity("/specificclasses/create", requestDto,
                SpecificClassResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        SpecificClassResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(responseBody.getDate().toLocalDate(), requestDto.getDate().toLocalDate());
        assertEquals(responseBody.getStartTime(), requestDto.getStartTime());
        assertEquals(responseBody.getInstructor().getAccountId(), requestDto.getInstructorId());
        assertEquals(responseBody.getEndTime(), requestDto.getEndTime());

    }

    @Test
    public void testCreateRecurringSpecificClasses() {
        // Prepare the request payload
        Date startDate = Date.valueOf("2024-04-01");
        Date endDate = Date.valueOf("2024-04-30");
        RecurringSpecificClassRequestDto requestDto = new RecurringSpecificClassRequestDto();
        requestDto.setDate(startDate);
        requestDto.setEndDate(endDate);
        requestDto.setStartTime(START_TIME);
        requestDto.setEndTime(END_TIME);
        requestDto.setDayOfWeek(1); // Monday
        requestDto.setInstructorId(INSTRUCTOR_ID);
        requestDto.setClassTypeId(CLASS_TYPE);

        // Perform the POST request
        ResponseEntity<RecurringSpecificClassesResponseDto> response = client.postForEntity("/recurring", requestDto,
                RecurringSpecificClassesResponseDto.class);

        // Assert the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        RecurringSpecificClassesResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        List<SpecificClassResponseDto> responseDtos = responseBody.getSpecificClasses();
        assertNotNull(responseDtos);
        // 5MondaysinApril
        assertEquals(5, responseDtos.size());
        for (SpecificClassResponseDto responseClass : responseDtos) {
            assertEquals(responseClass.getStartTime(), START_TIME);
            assertEquals(responseClass.getEndTime(), END_TIME);
            assertEquals(responseClass.getInstructor().getAccountId(), INSTRUCTOR_ID);
        }
    }

    @Test
    public void testUpdateSpecificClassDate() {

        // Assume there's a specific class with ID sessionId that you want to update
        ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
        SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType);
        Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
        specifcClass.setSupervisor(supervisor);
        specificClassRepository.save(specifcClass);
        SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

        Date newDate = Date.valueOf("2024-04-30");
        // Prepare the request payload with the new date
        SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
        requestDto.setDate(newDate);

        // Perform the PUT request
        String url = String.format("/%d/date", foundClass.getSessionId());

        HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                SpecificClassResponseDto.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificClassResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(newDate.toLocalDate(), responseBody.getDate().toLocalDate());

    }

    @Test
    public void testUpdateSpecificClassTime() {

        // Assume there's a specific class with ID sessionId that you want to update
        ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
        SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType);
        Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
        specifcClass.setSupervisor(supervisor);
        specificClassRepository.save(specifcClass);
        SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

        Time newStartTime = Time.valueOf("14:00:00");
        Time newEndTime = Time.valueOf("15:00:00");
        // Prepare the request payload with the new date
        SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
        requestDto.setStartTime(newStartTime);
        requestDto.setEndTime(newEndTime);

        // Perform the PUT request
        String url = String.format("/%d/time", foundClass.getSessionId());

        HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                SpecificClassResponseDto.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificClassResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(newStartTime, responseBody.getStartTime());
        assertEquals(newEndTime, responseBody.getEndTime());

    }

    @Test
    public void testUpdateSpecificClassClassType() {

        // Assume there's a specific class with ID sessionId that you want to update
        ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
        System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOO");
        System.out.println(classType);
        SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType);
        Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
        specifcClass.setSupervisor(supervisor);
        specificClassRepository.save(specifcClass);
        SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);
        Owner owner = ownerRepository.findByEmail("email@owner.com");
        ClassType newClassType = new ClassType("tennis", "Fun Class Tennis", 0, true, owner);
        classTypeRepository.save(classType);
        classTypeRepository.save(newClassType);
        ClassType newClass = classTypeRepository.findByName("tennis");
        // Prepare the request payload with the new date
        SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
        requestDto.setClassTypeId(newClass.getTypeId());

        // Perform the PUT request
        String url = String.format("/%d/classtype", foundClass.getSessionId());

        HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
        ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                SpecificClassResponseDto.class);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SpecificClassResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(newClass.getTypeId(), responseBody.getClassType().getTypeId());

    }
}
