package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.Date;
import java.util.List;
//import javax.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
@SpringBootTest
public class PaymentMethodRepositoryTests {
	@Autowired
	private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ClientRepository clientRepository;

	@AfterEach
	public void clearDatabase() {
		clientRepository.deleteAll();
        paymentMethodRepository.deleteAll();
	}

	//@Test
	public void testPersistAndLoadPerson() {
		// Create person and persist a client.
		String Firstname = "Muffin Man";
		Integer userId = 40;
		String password = "123 Drury Lane";
        String lastName="";
        String email="";
        Client client = new Client(email, Firstname, userId, password, lastName);
		clientRepository.save(client);

        // create a payment method.

        String cardNumber = "1234567890123456";
        Date expDate = Date.valueOf("2024-02-16");
        
        int cvc = 123;
        String cardHolderName = "Muffin Man";

        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, expDate, cvc, client);
        List<PaymentMethod> loadedPaymentMethod = paymentMethodRepository.findByClient(client);

        // Assert that payment method is not null and has correct attributes.
        assertNotNull(loadedPaymentMethod);
        /*
        assertEquals(cardNumber, loadedPaymentMethod.getCardNumber());
        assertEquals(expDate, loadedPaymentMethod.getExpDate());
        assertEquals(cvc, loadedPaymentMethod.getCvc());
        assertEquals(cardHolderName, loadedPaymentMethod.getCardHolderName());
        assertEquals(sportPlus, loadedPaymentMethod.getSportPlus());
        assertEquals(client, loadedPaymentMethod.getClient());*/
	}
}
