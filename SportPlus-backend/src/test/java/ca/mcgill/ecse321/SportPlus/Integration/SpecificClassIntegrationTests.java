package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
import org.springframework.web.util.UriComponentsBuilder;

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
import java.util.List;
import java.util.TimeZone;
import java.sql.Time;
import java.text.SimpleDateFormat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpecificClassIntegrationTests {

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

        private static int CLASS_TYPE = 1;
        private static int INSTRUCTOR_ID = 1;
        private static final Date DATE = Date.valueOf("2024-04-16");
        private static final Time START_TIME = Time.valueOf("11:00:00");
        private static final Time END_TIME = Time.valueOf("12:00:00");

        @BeforeEach
        @AfterEach
        public void init() {
                specificClassRepository.deleteAll();
                instructorRepository.deleteAll();
                classTypeRepository.deleteAll();
                ownerRepository.deleteAll();
        }

        @BeforeEach
        public void setup() {
                TimeZone.setDefault(TimeZone.getTimeZone("EDT"));

                Owner owner = new Owner("email@owner.com", "Johm", "password", "theOwner", 0);
                ownerRepository.save(owner);

                ClassType classType = new ClassType("yoga", "Fun Class Yoga it is", 0, true, owner);
                classTypeRepository.save(classType);

                Instructor instructor = new Instructor("Supervisor", "Bob", "password", "lastname", 0);
                instructorRepository.save(instructor);

                INSTRUCTOR_ID = instructor.getAccountId();

                CLASS_TYPE = classType.getTypeId();
        }

        @Test
        void testCreateSpecificClass() {
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setDate(DATE);
                requestDto.setStartTime(START_TIME);
                requestDto.setEndTime(END_TIME);
                requestDto.setInstructorId(INSTRUCTOR_ID);
                requestDto.setClassTypeId(CLASS_TYPE);

                ResponseEntity<SpecificClassResponseDto> response = client.postForEntity("/specificClass/create",
                                requestDto,
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
        void testCreateRecurringSpecificClasses() {
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
                ResponseEntity<RecurringSpecificClassesResponseDto> response = client.postForEntity(
                                "/specificClass/recurring",
                                requestDto,
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
        void testUpdateSpecificClassDate() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                Date newDate = Date.valueOf("2024-04-30");
                // Prepare the request payload with the new date
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setDate(newDate);

                // Perform the PUT request
                String url = String.format("/specificClass/%d/date", foundClass.getSessionId());

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
        void testUpdateSpecificClassTime() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
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
                String url = String.format("/specificClass/%d/time", foundClass.getSessionId());

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
        void testUpdateSpecificClassClassType() {
                // Create a SpecificCLass
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);

                classTypeRepository.save(classType);
                specificClassRepository.save(specifcClass);

                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Create a new ClassType
                Owner owner = ownerRepository.findByEmail("email@owner.com");
                ClassType newClassType = new ClassType("tennis", "Fun Class Tennis", 0, true, owner);

                classTypeRepository.save(newClassType);

                ClassType newClass = classTypeRepository.findByName("tennis");

                // Prepare the request payload with the new ClassType
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setClassTypeId(newClass.getTypeId());

                // Perform the PUT request
                String url = String.format("/specificClass/%d/classtype", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(foundClass.getStartTime(), responseBody.getStartTime());
                assertEquals(newClass.getTypeId(), responseBody.getClassType().getTypeId());
                assertEquals(newClass.getName(), responseBody.getClassType().getName());
        }

        @Test
        void testAssignSpecificClassInstructor() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Prepare the request payload with the new instructor
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setInstructorId(INSTRUCTOR_ID);

                // Perform the PUT request
                String url = String.format("/specificClass/%d/assign-instructor", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(INSTRUCTOR_ID, responseBody.getInstructor().getAccountId());
        }

        @Test
        void testRemoveSpecificClassInstructor() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Prepare the request payload
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();

                // Perform the PUT request
                String url = String.format("/specificClass/%d/remove-instructor", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertNull(responseBody.getInstructor());
        }

        @Test
        void testGetSpecificClassByInstructor() {
                // Create 2 SpecificCLasses
                Date DATE2 = Date.valueOf("2024-04-18");
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE2, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);

                String url = "/specificClass/instructor/" + INSTRUCTOR_ID;

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that 2 classes are returned
        }

        @Test
        void testGetSpecificClassByDate() {
                // Create 2 classes on the same date with different starting times
                Time START_TIME2 = Time.valueOf("14:00:00");
                Time END_TIME2 = Time.valueOf("15:00:00");
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE, START_TIME2, END_TIME2, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);

                // Construct the URL with the formatted date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(DATE);

                String url = "/specificClass/by-date?date=" + formattedDate;

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that 2 classes are returned
        }

        @Test
        void testGetSpecificClassByClassType() {
                // Create 2 classes with different ClassTypes
                ClassType classType1 = classTypeRepository.findByTypeId(CLASS_TYPE);
                Owner owner = ownerRepository.findByEmail("email@owner.com");
                ClassType classType2 = new ClassType("tennis", "Fun Class Tennis", 0, true, owner);
                classTypeRepository.save(classType2);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType1, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType2, null);
                SpecificClass specifcClass3 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType1, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specifcClass3.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);
                specificClassRepository.save(specifcClass3);

                String url = String.format("/specificClass/class-type/%d", CLASS_TYPE);

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that only 2 classes are returned
        }

        @Test
        void testGetSpecificClassByDateRange() {
                // Setup data
                Date startDate = Date.valueOf("2024-04-10"); // April 10th
                Date endDate = Date.valueOf("2024-04-20"); // April 20th
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                classTypeRepository.save(classType);
                SpecificClass class1 = new SpecificClass(Date.valueOf("2024-04-11"), START_TIME, END_TIME, 0, classType,
                                null); // April
                // 11th
                SpecificClass class2 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);// April 16th
                specificClassRepository.save(class1);
                specificClassRepository.save(class2);

                // Construct the URL with query parameters
                String url = String.format("/specificClass/by-date-range?startDate=%s&endDate=%s", startDate.toString(),
                                endDate.toString());

                // Execute the GET request
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // 2 Classes in range from, April 10th to April 20th
        }

        @Test
        void testGetAvailableSpecificClasses() {
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(Date.valueOf("2024-04-11"), Time.valueOf("14:00:00"),
                                Time.valueOf("15:00:00"), 0, classType, null); // In the future
                SpecificClass availableClass2 = new SpecificClass(Date.valueOf("2024-04-11"), Time.valueOf("21:00:00"),
                                Time.valueOf("22:00:00"), 0, classType, null);// Same date, just different time whould
                                                                              // still be
                                                                              // available
                SpecificClass unavailableClass = new SpecificClass(Date.valueOf("2024-04-12"), Time.valueOf("14:00:00"),
                                Time.valueOf("15:00:00"), 0, classType, null);// In the future, but without an
                                                                              // instructor
                availableClass.setSupervisor(supervisor);
                availableClass2.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                specificClassRepository.save(availableClass2);
                specificClassRepository.save(unavailableClass);

                // Call the /available endpoint
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity("/specificClass/available",
                                SpecificClassResponseDto[].class);

                // Assert the response status is OK
                assertEquals(HttpStatus.OK, response.getStatusCode());

                // Assert the response body
                SpecificClassResponseDto[] responseDtos = response.getBody();
                assertNotNull(responseDtos);

                assertEquals(2, responseDtos.length); // Check that the 2 class are returned as available
                assertEquals(CLASS_TYPE, responseDtos[0].getClassType().getTypeId());
        }

        @Test
        void testGetByDateAndStartTimeSpecificClasses() {
                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);

                String date = DATE.toString();
                String startTime = START_TIME.toString();

                // Construct URL with query parameters
                String urlTemplate = UriComponentsBuilder.fromPath("/specificClass/by-date-and-start-time")
                                .queryParam("date", date)
                                .queryParam("startTime", startTime)
                                .toUriString();

                ResponseEntity<SpecificClassResponseDto> response = client.getForEntity(urlTemplate,
                                SpecificClassResponseDto.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());

                // Assert more fields as necessary
                SpecificClassResponseDto specificClassResponse = response.getBody();
                assertNotNull(specificClassResponse.getId());
                assertEquals(java.sql.Date.valueOf(date), specificClassResponse.getDate());
                assertEquals(Time.valueOf(startTime), specificClassResponse.getStartTime());
        }

        @Test
        void testGetAllSpecificClasses() {
                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                // The URL for the endpoint
                String url = "/specificClass/all";

                // Make the GET request
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                // Assert the response status
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected OK status");

                // Assert the response body
                SpecificClassResponseDto[] responseBody = response.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                assertFalse(responseBody.length == 0, "Response body should contain one or more elements");
                assertEquals(1, responseBody.length);
        }

        @Test
        void testDeleteSpecificClassesByClassType() {
                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                SpecificClass class1 = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // The URL for the endpoint
                String url = "/specificClass/class-type/" + class1.getSessionId();

                // Make the DELETE request
                ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

                // Assert the response status
                assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Expected NO_CONTENT status");
        }

        @Test
        void testCreateSpecificClass2() {
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setDate(DATE);
                requestDto.setStartTime(START_TIME);
                requestDto.setEndTime(END_TIME);
                requestDto.setInstructorId(INSTRUCTOR_ID);
                requestDto.setClassTypeId(CLASS_TYPE);

                ResponseEntity<SpecificClassResponseDto> response = client.postForEntity("/specificClass/create/",
                                requestDto,
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
        void testCreateRecurringSpecificClasses2() {
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
                ResponseEntity<RecurringSpecificClassesResponseDto> response = client.postForEntity(
                                "/specificClass/recurring/",
                                requestDto,
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
        void testUpdateSpecificClassDate2() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                Date newDate = Date.valueOf("2024-04-30");
                // Prepare the request payload with the new date
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setDate(newDate);

                // Perform the PUT request
                String url = String.format("/specificClass/%d/date/", foundClass.getSessionId());

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
        void testUpdateSpecificClassTime2() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
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
                String url = String.format("/specificClass/%d/time/", foundClass.getSessionId());

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
        void testUpdateSpecificClassClassType2() {
                // Create a SpecificCLass
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);

                classTypeRepository.save(classType);
                specificClassRepository.save(specifcClass);

                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Create a new ClassType
                Owner owner = ownerRepository.findByEmail("email@owner.com");
                ClassType newClassType = new ClassType("tennis", "Fun Class Tennis", 0, true, owner);

                classTypeRepository.save(newClassType);

                ClassType newClass = classTypeRepository.findByName("tennis");

                // Prepare the request payload with the new ClassType
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setClassTypeId(newClass.getTypeId());

                // Perform the PUT request
                String url = String.format("/specificClass/%d/classtype/", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(foundClass.getStartTime(), responseBody.getStartTime());
                assertEquals(newClass.getTypeId(), responseBody.getClassType().getTypeId());
                assertEquals(newClass.getName(), responseBody.getClassType().getName());
        }

        @Test
        void testAssignSpecificClassInstructor2() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Prepare the request payload with the new instructor
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();
                requestDto.setInstructorId(INSTRUCTOR_ID);

                // Perform the PUT request
                String url = String.format("/specificClass/%d/assign-instructor/", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertEquals(INSTRUCTOR_ID, responseBody.getInstructor().getAccountId());
        }

        @Test
        void testRemoveSpecificClassInstructor2() {
                // Assume there's a specific class with ID sessionId that you want to update
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                SpecificClass foundClass = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // Prepare the request payload
                SpecificClassRequestsDto requestDto = new SpecificClassRequestsDto();

                // Perform the PUT request
                String url = String.format("/specificClass/%d/remove-instructor/", foundClass.getSessionId());

                HttpEntity<SpecificClassRequestsDto> requestEntity = new HttpEntity<>(requestDto);
                ResponseEntity<SpecificClassResponseDto> response = client.exchange(url, HttpMethod.PUT, requestEntity,
                                SpecificClassResponseDto.class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto responseBody = response.getBody();
                assertNotNull(responseBody);
                assertNull(responseBody.getInstructor());
        }

        @Test
        void testGetSpecificClassByInstructor2() {
                // Create 2 SpecificCLasses
                Date DATE2 = Date.valueOf("2024-04-18");
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE2, START_TIME, END_TIME, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);

                String url = "/specificClass/instructor/" + INSTRUCTOR_ID + "/";

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that 2 classes are returned
        }

        @Test
        void testGetSpecificClassByDate2() {
                // Create 2 classes on the same date with different starting times
                Time START_TIME2 = Time.valueOf("14:00:00");
                Time END_TIME2 = Time.valueOf("15:00:00");
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE, START_TIME2, END_TIME2, 0, classType, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);

                // Construct the URL with the formatted date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = sdf.format(DATE);

                String url = "/specificClass/by-date?date=" + formattedDate + "/";

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that 2 classes are returned
        }

        @Test
        void testGetSpecificClassByClassType2() {
                // Create 2 classes with different ClassTypes
                ClassType classType1 = classTypeRepository.findByTypeId(CLASS_TYPE);
                Owner owner = ownerRepository.findByEmail("email@owner.com");
                ClassType classType2 = new ClassType("tennis", "Fun Class Tennis", 0, true, owner);
                classTypeRepository.save(classType2);
                SpecificClass specifcClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType1, null);
                SpecificClass specifcClass2 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType2, null);
                SpecificClass specifcClass3 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType1, null);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                specifcClass.setSupervisor(supervisor);
                specifcClass2.setSupervisor(supervisor);
                specifcClass3.setSupervisor(supervisor);
                specificClassRepository.save(specifcClass);
                specificClassRepository.save(specifcClass2);
                specificClassRepository.save(specifcClass3);

                String url = String.format("/specificClass/class-type/%d/", CLASS_TYPE);

                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                assertEquals(HttpStatus.OK, response.getStatusCode());

                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // Check that only 2 classes are returned
        }

        @Test
        void testGetSpecificClassByDateRange2() {
                // Setup data
                Date startDate = Date.valueOf("2024-04-10"); // April 10th
                Date endDate = Date.valueOf("2024-04-20"); // April 20th
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                classTypeRepository.save(classType);
                SpecificClass class1 = new SpecificClass(Date.valueOf("2024-04-11"), START_TIME, END_TIME, 0, classType,
                                null); // April
                // 11th
                SpecificClass class2 = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);// April 16th
                specificClassRepository.save(class1);
                specificClassRepository.save(class2);

                // Construct the URL with query parameters
                String url = String.format("/specificClass/by-date-range?startDate=%s&endDate=%s/",
                                startDate.toString(),
                                endDate.toString());

                // Execute the GET request
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                // Assert the response
                assertEquals(HttpStatus.OK, response.getStatusCode());
                SpecificClassResponseDto[] specificClasses = response.getBody();
                assertNotNull(specificClasses);
                assertEquals(2, specificClasses.length); // 2 Classes in range from, April 10th to April 20th
        }

        @Test
        void testGetAvailableSpecificClasses2() {
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(Date.valueOf("2024-04-11"), Time.valueOf("14:00:00"),
                                Time.valueOf("15:00:00"), 0, classType, null); // In the future
                SpecificClass availableClass2 = new SpecificClass(Date.valueOf("2024-04-11"), Time.valueOf("21:00:00"),
                                Time.valueOf("22:00:00"), 0, classType, null);// Same date, just different time whould
                                                                              // still be
                                                                              // available
                SpecificClass unavailableClass = new SpecificClass(Date.valueOf("2024-04-12"), Time.valueOf("14:00:00"),
                                Time.valueOf("15:00:00"), 0, classType, null);// In the future, but without an
                                                                              // instructor
                availableClass.setSupervisor(supervisor);
                availableClass2.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                specificClassRepository.save(availableClass2);
                specificClassRepository.save(unavailableClass);

                // Call the /available endpoint
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity("/specificClass/available/",
                                SpecificClassResponseDto[].class);

                // Assert the response status is OK
                assertEquals(HttpStatus.OK, response.getStatusCode());

                // Assert the response body
                SpecificClassResponseDto[] responseDtos = response.getBody();
                assertNotNull(responseDtos);

                assertEquals(2, responseDtos.length); // Check that the 2 class are returned as available
                assertEquals(CLASS_TYPE, responseDtos[0].getClassType().getTypeId());
        }

        @Test
        void testGetByDateAndStartTimeSpecificClasses2() {
                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);

                String date = DATE.toString();
                String startTime = START_TIME.toString();

                // Construct URL with query parameters
                String urlTemplate = UriComponentsBuilder.fromPath("/specificClass/by-date-and-start-time/")
                                .queryParam("date", date)
                                .queryParam("startTime", startTime)
                                .toUriString();

                ResponseEntity<SpecificClassResponseDto> response = client.getForEntity(urlTemplate,
                                SpecificClassResponseDto.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());

                // Assert more fields as necessary
                SpecificClassResponseDto specificClassResponse = response.getBody();
                assertNotNull(specificClassResponse.getId());
                assertEquals(java.sql.Date.valueOf(date), specificClassResponse.getDate());
                assertEquals(Time.valueOf(startTime), specificClassResponse.getStartTime());
        }

        @Test
        void testGetAllSpecificClasses2() {
                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                // The URL for the endpoint
                String url = "/specificClass/all/";

                // Make the GET request
                ResponseEntity<SpecificClassResponseDto[]> response = client.getForEntity(url,
                                SpecificClassResponseDto[].class);

                // Assert the response status
                assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected OK status");

                // Assert the response body
                SpecificClassResponseDto[] responseBody = response.getBody();
                assertNotNull(responseBody, "Response body should not be null");
                assertFalse(responseBody.length == 0, "Response body should contain one or more elements");
                assertEquals(1, responseBody.length);
        }

        @Test
        void testDeleteSpecificClassesByClassType2() {

                // Create specific classes
                ClassType classType = classTypeRepository.findByTypeId(CLASS_TYPE);
                Instructor supervisor = instructorRepository.findByAccountId(INSTRUCTOR_ID);
                SpecificClass availableClass = new SpecificClass(DATE, START_TIME, END_TIME, 0, classType, null);
                availableClass.setSupervisor(supervisor);
                specificClassRepository.save(availableClass);
                SpecificClass class1 = specificClassRepository.findByDateAndStartTime(DATE, START_TIME);

                // The URL for the endpoint
                String url = "/specificClass/class-type/" + class1.getSessionId() + "/";

                // Make the DELETE request
                ResponseEntity<Void> response = client.exchange(url, HttpMethod.DELETE, null, Void.class);

                // Assert the response status
                assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Expected NO_CONTENT status");
        }

}
