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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.OwnerRepository;
import ca.mcgill.ecse321.SportPlus.dto.OwnerRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.OwnerResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Owner;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OwnerIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private OwnerRepository ownerRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        ownerRepository.deleteAll();
    }

    private static final String OWNER_EMAIL = "owner@sportplus.com";
    private static final String OWNER_FIRSTNAME = "John";
    private static final String OWNER_LASTNAME = "Doe";
    private static final String OWNER_PASSWORD = "Password123";

    private int OWNER_VALID_ACCOUNTID;

    @Test
    public void testFindOwner() {
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_VALID_ACCOUNTID);
        ownerRepository.save(owner);

        String url = "/owner/get";

        ResponseEntity<OwnerResponseDto> response = client.getForEntity(url, OwnerResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OwnerResponseDto ownerResponse = response.getBody();
        assertNotNull(ownerResponse);
        assertTrue(ownerResponse.getAccountId() > 0);
        assertEquals(OWNER_EMAIL, ownerResponse.getEmail());
        assertEquals(OWNER_FIRSTNAME, ownerResponse.getFirstName());
        assertEquals(OWNER_LASTNAME, ownerResponse.getLastName());
    }

    @Test
    public void testFindOwner2() {
        Owner owner = new Owner(OWNER_EMAIL, OWNER_FIRSTNAME, OWNER_PASSWORD, OWNER_LASTNAME, OWNER_VALID_ACCOUNTID);
        ownerRepository.save(owner);

        String url = "/owner/get/";

        ResponseEntity<OwnerResponseDto> response = client.getForEntity(url, OwnerResponseDto.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        OwnerResponseDto ownerResponse = response.getBody();
        assertNotNull(ownerResponse);
        assertTrue(ownerResponse.getAccountId() > 0);
        assertEquals(OWNER_EMAIL, ownerResponse.getEmail());
        assertEquals(OWNER_FIRSTNAME, ownerResponse.getFirstName());
        assertEquals(OWNER_LASTNAME, ownerResponse.getLastName());
    }

    @Test
    public void testCreateOwner() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertEquals(createdOwner.getEmail(), OWNER_EMAIL);
        assertEquals(createdOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(createdOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(createdOwner.getAccountId());
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
    }

    @Test
    public void testCreateOwner2() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create/", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertEquals(createdOwner.getEmail(), OWNER_EMAIL);
        assertEquals(createdOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(createdOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(createdOwner.getAccountId());
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
    }

    @Test
    public void testUpdateOwnerFirstName() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/owner/updateFirstName/" + newFirstName;

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), newFirstName);
        assertEquals(updatedOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getFirstName(), newFirstName);
    }

    @Test
    public void testUpdateOwnerFirstName2() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newFirstName = "JohnTheNew";

        String url = "/owner/updateFirstName/" + newFirstName + "/";

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), newFirstName);
        assertEquals(updatedOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getFirstName(), newFirstName);
    }

    @Test
    public void testUpdateOwnerLastName() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/owner/updateLastName/" + newLastName;

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(updatedOwner.getLastName(), newLastName);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getLastName(), newLastName);
    }

    @Test
    public void testUpdateOwnerLastName2() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newLastName = "DoeTheNew";

        String url = "/owner/updateLastName/" + newLastName + "/";

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(updatedOwner.getLastName(), newLastName);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getLastName(), newLastName);
    }

    @Test
    public void testUpdateOwnerPassword() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/owner/updatePassword/" + OWNER_PASSWORD + "/" + newPassword;

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(updatedOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getPassword(), newPassword);
    }

    @Test
    public void testUpdateOwnerPassword2() {
        OwnerRequestDto request = new OwnerRequestDto(OWNER_FIRSTNAME, OWNER_LASTNAME, OWNER_PASSWORD);

        ResponseEntity<OwnerResponseDto> response = client.postForEntity("/owner/create", request, OwnerResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        OwnerResponseDto createdOwner = response.getBody();
        assertNotNull(createdOwner);
        assertTrue(createdOwner.getAccountId() > 0, "Response should have a positive ID.");
        OWNER_VALID_ACCOUNTID = createdOwner.getAccountId();

        String newPassword = "TheNewPass456";

        String url = "/owner/updatePassword/" + OWNER_PASSWORD + "/" + newPassword + "/";

        ResponseEntity<OwnerResponseDto> responseAfterUpdate = client.exchange(url, HttpMethod.PUT, null, OwnerResponseDto.class);
        assertNotNull(responseAfterUpdate);
        assertEquals(HttpStatus.OK, responseAfterUpdate.getStatusCode());
        OwnerResponseDto updatedOwner = responseAfterUpdate.getBody();
        assertNotNull(updatedOwner);
        assertEquals(updatedOwner.getEmail(), OWNER_EMAIL);
        assertEquals(updatedOwner.getFirstName(), OWNER_FIRSTNAME);
        assertEquals(updatedOwner.getLastName(), OWNER_LASTNAME);
        assertNotNull(updatedOwner.getAccountId());
        assertTrue(updatedOwner.getAccountId() > 0, "Response should have a positive ID.");
        assertNotNull(ownerRepository.findByEmail(OWNER_EMAIL));
        assertEquals(ownerRepository.findByEmail(OWNER_EMAIL).getPassword(), newPassword);
    }
    
}
