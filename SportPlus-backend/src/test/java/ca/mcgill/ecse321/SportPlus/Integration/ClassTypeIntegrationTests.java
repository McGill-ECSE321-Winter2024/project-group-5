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

    public String CLASS_TYPE_NAME = "Soccer";
    public String CLASS_TYPE_DESCRIPTION = "description.";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ClassTypeRepository classTypeRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        classTypeRepository.deleteAll();
    }
    // Test case for creating a new class type using a POST request, validating successful creation and response data.
    @Test
    public void testCreateNewClassType() {
        ClassTypeRequestDto requestDto = new ClassTypeRequestDto();
        requestDto.setName("Yoga");
        requestDto.setDescription("A relaxing yoga class");

        ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create", requestDto,
                ClassTypeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClassTypeResponseDto classTypeResponse = response.getBody();
        assertNotNull(classTypeResponse);
        assertEquals(requestDto.getName(), classTypeResponse.getName());
        assertEquals(requestDto.getDescription(), classTypeResponse.getDescription());
    }

        // Test case for creating a new class type ensuring endpoint consistency with trailing slash.
    @Test
    public void testCreateNewClassType2() {
        ClassTypeRequestDto requestDto = new ClassTypeRequestDto();
        requestDto.setName("Yoga");
        requestDto.setDescription("A relaxing yoga class");

        ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create/", requestDto,
                ClassTypeResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ClassTypeResponseDto classTypeResponse = response.getBody();
        assertNotNull(classTypeResponse);
        assertEquals(requestDto.getName(), classTypeResponse.getName());
        assertEquals(requestDto.getDescription(), classTypeResponse.getDescription());
    }

    // Validates the functionality of fetching all class types, ensuring the response includes all entries.
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

    // Validates the functionality of fetching all class types, ensuring the response includes all entries.

    @Test
    public void testFindAllClassTypes2() {
        // Pre-populate the database with two classType
        createClassTypeForTesting("Yoga", "A relaxing yoga class");
        createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
        ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all/",
                ClassTypeListDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClassTypeListDto classTypeList = response.getBody();
        assertNotNull(classTypeList);
        assertEquals(2, classTypeList.getClassTypes().size());
        assertEquals("Yoga", classTypeList.getClassTypes().get(0).getName());
        assertEquals(CLASS_TYPE_NAME, classTypeList.getClassTypes().get(1).getName());
    }

// Test finding a ClassType by name. It validates that the correct ClassType is retrieved.
@SuppressWarnings("null")
@Test
public void testFindClassTypeByName() {
    // Setup: Create a class type to find.
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    // Action: Fetch the created class type by its name.
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME, ClassTypeResponseDto.class);
    
    // Assertions: Ensure the response is correct and contains the expected class type details.
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(CLASS_TYPE_NAME, response.getBody().getName());
}

// Similar to testFindClassTypeByName but tests the endpoint's handling of a trailing slash.
@SuppressWarnings("null")
@Test
public void testFindClassTypeByName2() {
    // Setup and action are similar to testFindClassTypeByName, focusing on URL consistency.
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME + "/", ClassTypeResponseDto.class);
    
    // Assertions are identical, verifying the outcome is unaffected by the trailing slash.
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(CLASS_TYPE_NAME, response.getBody().getName());
}

// Tests the deletion of a ClassType by name, verifying that the specified class type is successfully removed.
@SuppressWarnings("null")
@Test
public void testDeleteClassTypeByName() {
    // Setup: Create two class types, one of which will be deleted.
    createClassTypeForTesting("Yoga", "A relaxing yoga class");
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    // Action: Delete one of the created class types by name.
    restTemplate.delete("/classType/delete/" + CLASS_TYPE_NAME);
    
    // Verification: Fetch all class types and verify the deletion was successful.
    ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all", ClassTypeListDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getClassTypes().size() == 1); // Expecting only one class type to remain.
}

// Similar to testDeleteClassTypeByName but with a trailing slash in the URL, testing consistent URL handling.
@SuppressWarnings("null")
@Test
public void testDeleteClassTypeByName2() {
    // Setup and action are identical to the previous deletion test, ensuring endpoint consistency.
    createClassTypeForTesting("Yoga", "A relaxing yoga class");
    createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    restTemplate.delete("/classType/delete/" + CLASS_TYPE_NAME + "/");
    
    // Verification ensures the class type was deleted, with a focus on URL format.
    ResponseEntity<ClassTypeListDto> response = restTemplate.getForEntity("/classType/all", ClassTypeListDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getClassTypes().size() == 1);
}

// Tests approving a ClassType, verifying the approval status is updated correctly.
@SuppressWarnings("null")
@Test
public void testApproveClassType() {
    // Setup: Create and then approve a class type.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/approve/" + createdClassType.getTypeId(), null, ClassTypeResponseDto.class);
    
    // Verification: Check that the class type's approval status is updated to true.
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isApproved());
}

// Similar to testApproveClassType but checks the endpoint's behavior with a trailing slash.
@SuppressWarnings("null")
@Test
public void testApproveClassType2() {
    // Setup and verification steps mirror testApproveClassType, emphasizing URL handling.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/approve/" + createdClassType.getTypeId() + "/", null, ClassTypeResponseDto.class);
    
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isApproved());
}

    // Tests the functionality of updating a ClassType's name. It verifies that after updating the name of a ClassType, 
// the system correctly reflects the change when the ClassType is fetched by the new name.
@SuppressWarnings("null")
@Test
public void testUpdateClassTypeName() {
    // Setup: Create a class type to be updated.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);

    // Action: Update the class type's name to a new value.
    String updatedName = "soccer";
    restTemplate.put("/classType/updateName/" + createdClassType.getTypeId(), updatedName);

    // Verification: Fetch the class type by the new name and verify the update was successful.
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + updatedName, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedName, response.getBody().getName());
}

// Similar to testUpdateClassTypeName but ensures that the endpoint correctly processes requests with a trailing slash.
@SuppressWarnings("null")
@Test
public void testUpdateClassTypeName2() {
    // Setup: Identical to the previous test, aiming to update a class type's name.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);

    // Action: Execute the name update with a trailing slash in the request URL.
    String updatedName = "soccer";
    restTemplate.put("/classType/updateName/" + createdClassType.getTypeId() + "/", updatedName);

    // Verification: Ensure the class type's name is updated by fetching it with the new name.
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + updatedName, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedName, response.getBody().getName());
}

// Tests the functionality of updating a ClassType's description. It confirms that the system accurately updates and reflects 
// the new description when the ClassType is retrieved.
@SuppressWarnings("null")
@Test
public void testUpdateClassTypeDescription() {
    // Setup: Create a class type whose description will be updated.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);

    // Action: Perform the update of the class type's description.
    String updatedDescription = "An intense Yoga class";
    restTemplate.put("/classType/updateDescription/" + createdClassType.getTypeId(), updatedDescription);

    // Verification: Fetch the class type to verify the description has been updated as expected.
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedDescription, response.getBody().getDescription());
}

// Similar to testUpdateClassTypeDescription, this test verifies the system's handling of trailing slashes in the URL 
// for description updates.
@SuppressWarnings("null")
@Test
public void testUpdateClassTypeDescription2() {
    // Setup: Repeat the setup from the previous test, preparing a class type for description update.
    ClassTypeResponseDto createdClassType = createClassTypeForTesting(CLASS_TYPE_NAME, CLASS_TYPE_DESCRIPTION);

    // Action: Update the class type's description, including a trailing slash in the request URL.
    String updatedDescription = "An intense Yoga class";
    restTemplate.put("/classType/updateDescription/" + createdClassType.getTypeId() + "/", updatedDescription);

    // Verification: Confirm the update by fetching the class type and checking the new description.
    ResponseEntity<ClassTypeResponseDto> response = restTemplate.getForEntity("/classType/get/" + CLASS_TYPE_NAME, ClassTypeResponseDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(updatedDescription, response.getBody().getDescription());
}
    @SuppressWarnings("null")
    @Test
    private ClassTypeResponseDto createClassTypeForTesting(String name, String description) {
        ClassTypeRequestDto requestDto = new ClassTypeRequestDto();
        requestDto.setName(name);
        requestDto.setDescription(description);

        ResponseEntity<ClassTypeResponseDto> response = restTemplate.postForEntity("/classType/create", requestDto,
                ClassTypeResponseDto.class);
        return response.getBody();
    }
}
