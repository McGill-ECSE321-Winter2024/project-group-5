package ca.mcgill.ecse321.SportPlus.Repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import ca.mcgill.ecse321.SportPlus.dao.PaymentMethodRepository;
import ca.mcgill.ecse321.SportPlus.dao.ClientRepository;
import ca.mcgill.ecse321.SportPlus.model.Client;
import ca.mcgill.ecse321.SportPlus.model.PaymentMethod;
import java.util.List;

@SpringBootTest
public class PaymentMethodRepositoryTests {

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

    @Test
    @Transactional
    public void testFindByCardNumber() {

        // Create a client
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);
        // Given a PaymentMethod entity with a specific card number
        String cardNumber = "1234567890123456";
        PaymentMethod paymentMethod = new PaymentMethod(cardNumber, null, "123", "John Doe", 123, client);
        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod);

        // When findByCardNumber is executed
        PaymentMethod foundPaymentMethod = paymentMethodRepository.findByCardNumber(cardNumber);

        // Then the found PaymentMethod should match the saved one
        assertThat(foundPaymentMethod).isNotNull();
        assertThat(foundPaymentMethod.getCardNumber()).isEqualTo(cardNumber);
        assertEquals(cardNumber, foundPaymentMethod.getCardNumber());
        assertEquals("123", foundPaymentMethod.getCvc());
        assertEquals("John Doe", foundPaymentMethod.getCardHolderName());
        assertEquals(client, foundPaymentMethod.getClient());
        assertEquals(123, foundPaymentMethod.getCardId());

    }

    // List<PaymentMethod> findByClient(Client client);
    @Test
    public void testFindByClient() {

        // Create a client
        Client client = new Client("test@email.com", "John", "123321", "Doe", 0);
        clientRepository.save(client);

        // Given two PaymentMethods entity with a specific client with different cardIds
        String cardNumber1 = "1234567890123456";
        String cardNumber2 = "1234567890123453";
        PaymentMethod paymentMethod1 = new PaymentMethod(cardNumber1, null, "123", "John Doe", 123, client);
        PaymentMethod paymentMethod2 = new PaymentMethod(cardNumber2, null, "321", "John Doe", 321, client);
        // Use the repository to save the PaymentMethod
        paymentMethodRepository.save(paymentMethod1);
        paymentMethodRepository.save(paymentMethod2);

        // When findByCardNumber is executed
        List<PaymentMethod> foundPaymentMethods = paymentMethodRepository.findByClient(client);

        assertEquals(2, foundPaymentMethods.size());

        assertThat(foundPaymentMethods).contains(paymentMethod1, paymentMethod2);

    }

}
