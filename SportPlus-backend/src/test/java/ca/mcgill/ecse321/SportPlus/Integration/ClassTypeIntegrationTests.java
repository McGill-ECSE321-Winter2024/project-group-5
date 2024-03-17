package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClassTypeRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeListDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.ClassTypeResponseDto;
import ca.mcgill.ecse321.SportPlus.model.ClassType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClassTypeIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        classTypeRepository.deleteAll();
    }

    @Test
    public void testCreateNewClassType() {
        ClassTypeRequestDto requestDto = new ClassTypeRequestDto();
        requestDto.setName("Yoga");
        requestDto.setDescription("A relaxing yoga class");
        // Assuming approver logic is implemented elsewhere or not needed for the test

        ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create", requestDto, ClassTypeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClassTypeResponseDto classTypeResponse = response.getBody();
        assertNotNull(classTypeResponse);
        assertEquals(requestDto.getName(), classTypeResponse.getName());
        assertEquals(requestDto.getDescription(), classTypeResponse.getDescription());
        // Additional assertions as needed
    }

    @Test
    public void testFindAllClassTypes() {
        // Pre-populate the database with a classType
        ClassType classType = new ClassType("Yoga", "A relaxing yoga class", 0, false, null);
        classTypeRepository.save(classType);

        ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all", ClassTypeListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClassTypeListDto classTypeList = response.getBody();
        assertNotNull(classTypeList);
        assertEquals(1, classTypeList.getClassTypes().size());
        // Additional assertions as needed
    }

    // Implement additional tests for other endpoints as needed
}
