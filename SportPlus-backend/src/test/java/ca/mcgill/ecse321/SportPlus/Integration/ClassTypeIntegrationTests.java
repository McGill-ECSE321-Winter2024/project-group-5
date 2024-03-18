package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ClassTypeIntegrationTests {
    public String CLASS_TYPE_NAME="Soccer";
    public String CLASS_TYPE_DESCRIPTION="description.";


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

        ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create", requestDto, ClassTypeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClassTypeResponseDto classTypeResponse = response.getBody();
        assertNotNull(classTypeResponse);
        assertEquals(requestDto.getName(), classTypeResponse.getName());
        assertEquals(requestDto.getDescription(), classTypeResponse.getDescription());
    }

    @Test
    public void testFindAllClassTypes() {
        // Pre-populate the database with two classType
        createClassTypeForTesting("Yoga", "A relaxing yoga class");
        createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
        ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all", ClassTypeListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClassTypeListDto classTypeList = response.getBody();
        assertNotNull(classTypeList);
        assertEquals(2, classTypeList.getClassTypes().size());
        assertEquals("Yoga", classTypeList.getClassTypes().get(0).getName());
        assertEquals(CLASS_TYPE_NAME, classTypeList.getClassTypes().get(1).getName());
    }

@SuppressWarnings("null")
@Test
public void testFindClassTypeByName() {
    // Setup: Create a class type to find
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);    
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME, ClassTypeResponseDto.class);
    
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(CLASS_TYPE_NAME, response.getBody().getName());
}
@SuppressWarnings("null")
@Test
public void testDeleteClassTypeByName() {
    createClassTypeForTesting("Yoga", "A relaxing yoga class");
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    restTemplate.delete("/classType/delete/" + CLASS_TYPE_NAME);
    // Verify the class type is deleted
    ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all", ClassTypeListDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getClassTypes().size()==1);
}

@SuppressWarnings("null")
@Test
public void testApproveClassType() {
    // Setup: Create a class type to approve
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    // approve it withe a request
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/approve/" + createdClassType.getTypeId(), null, ClassTypeResponseDto.class);
    
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isApproved());
}

@SuppressWarnings("null")
@Test
public void testUpdateClassTypeName() {
    // Setup: Create a class type to update
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    
    String updatedName = "soccer";
    restTemplate.put("/classType/updateName/" + createdClassType.getTypeId(), updatedName);
    
    // Verify update
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + updatedName, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedName, response.getBody().getName());
}

@SuppressWarnings("null")           
@Test
public void testUpdateClassTypeDescription() {
    // Setup: Create a class type to update
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    
    String updatedDescription = "An intense Yoga class";
    restTemplate.put("/classType/updateDescription/" + createdClassType.getTypeId(), updatedDescription);
    
    // Fetch and verify update
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedDescription, response.getBody().getDescription());
}

private ClassTypeResponseDto createClassTypeForTesting(String name, String description) {
    ClassTypeRequestDto requestDto = new ClassTypeRequestDto();
    requestDto.setName(name);
    requestDto.setDescription(description);
    
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create", requestDto, ClassTypeResponseDto.class);
    return response.getBody();
}
}
