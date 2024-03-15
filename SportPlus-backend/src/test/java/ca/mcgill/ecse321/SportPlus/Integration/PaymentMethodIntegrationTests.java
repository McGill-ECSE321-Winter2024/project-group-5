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
        paymentMethodRepository.deleteAll();
        clientRepository.deleteAll();
    }

    private static final String PAYMENTMETHOD_CARDNUMBER = "1234567812345678";
    private static final Date PAYMENTMETHOD_EXPDATE = new Date(1830297600000L);
    private static final String PAYMENTMETHOD_CVC = "111";
    private static final String PAYMENTMETHOD_CARDHOLDERNAME = "John Doe";
    private static final int PAYMENTMETHOD_CARDID = 1;

    private static final String CLIENT_EMAIL = "johndoe@email.com";
    
    @Test
    public void testFindPaymentMethodByCardNumber() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        String url = "/paymentMethod/getByCardNumber/" + PAYMENTMETHOD_CARDNUMBER;

        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity(url, PaymentMethodResponseDto.class);

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
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        String url = "/paymentMethod/getByCardNumber/" + PAYMENTMETHOD_CARDNUMBER + "/";

        ResponseEntity<PaymentMethodResponseDto> response = client.getForEntity(url, PaymentMethodResponseDto.class);

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
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        int clientId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String url = "/paymentMethod/getByClient/" + clientId;

        ResponseEntity<PaymentMethodListDto> response = client.getForEntity(url, PaymentMethodListDto.class);

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
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        int clientId = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String url = "/paymentMethod/getByClient/" + clientId + "/";

        ResponseEntity<PaymentMethodListDto> response = client.getForEntity(url, PaymentMethodListDto.class);

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
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        String urlToDelete = "/paymentMethod/deleteByCardNumber/" + PAYMENTMETHOD_CARDNUMBER;

        client.delete(urlToDelete);

        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodByCardNumber2() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        String urlToDelete = "/paymentMethod/deleteByCardNumber/" + PAYMENTMETHOD_CARDNUMBER + "/";

        client.delete(urlToDelete);

        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodsByClient() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        int CLIENT_ID = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String urlToDelete = "/paymentMethod/deleteByClient/" + CLIENT_ID;

        client.delete(urlToDelete);

        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }

    @Test
    public void testDeletePaymentMethodsByClient2() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        assertNotNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));

        int CLIENT_ID = clientRepository.findByEmail(CLIENT_EMAIL).getAccountId();

        String urlToDelete = "/paymentMethod/deleteByClient/" + CLIENT_ID + "/";

        client.delete(urlToDelete);

        assertNull(paymentMethodRepository.findByCardNumber(PAYMENTMETHOD_CARDNUMBER));
    }
/*
    @Test
    public void testCreatePaymentMethod() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethodRequestDto request = new PaymentMethodRequestDto(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CLIENT);

        ResponseEntity<PaymentMethodResponseDto> response = client.postForEntity("/paymentMethod/create", request, PaymentMethodResponseDto.class);
        
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
*/
    // add second create test
    
    @Test
    public void testHasPaymentMethod() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        String url = "/clients/hasPaymentMethod/" + CLIENT_EMAIL;

        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertTrue(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testHasPaymentMethod2() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);

        String url = "/clients/hasPaymentMethod/" + CLIENT_EMAIL + "/";

        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertTrue(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testDoesNotHavePaymentMethod() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);
        Client OTHER_CLIENT = new Client("notme@email.com", "John", "Pass123", "Doe", 2);
        clientRepository.save(OTHER_CLIENT);

        String url = "/clients/hasPaymentMethod/" + "notme@email.com";

        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertFalse(clientPayment.getHasPaymentMethod());
    }

    @Test
    public void testDoesNotHavePaymentMethod2() {
        Client PAYMENTMETHOD_CLIENT = new Client(CLIENT_EMAIL, "John", "Pass123", "Doe", 2);
        clientRepository.save(PAYMENTMETHOD_CLIENT);
        PaymentMethod paymentMethod = new PaymentMethod(PAYMENTMETHOD_CARDNUMBER, PAYMENTMETHOD_EXPDATE, PAYMENTMETHOD_CVC, PAYMENTMETHOD_CARDHOLDERNAME, PAYMENTMETHOD_CARDID, PAYMENTMETHOD_CLIENT);
        paymentMethodRepository.save(paymentMethod);
        Client OTHER_CLIENT = new Client("notme@email.com", "John", "Pass123", "Doe", 2);
        clientRepository.save(OTHER_CLIENT);

        String url = "/clients/hasPaymentMethod/" + "notme@email.com" + "/";

        ResponseEntity<ClientPaymentResponseDto> response = client.getForEntity(url, ClientPaymentResponseDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ClientPaymentResponseDto clientPayment = response.getBody();
        assertNotNull(clientPayment);
        assertFalse(clientPayment.getHasPaymentMethod());
    }

}
