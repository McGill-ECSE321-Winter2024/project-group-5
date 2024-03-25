package ca.mcgill.ecse321.SportPlus.Integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dto.ClientPaymentResponseDto;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodListDto;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodRequestDto;
import ca.mcgill.ecse321.SportPlus.dto.PaymentMethodResponseDto;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PaymentMethodIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {

        // Clear the databse before and after each test
        paymentMethodRepository.deleteAll();
        clientRepository.deleteAll();
    }

    // Set some global variables
    private static final String PAYMENTMETHOD_CARDNUMBER = "1234567812345678";
    private static final Date PAYMENTMETHOD_EXPDATE = new Date(1830297600000L);
    private static final String PAYMENTMETHOD_CVC = "111";
    private static final String PAYMENTMETHOD_CARDHOLDERNAME = "John Doe";
    private static final int PAYMENTMETHOD_CARDID = 1;

    private static final String CLIENT_EMAIL = "johndoe@email.com";

    @Test
    public void testFindPaymentMethodByCardNumber() {

        // Set up the paymment method
        // Save in the db
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Set the url
        String url = "/paymentMethod/getByCardNumber/" + PAYMENTMETHOD_CARDNUMBER;

        // Get the response
        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity(url, PaymentMethodResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentMethodResponseDto paymentMethodResponse = response.getBody();
        assertNotNull(paymentMethodResponse);
        assertTrue(paymentMethodResponse.getCardId() > 0);
        assertEquals(PAYMENTMETHOD_CARDHOLDERNAME, paymentMethodResponse.getCardHolderName());
        assertEquals(PAYMENTMETHOD_CARDNUMBER, paymentMethodResponse.getCardNumber());
        assertEquals(PAYMENTMETHOD_CLIENT, paymentMethodResponse.getClient());
    }

    @Test
    public void testFindPaymentMethodByCardNumber2() {

        // Set up the test
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Set the url
        String url = "/paymentMethod/getByCardNumber/" + PAYMENTMETHOD_CARDNUMBER + "/";

        // get the response
        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity(url, PaymentMethodResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentMethodResponseDto paymentMethodResponse = response.getBody();
        assertNotNull(paymentMethodResponse);
        assertTrue(paymentMethodResponse.getCardId() > 0);
        assertEquals(PAYMENTMETHOD_CARDHOLDERNAME, paymentMethodResponse.getCardHolderName());
        assertEquals(PAYMENTMETHOD_CARDNUMBER, paymentMethodResponse.getCardNumber());
        assertEquals(PAYMENTMETHOD_CLIENT, paymentMethodResponse.getClient());
    }

    @Test
    public void testFindPaymentMethodByClient() {

        // Set up a peyment Method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // get the clientId for the url
        int clientId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        // Set the url
        String url = "/paymentMethod/getByClient/" + clientId;

        // get the response
        ResponseEntity<PaymentMethodListDto> response = client.getForEntity(url, PaymentMethodListDto.class);

        // validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentMethodListDto paymentMethodResponse = response.getBody();
        assertNotNull(paymentMethodResponse);
        List<PaymentMethodResponseDto> theReponses = paymentMethodResponse.getPaymentMethods();
        assertNotNull(theReponses);
        for (PaymentMethodResponseDto reponse : theReponses) {
            assertTrue(reponse.getCardId() > 0);
            assertEquals(PAYMENTMETHOD_CARDHOLDERNAME, reponse.getCardHolderName());
            assertEquals(PAYMENTMETHOD_CARDNUMBER, reponse.getCardNumber());
            assertEquals(PAYMENTMETHOD_CLIENT, reponse.getClient());
        }
    }

    @Test
    public void testFindPaymentMethodByClient2() {

        // Set up a peyment Method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // get the clientId for the url
        int clientId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        // Set the url
        String url = "/paymentMethod/getByClient/" + clientId + "/";

        // get the response
        ResponseEntity<PaymentMethodListDto> response = client.getForEntity(url, PaymentMethodListDto.class);

        // validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PaymentMethodListDto paymentMethodResponse = response.getBody();
        assertNotNull(paymentMethodResponse);
        List<PaymentMethodResponseDto> theReponses = paymentMethodResponse.getPaymentMethods();
        assertNotNull(theReponses);
        for (PaymentMethodResponseDto reponse : theReponses) {
            assertTrue(reponse.getCardId() > 0);
            assertEquals(PAYMENTMETHOD_CARDHOLDERNAME, reponse.getCardHolderName());
            assertEquals(PAYMENTMETHOD_CARDNUMBER, reponse.getCardNumber());
            assertEquals(PAYMENTMETHOD_CLIENT, reponse.getClient());
        }
    }

    @Test
    public void testDeletePaymentMethodByCardNumber() {

        // Create a payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        // Set the url
        String urlToDelete = "/paymentMethod/deleteByCardNumber/" + PAYMENTMETHOD_CARDNUMBER;

        // Delete the payment mathod
        client.delete(urlToDelete);

        // Validate that it is now null
        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodByCardNumber2() {

        // Create a poyment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Checks if exists
        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        // Set the url
        String urlToDelete = "/paymentMethod/deleteByCardNumber/" + PAYMENTMETHOD_CARDNUMBER + "/";

        // Delete the payment method
        client.delete(urlToDelete);

        // Checks if delted succesfully
        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodsByClient() {

        // Create a peyment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Checks if exists
        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        // Get the id for the url
        int CLIENT_ID = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        // set the url.
        String urlToDelete = "/paymentMethod/deleteByClient/" + CLIENT_ID;

        // Delte the payment method
        client.delete(urlToDelete);

        // Checks if delted succesfully
        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodsByClient2() {

        // Create a payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Checks if exists
        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        // Find the id for the url
        int CLIENT_ID = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        // Set the url
        String urlToDelete = "/paymentMethod/deleteByClient/" + CLIENT_ID + "/";

        // Delete the payment method
        client.delete(urlToDelete);

        // Validate if deleted succesfully
        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testCreatePaymentMethod() {

        // Create a payment method request
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CLIENT);

        // get the response
        ResponseEntity<PaymentMethodResponseDto> response = client.postForEntity("/paymentMethod/create", request,
                PaymentMethodResponseDto.class);

        // validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PaymentMethodResponseDto createdPaymentMethod = response.getBody();
        assertNotNull(createdPaymentMethod);
        assertTrue(createdPaymentMethod.getCardId() > 0);
        assertEquals(createdPaymentMethod.getCardHolderName(), PAYMENTMETHOD_CARDHOLDERNAME);
        assertEquals(createdPaymentMethod.getCardNumber(), PAYMENTMETHOD_CARDNUMBER);
        assertEquals(createdPaymentMethod.getClient(), PAYMENTMETHOD_CLIENT);
        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testCreatePaymentMethod2() {

        // Create a payment method request
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CLIENT);

        // Get the response
        ResponseEntity<PaymentMethodResponseDto> response = client.postForEntity("/paymentMethod/create/", request,
                PaymentMethodResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PaymentMethodResponseDto createdPaymentMethod = response.getBody();
        assertNotNull(createdPaymentMethod);
        assertTrue(createdPaymentMethod.getCardId() > 0);
        assertEquals(createdPaymentMethod.getCardHolderName(), PAYMENTMETHOD_CARDHOLDERNAME);
        assertEquals(createdPaymentMethod.getCardNumber(), PAYMENTMETHOD_CARDNUMBER);
        assertEquals(createdPaymentMethod.getClient(), PAYMENTMETHOD_CLIENT);
        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testHasPaymentMethod() {

        // Create a payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Set the url;
        String url = "/clients/hasPaymentMethod/" + CLIENT_EMAIL;

        // Get the response
        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertTrue(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testHasPaymentMethod2() {

        // Create a payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        // Set the url;
        String url = "/clients/hasPaymentMethod/" + CLIENT_EMAIL + "/";

        // Get the response
        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertTrue(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testDoesNotHavePaymentMethod() {

        // Create a payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);
        Client OTHER_CLIENT = new Client("notme@email.com", "John", "Pass123", "Doe", 2);
        clientRepository.save(OTHER_CLIENT);

        // Set the url
        String url = "/clients/hasPaymentMethod/" + "notme@email.com";

        // Get the response
        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertFalse(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testDoesNotHavePaymentMethod2() {

        // Create a new payment method
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE,
                PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);
        Client OTHER_CLIENT = new Client("notme@email.com", "John", "Pass123", "Doe", 2);
        clientRepository.save(OTHER_CLIENT);

        // Set the url
        String url = "/clients/hasPaymentMethod/" + "notme@email.com" + "/";

        // Get the response
        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        // Validate the response
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertFalse(clientPayment.getHasPaymentMethod());
    }

}
